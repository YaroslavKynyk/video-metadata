package com.goldmediatech.vms.persistence;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.goldmediatech.vms.service.dto.VideoMetadataDto;
import jakarta.persistence.EntityNotFoundException;

@Repository
public class VideoMetadataLoader {
    
    private final VideoMetadataRepository videoMetadataRepository;

    public VideoMetadataLoader(VideoMetadataRepository videoMetadataRepository) {
        this.videoMetadataRepository = videoMetadataRepository;
    }

    public void saveVideoMetadata(List<VideoMetadataDto> response) {
        var entities = response.stream()
                .map(this::toEntity)
                .toList();
        videoMetadataRepository.saveAll(entities);
    }

    public List<VideoMetadataDto> findAllVideoMetadata() {
        return videoMetadataRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public VideoMetadataDto findVideoById(Long id) {
        return videoMetadataRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with id: " + id));
    }

    private VideoMetadataEntity toEntity(VideoMetadataDto dto) {
        var entity = new VideoMetadataEntity();
        entity.setSourceId(dto.sourceId());
        entity.setSource(dto.source());
        entity.setTitle(dto.title());
        entity.setCreator(dto.creator());
        entity.setUploadDate(dto.uploadDate());
        entity.setDuration(dto.duration());
        entity.setDescription(dto.description());
        entity.setCategory(dto.category());
        entity.setStatus(dto.status());
        return entity;
    }

    private VideoMetadataDto toDto(VideoMetadataEntity entity) {
        return VideoMetadataDto.builder()
                .id(entity.getId())
                .source(entity.getSource())
                .sourceId(entity.getSourceId())
                .title(entity.getTitle())
                .creator(entity.getCreator())
                .uploadDate(entity.getUploadDate())
                .duration(entity.getDuration())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .build();
    }
}
