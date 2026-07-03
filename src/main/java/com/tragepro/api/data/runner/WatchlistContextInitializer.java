package com.tragepro.api.data.runner;

import com.tragepro.api.common.context.WatchlistContext;
import com.tragepro.api.data.model.response.WatchListResponse;
import com.tragepro.api.data.service.WatchListService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WatchlistContextInitializer implements CommandLineRunner {

  private final WatchListService watchListService;
  private final WatchlistContext watchlistContext;

  @Override
  public void run(String @NonNull ... args) throws Exception {
    Set<WatchListResponse> response = watchListService.getAll();
    log.info("Initializing Watchlist - Count :: {}", response.size());
    response.forEach(
        watchListResponse ->
            watchlistContext.addWatchlist(watchListResponse.name(), watchListResponse.stocks()));
    log.info("Initialized Watchlist - Count :: {}", response.size());
  }
}
