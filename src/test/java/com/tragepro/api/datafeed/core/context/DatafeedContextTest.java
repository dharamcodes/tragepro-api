package com.tragepro.api.datafeed.core.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tragepro.api.domain.datafeed.DatafeedModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.DatafeedState;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatafeedContextTest {

    private DatafeedContext datafeedContext;

    @BeforeEach
    void setUp() {
        datafeedContext = new DatafeedContext();
    }

    @Test
    void testPutAndGet() {
        SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");
        DatafeedModel model = DatafeedModel.builder()
                .symbol("AAPL")
                .state(DatafeedState.INITIALIZED)
                .build();

        datafeedContext.put(symbol, model);
        assertEquals(model, datafeedContext.get(symbol));
    }

    @Test
    void testTransitionTo_NewSymbol_WithoutTimestamp() {
        SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");

        datafeedContext.transitionTo(symbol, DatafeedState.PROCESSING);

        DatafeedModel result = datafeedContext.get(symbol);
        assertNotNull(result);
        assertEquals("AAPL", result.getSymbol());
        assertEquals(DatafeedState.PROCESSING, result.getState());
        assertNull(result.getTimestamp());
    }

    @Test
    void testTransitionTo_NewSymbol_WithTimestamp() {
        SymbolDataModel symbol = new SymbolDataModel("GOOG", "Alphabet Inc.");
        LocalDate timestamp = LocalDate.of(2026, 8, 23);

        datafeedContext.transitionTo(symbol, DatafeedState.INITIALIZED, timestamp);

        DatafeedModel result = datafeedContext.get(symbol);
        assertNotNull(result);
        assertEquals("GOOG", result.getSymbol());
        assertEquals(DatafeedState.INITIALIZED, result.getState());
        assertEquals(timestamp, result.getTimestamp());
    }

    @Test
    void testTransitionTo_ExistingSymbol_UpdatesStateAndTimestamp() {
        SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");
        LocalDate initialDate = LocalDate.of(2026, 1, 1);
        LocalDate updatedDate = LocalDate.of(2026, 8, 23);

        datafeedContext.put(
                symbol,
                DatafeedModel.builder()
                        .symbol("AAPL")
                        .timestamp(initialDate)
                        .state(DatafeedState.INITIALIZED)
                        .build());

        datafeedContext.transitionTo(symbol, DatafeedState.COMPLETED, updatedDate);

        DatafeedModel result = datafeedContext.get(symbol);
        assertNotNull(result);
        assertEquals(DatafeedState.COMPLETED, result.getState());
        assertEquals(updatedDate, result.getTimestamp());
    }

    @Test
    void testTransitionTo_ExistingSymbol_RetainsTimestampWhenNull() {
        SymbolDataModel symbol = new SymbolDataModel("AAPL", "Apple Inc.");
        LocalDate initialDate = LocalDate.of(2026, 1, 1);

        datafeedContext.put(
                symbol,
                DatafeedModel.builder()
                        .symbol("AAPL")
                        .timestamp(initialDate)
                        .state(DatafeedState.PROCESSING)
                        .build());

        datafeedContext.transitionTo(symbol, DatafeedState.INITIALIZED);

        DatafeedModel result = datafeedContext.get(symbol);
        assertNotNull(result);
        assertEquals(DatafeedState.INITIALIZED, result.getState());
        assertEquals(initialDate, result.getTimestamp());
    }
}
