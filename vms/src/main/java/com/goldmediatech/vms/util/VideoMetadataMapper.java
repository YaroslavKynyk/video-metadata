package com.goldmediatech.vms.util;

import java.time.LocalDateTime;

import com.goldmediatech.vms.client.message.VideoMetadataResponse;
import com.goldmediatech.vms.service.dto.IngestDto;
import com.goldmediatech.vms.service.dto.VideoMetadataDto;
import com.goldmediatech.vms.web.message.IngestRequest;
import com.goldmediatech.vms.web.message.VideoResponse;

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

    public static VideoMetadataDto toVideoMetadataDto(VideoMetadataResponse videoMetadata, String source) {
        return VideoMetadataDto.builder()
                .id(Long.valueOf(videoMetadata.id()))
                .source(source)
                .title(videoMetadata.title())
                .creator(videoMetadata.creator())
                .uploadDate(LocalDateTime.parse(videoMetadata.uploadDate()))
                .duration(videoMetadata.duration())
                .description(videoMetadata.description())
                .category(videoMetadata.category())
                .status(videoMetadata.status())
                .build();
    }

    public static VideoResponse toVideoResponse(VideoMetadataDto video) {
        return new VideoResponse(
                video.title(),
                video.description(),
                video.uploadDate().toLocalDate(),
                video.duration(),
                video.creator(),
                video.category(),
                video.status()
        );
    }
}
