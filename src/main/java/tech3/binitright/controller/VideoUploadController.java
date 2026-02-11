package tech3.binitright.controller;

import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.VideoUploadInterface;
import tech3.binitright.request.PresignedUploadRequest;
import tech3.binitright.response.PresignedUploadResponse;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
public class VideoUploadController {

    private final VideoUploadInterface videoService;

    public VideoUploadController(VideoUploadInterface videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/presign-upload")
    public PresignedUploadResponse presignUpload(
            @RequestBody PresignedUploadRequest req) {
        return videoService.createPresignedUpload(req.getUserId());
    }
}