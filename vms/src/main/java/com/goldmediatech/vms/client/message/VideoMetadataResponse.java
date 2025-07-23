package com.goldmediatech.vms.client.message;

public record VideoMetadataResponse(
        String id,
        String title,
        String creator,
        String uploadDate,
        Long duration,
        String description,
        String category,
        String status
) {
}
