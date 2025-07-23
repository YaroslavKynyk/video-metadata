package com.goldmediatech.vms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldmediatech.vms.client.message.VideoMetadataResponse;
import com.goldmediatech.vms.service.dto.IngestDto;
import com.goldmediatech.vms.service.dto.VideoMetadataDto;
import com.goldmediatech.vms.util.VideoMetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class VideoMetadataClient {

    private static final Logger log = LoggerFactory.getLogger(VideoMetadataClient.class);
    private static final long PARALLEL_STREAMING_THRESHOLD = 100000;

    public List<VideoMetadataDto> requestVideoMetadata(IngestDto dto) {
        List<VideoMetadataResponse> responses = fetchVideoMetadata(dto);

        if (responses == null || responses.isEmpty()) {
            log.warn("[VIDEO METADATA] No video metadata found for the given filters: {}", dto);
            return List.of();
        }

        if (responses.size() > PARALLEL_STREAMING_THRESHOLD) {
            log.info("[VIDEO METADATA] Processing {} video metadata entries in parallel", responses.size());
            return responses.parallelStream()
                    .map(response -> VideoMetadataMapper.toVideoMetadataDto(response, dto.source()))
                    .toList();

        } else {
            log.info("[VIDEO METADATA] Processing {} video metadata entries sequentially", responses.size());
            return responses.stream()
                    .map(response -> VideoMetadataMapper.toVideoMetadataDto(response, dto.source()))
                    .toList();
        }
    }

    // This method simulates fetching video metadata from an external service
    private List<VideoMetadataResponse> fetchVideoMetadata(IngestDto dto) {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("fixture/video-metadata-fixture.json")) {
            if (inputStream == null) {
                throw new RuntimeException("Fixture file not found");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream,
                    objectMapper
                            .getTypeFactory()
                            .constructCollectionType(List.class, VideoMetadataResponse.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load video metadata fixture", e);
        }
    }
}
