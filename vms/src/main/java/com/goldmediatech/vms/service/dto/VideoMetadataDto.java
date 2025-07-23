package com.goldmediatech.vms.service.dto;

import java.time.LocalDateTime;

public record VideoMetadataDto(
    Long id,
    String source,
    String sourceId,
    String title,
    String creator,
    LocalDateTime uploadDate,
    Long duration,
    String description,
    String category,
    String status) {
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long id;
            private String source;
            private String sourceId;
            private String title;
            private String creator;
            private LocalDateTime uploadDate;
            private Long duration;
            private String description;
            private String category;
            private String status;

            public Builder id(Long id) {
                this.id = id;
                return this;
            }

            public Builder source(String source) {
                this.source = source;
                return this;
            }

            public Builder sourceId(String sourceId) {
                this.sourceId = sourceId;
                return this;
            }

            public Builder title(String title) {
                this.title = title;
                return this;
            }

            public Builder creator(String creator) {
                this.creator = creator;
                return this;
            }

            public Builder uploadDate(LocalDateTime uploadDate) {
                this.uploadDate = uploadDate;
                return this;
            }

            public Builder duration(Long duration) {
                this.duration = duration;
                return this;
            }

            public Builder description(String description) {
                this.description = description;
                return this;
            }

            public Builder category(String category) {
                this.category = category;
                return this;
            }

            public Builder status(String status) {
                this.status = status;
                return this;
            }

            public VideoMetadataDto build() {
                return new VideoMetadataDto(id, source, sourceId, title, creator, uploadDate, duration, description, category, status);
            }
        }
}
