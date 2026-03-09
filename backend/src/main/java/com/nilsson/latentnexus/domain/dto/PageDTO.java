package com.nilsson.latentnexus.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A generic Data Transfer Object (DTO) designed for encapsulating paginated results
 * in a cursor-based pagination scheme.
 * <p>
 * This record provides a standardized structure for API responses that return
 * a subset of data along with a mechanism to fetch the next set of results.
 * Cursor-based pagination is preferred over offset-based pagination for large datasets
 * as it avoids performance degradation and issues with data consistency when
 * items are added or removed during pagination.
 * </p>
 *
 * @param <T>
 *         The type of the data items contained within this page.
 * @param data
 *         A {@link List} of data items for the current page.
 * @param nextCursor
 *         A {@link String} representing the cursor for the next page.
 *         If this value is `null`, it indicates that there are no more pages
 *         available, and the current page is the last one. The `nextCursor`
 *         typically corresponds to a unique identifier (e.g., `id` or `createdAt` timestamp)
 *         of the last item on the current page, which the client can then
 *         pass in a subsequent request to retrieve the next page.
 */
public record PageDTO<T>(
        List<T> data,
        @JsonProperty("next_cursor") String nextCursor
) {
}
