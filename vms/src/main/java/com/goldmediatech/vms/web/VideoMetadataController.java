package com.goldmediatech.vms.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goldmediatech.vms.service.VideoService;
import com.goldmediatech.vms.util.GlobalExceptionHandler;
import com.goldmediatech.vms.util.MessageQueue;
import com.goldmediatech.vms.util.VideoMetadataMapper;
import com.goldmediatech.vms.web.message.IngestRequest;

@RestController
@RequestMapping("/api/videos")
@Tag(
    name = "Video Metadata",
    description = "Operations related to processing videos metadata."
)
@SecurityRequirement(name = "JWT Auth")
@SecurityScheme(
    name = "JWT Auth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT Authentication for API access"
)
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = GlobalExceptionHandler.CODE_400_ERROR_MESSAGE),
        @ApiResponse(responseCode = "401", description = GlobalExceptionHandler.CODE_401_ERROR_MESSAGE),
        @ApiResponse(responseCode = "403", description = GlobalExceptionHandler.CODE_403_ERROR_MESSAGE),
        @ApiResponse(responseCode = "500", description = GlobalExceptionHandler.CODE_500_ERROR_MESSAGE)
})
public class VideoMetadataController {

    private final VideoService videoService;

    public VideoMetadataController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(
        summary = "Import video metadata",
        description = """
                    Initiates an asynchronous process to import video metadata from specified external source.
                    Only users with the ADMIN role can access this endpoint.
                    """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = IngestRequest.class),
                examples = {
                    @ExampleObject(
                    name = "Import from YouTube Channel",
                    description = "Import all videos metadata from a specified YouTube channel",
                    value = """
                            { 
                                "source": "YouTube",
                                "query": "GMT Tutorials",
                                "limit": "0"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Import from Mock Service",
                        description = "Import latest 1000 videos from a mock service on specified topic",
                        value = """
                            { 
                                "source": "Mock",
                                "query": "Java Explained",
                                "limit": "1000"
                            }
                            """
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Video metadata import request sent and processed asynchronously in the background"),
        }
    )
    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> importVideoMetadata(@RequestBody IngestRequest request) {
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA,
                String.format("[%s] Requesting video metadata", Thread.currentThread()));
        videoService.importVideoMetadata(VideoMetadataMapper.toIngestDto(request));
        MessageQueue.sendMessage(MessageQueue.TOPIC_VIDEO_METADATA,
                String.format("[%s] Video metadata import request sent", Thread.currentThread()));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> loadAllVideos(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String uploadDate,
            @RequestParam(required = false) Integer duration) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> loadVideo(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<?> loadVideoStatistics() {
        return ResponseEntity.ok().build();
    }
}
