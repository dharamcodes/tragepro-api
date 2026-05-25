package com.tragepro.api.candle.provider.impl;

import com.tragepro.api.candle.config.DataProviderProperties;
import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.provider.DataProviderClient;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestClientDataProvider implements DataProviderClient {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final RestClient dataProviderRestClient;
    private final DataProviderProperties properties;

    @Override
    public List<CandleRequest> fetchAll(CandleInterval interval) {
        List<CandleRequest> results = new ArrayList<>();
        if (properties.clients() == null || properties.clients().isEmpty()) {
            log.warn("No data providers configured.");
            return results;
        }

        for (Map.Entry<String, DataProviderProperties.ProviderConfig> entry :
                properties.clients().entrySet()) {
            processProvider(entry.getKey(), entry.getValue(), interval, results);
        }

        log.info("Total records fetched from providers: {}", results.size());
        return results;
    }

    private void processProvider(
            String clientName,
            DataProviderProperties.ProviderConfig config,
            CandleInterval interval,
            List<CandleRequest> results) {
        List<String> symbols = config.symbols();
        if (symbols == null || symbols.isEmpty()) {
            log.warn("No symbols configured for provider: {}", clientName);
            return;
        }

        int batchSize = config.batchSize() > 0 ? config.batchSize() : 100;

        for (int i = 0; i < symbols.size(); i += batchSize) {
            List<String> batch = symbols.subList(i, Math.min(i + batchSize, symbols.size()));
            try {
                List<CandleRequest> batchResult = fetchBatch(config, batch, interval);
                results.addAll(batchResult);
                log.debug(
                        "Fetched {} records for batch starting at index {} from provider {}",
                        batchResult.size(),
                        i,
                        clientName);
            } catch (RestClientException ex) {
                log.error(
                        "Failed to fetch batch [{}-{}] from provider {}: {}",
                        i,
                        i + batchSize,
                        clientName,
                        ex.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<CandleRequest> fetchBatch(
            DataProviderProperties.ProviderConfig config, List<String> symbols, CandleInterval interval) {
        RestClient.RequestBodySpec requestSpec = buildRequestSpec(config, symbols, interval);
        List<Map<String, Object>> rawResponse = requestSpec.retrieve().body(List.class);

        if (rawResponse == null || rawResponse.isEmpty()) {
            return List.of();
        }

        return rawResponse.stream().map(this::mapToRequest).toList();
    }

    private RestClient.RequestBodySpec buildRequestSpec(
            DataProviderProperties.ProviderConfig config, List<String> symbols, CandleInterval interval) {
        String symbolsCsv = String.join(",", symbols);
        String methodStr = config.method() != null ? config.method().toUpperCase() : "GET";
        org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.valueOf(methodStr);

        String dataUrl = config.dataUrl() != null ? config.dataUrl() : "";
        String finalDataUrl = dataUrl.replace("{symbols}", symbolsCsv).replace("{interval}", interval.getValue());

        RestClient.RequestBodySpec bodySpec =
                dataProviderRestClient.method(method).uri(config.baseUrl() + finalDataUrl);

        if (config.apiKey() != null && !config.apiKey().isBlank()) {
            bodySpec = bodySpec.header(API_KEY_HEADER, config.apiKey());
        }

        if (config.authToken() != null && !config.authToken().isBlank()) {
            bodySpec = bodySpec.header("Authorization", "Bearer " + config.authToken());
        }

        String requestBody = config.body();
        if (requestBody != null
                && !requestBody.isBlank()
                && (method == org.springframework.http.HttpMethod.POST
                        || method == org.springframework.http.HttpMethod.PUT)) {
            String populatedBody =
                    requestBody.replace("{symbols}", symbolsCsv).replace("{interval}", interval.getValue());
            bodySpec = bodySpec.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(populatedBody);
        }

        return bodySpec;
    }

    private CandleRequest mapToRequest(Map<String, Object> raw) {
        String symbolId = String.valueOf(raw.get("symbolId"));
        String symbolName = String.valueOf(raw.getOrDefault("symbolName", symbolId));

        long timestamp = raw.containsKey("timestamp")
                ? ((Number) raw.get("timestamp")).longValue()
                : Instant.now().toEpochMilli();

        Candle candle = Candle.builder()
                .timestamp(timestamp)
                .open(toDouble(raw.get("open")))
                .high(toDouble(raw.get("high")))
                .low(toDouble(raw.get("low")))
                .close(toDouble(raw.get("close")))
                .volume(toDouble(raw.get("volume")))
                .build();

        Symbol symbol = Symbol.builder().id(symbolId).name(symbolName).build();

        return new CandleRequest(symbol, candle);
    }

    private double toDouble(Object value) {
        return value instanceof Number n ? n.doubleValue() : 0.0;
    }
}
