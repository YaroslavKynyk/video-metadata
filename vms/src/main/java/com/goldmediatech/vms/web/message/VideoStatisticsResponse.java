package com.goldmediatech.vms.web.message;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents statistics for a video.")
public record VideoStatisticsResponse(
    @Schema(description = "A short explanation", example = "YouTube videos total")
    String title,
    @Schema(description = "The statistics data for the video", example = "36 minutes")
    Object statistics
) {
}
