package com.goldmediatech.vms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.goldmediatech.vms.client.VideoMetadataClient;
import com.goldmediatech.vms.persistence.VideoMetadataLoader;
import com.goldmediatech.vms.service.dto.IngestDto;
import com.goldmediatech.vms.service.dto.SearchFilterDto;
import com.goldmediatech.vms.service.dto.VideoMetadataDto;
import com.goldmediatech.vms.service.dto.VideoStatisticsDto;
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

    public List<VideoStatisticsDto> loadVideoStatistics() {
        var videos = videoMetadataLoader.findAllVideoMetadata();
        // extract all video sources distinctly
        var sources = videos.stream()
                .map(VideoMetadataDto::source)
                .distinct()
                .collect(Collectors.toList());

        // calculate videos per source
        var videoCounts = videos.stream()
                .collect(
                    Collectors.groupingBy(VideoMetadataDto::source,
                    Collectors.counting()));

        // calculate video duration per source
        var videoDurations = videos.stream()
                .collect(
                    Collectors.groupingBy(VideoMetadataDto::source,
                    Collectors.summingLong(VideoMetadataDto::duration)));

        var statistics = new ArrayList<VideoStatisticsDto>();
        sources.forEach(source -> {
            statistics.add(new VideoStatisticsDto(
                source + " videos total",
                videoCounts.getOrDefault(source, 0L)
            ));
            statistics.add(new VideoStatisticsDto(
                source + " videos duration average",
                toMinutesDurationFormat(
                    videoDurations.getOrDefault(source, 0L) / videoCounts.getOrDefault(source, 1L))
            ));
        });
        return statistics;
    }

    private String toMinutesDurationFormat(long duration) {
        return duration / 60000 + " minutes";
    }
}
