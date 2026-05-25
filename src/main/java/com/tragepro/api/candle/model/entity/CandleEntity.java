package com.tragepro.api.candle.model.entity;

import com.tragepro.api.candle.model.Candle;
import com.tragepro.api.candle.model.Symbol;
import com.tragepro.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "candle")
@CompoundIndexes({
    @CompoundIndex(name = "symbol_timestamp_idx", def = "{'symbol.id': 1, 'candle.timestamp': -1}"),
    @CompoundIndex(name = "unique_candle_idx", def = "{'symbol.id': 1, 'candle.timestamp': 1}", unique = true),
    @CompoundIndex(name = "timestamp_idx", def = "{'candle.timestamp': -1}"),
    @CompoundIndex(name = "symbol_idx", def = "{'symbol.id': 1}")
})
public class CandleEntity extends BaseEntity {

    @Id
    private String id;

    private Symbol symbol;
    private Candle candle;
}
