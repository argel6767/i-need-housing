package com.ineedhousing.backend.azure.blob.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileConverter {

    /*
     * converts the file back into a regular File object
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
       // null/empty check
        if (multipartFile == null || multipartFile.getSize() == 0) {
            throw new IllegalArgumentException("The multipartFile must not be null or empty");
        }
        if (multipartFile.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new IllegalArgumentException("File size exceeds 5MB limit");
        }
        // content type check
        String contentType = multipartFile.getContentType();
        if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType))) {
            throw new IllegalArgumentException("The multipartFile must contain image/jpeg or image/png");
        }
        // Create a temporary file and copy the contents of the multipart file
        File file = null;
        String tag = String.valueOf((int)(System.currentTimeMillis() / 1000));
        file = File.createTempFile(tag + "-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }
}
