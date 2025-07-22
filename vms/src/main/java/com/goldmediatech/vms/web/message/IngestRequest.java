package com.goldmediatech.vms.web.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Represents a request to ingest data from a specified source with a query and limit.
 *
 * @param source the source from which data is to be ingested
 * @param query the query string to filter or select data
 * @param limit the maximum number of records to ingest
 *
 */
public record IngestRequest(
    @Schema(
        description = "The external source to import video metadata from (e.g., YouTube, Vimeo).",
        example = "YouTube",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Source must not be blank")
    String source,

    @Schema(
        description = "The query string to filter or select data.",
        example = "GMT Tutorials",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Query must not be blank")
    String query,
    
    @Schema(
        description = "The maximum number of videos to ingest. Process all if set to 0.",
        example = "100",
        minimum = "0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @PositiveOrZero(message = "Limit must be zero or a positive number")
    long limit) {
}
