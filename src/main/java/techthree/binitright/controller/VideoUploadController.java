package techthree.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import techthree.binitright.interfacemethods.VideoUploadInterface;
import techthree.binitright.request.PresignedUploadRequest;
import techthree.binitright.response.PresignedUploadResponse;
import techthree.binitright.service.VideoUploadImplementation;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
public class VideoUploadController {

    @Autowired
    private VideoUploadInterface videoService;

    public void setVideoService(VideoUploadImplementation videoserviceImp) {
        this.videoService = videoserviceImp;
    }

    @PostMapping("/presign-upload")
    public PresignedUploadResponse presignUpload(
            @RequestBody PresignedUploadRequest req) {
        return videoService.createPresignedUpload(req.getUserId());
    }
}