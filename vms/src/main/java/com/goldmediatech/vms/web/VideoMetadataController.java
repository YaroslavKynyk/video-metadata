package com.goldmediatech.vms.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldmediatech.vms.service.VideoService;
import com.goldmediatech.vms.util.MessageQueue;
import com.goldmediatech.vms.util.VideoMetadataMapper;
import com.goldmediatech.vms.web.message.IngestRequest;

@RestController
@RequestMapping("/api/videos")
public class VideoMetadataController {

    private final VideoService videoService;

    public VideoMetadataController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importVideoMetadata(@RequestBody IngestRequest request) {
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA, String.format("[%s] Requesting video metadata", Thread.currentThread()));
        videoService.importVideoMetadata(VideoMetadataMapper.toIngestDto(request));
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA, String.format("[%s] Video metadata import request sent", Thread.currentThread()));
        return ResponseEntity.ok().build();
    }
}
