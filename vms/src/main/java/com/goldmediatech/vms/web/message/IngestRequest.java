package com.goldmediatech.vms.web.message;

public record IngestRequest(String source, String query, long limit) {
}
