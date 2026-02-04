package tech3.binitright.interfacemethods;

import tech3.binitright.response.PresignedUploadResponse;

public interface VideoUploadInterface {
    PresignedUploadResponse createPresignedUpload(Long userId);

}
