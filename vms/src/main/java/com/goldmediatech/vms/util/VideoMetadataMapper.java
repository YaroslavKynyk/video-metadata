package com.goldmediatech.vms.util;

import com.goldmediatech.vms.service.dto.IngestDto;
import com.goldmediatech.vms.web.message.IngestRequest;

public final class VideoMetadataMapper {

    private VideoMetadataMapper() {
        // Utility class, prevent instantiation
    }

    public static IngestDto toIngestDto(IngestRequest request) {
        return IngestDto.builder()
                .source(request.source())
                .query(request.query())
                .limit(request.limit())
                .build();
    }
}
