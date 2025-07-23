package com.goldmediatech.vms.service.dto;

import java.time.LocalDate;

public record SearchFilterDto(
    String source,
    LocalDate uploadDate,
    Long duration) {
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String source;
            private LocalDate uploadDate;
            private Long duration;

            public Builder source(String source) {
                this.source = source;
                return this;
            }

            public Builder uploadDate(LocalDate uploadDate) {
                this.uploadDate = uploadDate;
                return this;
            }

            public Builder duration(Long duration) {
                this.duration = duration;
                return this;
            }

            public SearchFilterDto build() {
                return new SearchFilterDto(source, uploadDate, duration);
            }
        }
}
