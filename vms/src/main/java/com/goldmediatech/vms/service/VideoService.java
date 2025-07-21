package com.goldmediatech.vms.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.goldmediatech.vms.client.VideoMetadataClient;
import com.goldmediatech.vms.persistence.VideoMetadataRepository;
import com.goldmediatech.vms.service.dto.IngestDto;
import com.goldmediatech.vms.util.MessageQueue;

@Service
public class VideoService {

    private final VideoMetadataClient videoMetadataClient;
    private final VideoMetadataRepository videoMetadataRepository;

    public VideoService(VideoMetadataClient videoMetadataClient, VideoMetadataRepository videoMetadataRepository) {
        this.videoMetadataClient = videoMetadataClient;
        this.videoMetadataRepository = videoMetadataRepository;
    }

    @Async
    public void importVideoMetadata(IngestDto dto) {
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA, String.format("[%s] Requesting video metadata", Thread.currentThread()));
        var response = videoMetadataClient.requestVideoMetadata(dto);
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA, String.format("[%s] Received video metadata", Thread.currentThread()));
        videoMetadataRepository.save(response);
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA, String.format("[%s] Video metadata import completed", Thread.currentThread()));
    }
}
