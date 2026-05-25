package com.tragepro.api.watchlist.model.entity;

import com.tragepro.api.common.model.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "watchlists")
public class WatchlistEntity extends BaseEntity {

    @Id
    private String id;

    @Indexed
    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotBlank(message = "Watchlist name cannot be blank")
    private String name;

    @Builder.Default
    private Set<String> symbols = new HashSet<>();
}
