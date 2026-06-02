package com.tragepro.api.data.service;

import com.tragepro.api.data.model.request.WatchListRequest;
import com.tragepro.api.data.model.response.WatchListResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing WatchLists.
 * Provides methods for CRUD and partial update (PATCH) operations on WatchLists.
 */
public interface WatchListService {

    /**
     * Creates a new WatchList.
     *
     * @param request the request body containing watchlist details
     * @return the created WatchList response
     */
    WatchListResponse create(WatchListRequest request);

    /**
     * Retrieves a WatchList by its ID.
     *
     * @param id the watchlist ID
     * @return an Optional containing the WatchList response if found, or empty otherwise
     */
    Optional<WatchListResponse> getById(String id);

    /**
     * Retrieves all WatchLists paginated.
     *
     * @param pageable pagination parameters
     * @return a page of WatchList responses
     */
    Page<WatchListResponse> getAll(Pageable pageable);

    /**
     * Updates an existing WatchList details fully.
     *
     * @param id the watchlist ID to update
     * @param request the new watchlist details
     * @return the updated WatchList response
     */
    WatchListResponse update(String id, WatchListRequest request);

    /**
     * Deletes a WatchList by its ID.
     *
     * @param id the watchlist ID to delete
     */
    void delete(String id);

    /**
     * Partially updates a WatchList (rename watchlist, add stock, update description).
     *
     * @param id the watchlist ID to update
     * @param request the partial update details
     * @return the updated WatchList response
     */
    WatchListResponse patch(String id, WatchListRequest request);
}
