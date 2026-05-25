package com.tragepro.api.watchlist.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WatchlistRequest(
        @NotBlank(message = "Watchlist name is required")
                @Size(min = 1, max = 50, message = "Watchlist name must be between 1 and 50 characters")
                String name) {}
