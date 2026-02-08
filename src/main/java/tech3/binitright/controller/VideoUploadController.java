package tech3.binitright.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.interfacemethods.VideoUploadInterface;
import tech3.binitright.request.PresignedUploadRequest;
import tech3.binitright.response.PresignedUploadResponse;
import tech3.binitright.service.VideoUploadImplementation;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
public class VideoUploadController {

    @Autowired
    private VideoUploadInterface videoService;

    public void setVideoService(final VideoUploadImplementation videoserviceImp) {
        this.videoService = videoserviceImp;
    }


    @PostMapping("/presign-upload")
    public PresignedUploadResponse presignUpload(
            @RequestBody final PresignedUploadRequest req) {
        return videoService.createPresignedUpload(req.getUserId());
    }
}