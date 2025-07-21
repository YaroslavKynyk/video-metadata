package com.goldmediatech.vms.persistence;

import org.springframework.stereotype.Repository;

@Repository
public class VideoMetadataRepository {

    public void save(Object response) {
        System.out.println("Saving video metadata...");
    }
    
}