package com.goldmediatech.vms.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

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
import com.goldmediatech.vms.service.dto.SearchFilterDto;
import com.goldmediatech.vms.util.GlobalExceptionHandler;
import com.goldmediatech.vms.util.VideoMetadataMapper;
import com.goldmediatech.vms.web.message.IngestRequest;
import com.goldmediatech.vms.web.message.VideoResponse;
import com.goldmediatech.vms.web.message.VideoStatisticsResponse;

@RestController
@RequestMapping("/api/videos")
@Tag(
    name = "Video",
    description = "Operations related to processing videos."
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
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
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
        videoService.importVideoMetadata(VideoMetadataMapper.toIngestDto(request));
        return ResponseEntity.ok().build();
    }

        @Operation(
        summary = "Search and retrieve video metadata",
        description = "Retrieves a list of video metadata records, optionally filtered by source, upload date, or duration. " +
                      "Parameters are optional and can be combined.",
        parameters = {
            @Parameter(name = "source", description = "Filter by the video source.", example = "YouTube"),
            @Parameter(name = "uploadDate", description = "Filter by upload date (YYYY-MM-DD).", example = "2025-07-23"),
            @Parameter(name = "duration", description = "Filter by minimum video duration in milliseconds.", example = "3600000")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved list of videos.",
                content = @Content(mediaType = "application/json",
                                   schema = @Schema(implementation = VideoResponse.class, type = "array"))
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<VideoResponse>> searchVideos(
            @RequestParam(name = "source", required = false) String source,
            @RequestParam(name = "uploadDate", required = false) String uploadDate,
            @RequestParam(name = "duration", required = false) Long duration) {
        var videos = videoService.searchVideos(SearchFilterDto.builder()
                .source(source)
                .uploadDate(LocalDate.parse(uploadDate))
                .duration(duration)
                .build());
        var response = videos.stream()
                .map(VideoMetadataMapper::toVideoResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Retrieve video metadata by ID",
        description = "Fetches the detailed metadata for a single video using its unique identifier.",
        parameters = {
            @Parameter(name = "id", description = "The unique identifier of the video.", required = true, example = "101",
                       schema = @Schema(type = "integer", format = "int64"))
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved video metadata.",
                content = @Content(mediaType = "application/json",
                                   schema = @Schema(implementation = VideoResponse.class))
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> searchVideo(@PathVariable Long id) {
        var video = videoService.searchVideo(id);
        return ResponseEntity.ok(VideoMetadataMapper.toVideoResponse(video));
    }

    @Operation(
        summary = "Get video statistics",
        description = "Retrieves a list of video analytics.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved video statistics.",
                content = @Content(mediaType = "application/json",
                                   schema = @Schema(implementation = VideoStatisticsResponse.class, type = "array"))
            )
        }
    )
    @GetMapping("/stats")
    public ResponseEntity<List<VideoStatisticsResponse>> loadVideoStatistics() {
        var response = videoService.loadVideoStatistics().stream()
                .map(VideoMetadataMapper::toVideoStatisticsResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
