package com.goldmediatech.vms.web.message;

import java.time.LocalDate;

public record VideoResponse(
    String title,
    String description,
    LocalDate uploadDate,
    Long duration,
    String creator,
    String category,
    String status) {
}
