package com.goldmediatech.vms.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDto {
    private final String token;
}
