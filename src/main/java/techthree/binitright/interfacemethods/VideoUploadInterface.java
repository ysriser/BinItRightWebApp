package techthree.binitright.interfacemethods;

import techthree.binitright.response.PresignedUploadResponse;

public interface VideoUploadInterface {
    PresignedUploadResponse createPresignedUpload(Long userId);

}
