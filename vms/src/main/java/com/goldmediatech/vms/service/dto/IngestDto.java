package com.goldmediatech.vms.service.dto;

public record IngestDto(String source, String query, long limit) {
    public static IngestDtoBuilder builder() {
        return new IngestDtoBuilder();
    }

    public static class IngestDtoBuilder {
        private String source;
        private String query;
        private long limit;

        public IngestDtoBuilder source(String source) {
            this.source = source;
            return this;
        }

        public IngestDtoBuilder query(String query) {
            this.query = query;
            return this;
        }

        public IngestDtoBuilder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public IngestDto build() {
            return new IngestDto(source, query, limit);
        }
    }
}
