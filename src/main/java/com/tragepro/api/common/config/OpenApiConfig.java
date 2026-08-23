package com.tragepro.api.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =
                @Info(
                        title = "TradePro API Docs",
                        version = "1.0",
                        description =
                                "Comprehensive API documentation for TradePro, covering identity management, authentication, historical and intraday candle data feeds, watchlists, algorithmic strategy configurations, and trading journals."),
        tags = {
            @Tag(
                    name = "1. Identity & Access Management",
                    description = "Authentication, registration, password management, and account profiles"),
            @Tag(
                    name = "2. Market Data & Feeds",
                    description = "Watchlists, OHLCV candle data, and datafeed ingestion"),
            @Tag(
                    name = "3. Trading & Execution",
                    description = "Order submission, order status, and execution management"),
            @Tag(
                    name = "4. Trade Journal & Analytics",
                    description = "Trading journals, logs, and performance filtering")
        },
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {}
