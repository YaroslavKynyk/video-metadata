package com.goldmediatech.vms.client;

import org.springframework.stereotype.Component;

import com.goldmediatech.vms.service.dto.IngestDto;

@Component
public class VideoMetadataClient {

    public Object requestVideoMetadata(IngestDto dto) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // Simulate latency for the request
        return new Object(); // Simulate a response
    }
}
