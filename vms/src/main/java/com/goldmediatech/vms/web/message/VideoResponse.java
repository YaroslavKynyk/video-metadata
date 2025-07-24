package com.goldmediatech.vms.web.message;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents detailed metadata for a single video.")
public record VideoResponse(
    @Schema(description = "The title of the video", example = "Exploring the Alps: A Travel Documentary")
    String title,
    @Schema(description = "A brief description of the video", example = "A breathtaking journey through the Alps.")
    String description,
    @Schema(description = "The date the video was uploaded (YYYY-MM-DD)", example = "2025-03-15")    
    LocalDate uploadDate,
    @Schema(description = "The duration of the video in milliseconds", example = "5400000")
    Long duration,
    @Schema(description = "The creator or channel that uploaded the video", example = "Travel Beats")
    String creator,
    @Schema(description = "The category the video belongs to (e.g., Travel, Education, Food)", example = "Travel")
    String category,
    @Schema(description = "The current status of the video", example = "ACTIVE", allowableValues = {"ACTIVE", "PENDING_REVIEW", "ARCHIVED", "DELETED", "DRAFT"})
    String status) {
}
