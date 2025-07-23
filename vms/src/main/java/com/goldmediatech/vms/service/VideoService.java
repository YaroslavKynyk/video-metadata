package com.goldmediatech.vms.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.goldmediatech.vms.client.VideoMetadataClient;
import com.goldmediatech.vms.persistence.VideoMetadataLoader;
import com.goldmediatech.vms.service.dto.IngestDto;
import com.goldmediatech.vms.service.dto.SearchFilterDto;
import com.goldmediatech.vms.service.dto.VideoMetadataDto;
import com.goldmediatech.vms.util.MessageQueue;

@Service
public class VideoService {

    private final VideoMetadataClient videoMetadataClient;
    private final VideoMetadataLoader videoMetadataLoader;

    public VideoService(VideoMetadataClient videoMetadataClient, VideoMetadataLoader videoMetadataLoader) {
        this.videoMetadataClient = videoMetadataClient;
        this.videoMetadataLoader = videoMetadataLoader;
    }

    @Async
    public void importVideoMetadata(IngestDto dto) {
        var response = videoMetadataClient.requestVideoMetadata(dto);
        videoMetadataLoader.saveVideoMetadata(response);
        // Simulate successful message to a broker for processes like push notifications, mail, etc.
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA, "Video Metadata Imported Stub");
    }

    public List<VideoMetadataDto> searchVideos(SearchFilterDto searchFilter) {
        return videoMetadataLoader.findAllVideoMetadata();
    }

    public VideoMetadataDto searchVideo(Long id) {
        return videoMetadataLoader.findVideoById(id);
    }
}
