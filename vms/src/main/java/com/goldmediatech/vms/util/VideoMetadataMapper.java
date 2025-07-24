package com.goldmediatech.vms.util;

import java.time.OffsetDateTime;

import com.goldmediatech.vms.client.message.VideoMetadataResponse;
import com.goldmediatech.vms.service.dto.IngestDto;
import com.goldmediatech.vms.service.dto.VideoMetadataDto;
import com.goldmediatech.vms.service.dto.VideoStatisticsDto;
import com.goldmediatech.vms.web.message.IngestRequest;
import com.goldmediatech.vms.web.message.VideoResponse;
import com.goldmediatech.vms.web.message.VideoStatisticsResponse;

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
                .sourceId(videoMetadata.id())
                .source(source)
                .title(videoMetadata.title())
                .creator(videoMetadata.creator())
                .uploadDate(OffsetDateTime.parse(videoMetadata.uploadDate()).toLocalDateTime())
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

    public static VideoStatisticsResponse toVideoStatisticsResponse(VideoStatisticsDto dto) {
        return new VideoStatisticsResponse(
                dto.title(),
                dto.statistics()
        );
    }
}
