package com.goldmediatech.vms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadataEntity, Long> {
}