package com.ineedhousing.backend.azure.blob.utils;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.java.Log;

@Log
public class FileConverter {

    /**
     * converts the MultipartFile back into a regular File object
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        log.info("converting multipart file to regular file");
       // null/empty check
        if (multipartFile == null || multipartFile.getSize() == 0) {
            log.warning("multipartFile cannot be null or empty");
            throw new IllegalArgumentException("The multipartFile must not be null or empty");
        }
        if (multipartFile.getSize() > 7 * 1024 * 1024) { // 7MB limit
            log.warning("file size is too large");
            throw new IllegalArgumentException("File size exceeds 7MB limit");
        }
        // content type check
        String contentType = multipartFile.getContentType();
        if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType) || "image/jpg".equals(contentType))) {
            log.warning("File is not a valid image type");
            throw new IllegalArgumentException("The multipartFile must be an image");
        }
        // Create a temporary file and copy the contents of the multipart file
        log.info("creating temporary file to house new file");
        String tag = String.valueOf((int)(System.currentTimeMillis() / 1000));
        File file = File.createTempFile(tag + "-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        log.info("file successfully converted");
        return file;
    }

    /**
     * converts a file to the desired jpg format
     */
    public static File convertFileToJpg(File file) throws IOException {
        // null/empty check
        if (file == null || file.length() == 0) {
            log.warning("multipartFile cannot be null or empty");
            throw new IllegalArgumentException("The file must not be null or empty");
        }
        if (file.length() > 7 * 1024 * 1024) { // 7MB limit
            log.warning("file size is too large");
            throw new IllegalArgumentException("File size exceeds 7MB limit");
        }
        if (file.getName().endsWith(".jpg")) {
            log.info("file is already of correct /jpg type");
            return file;
        }
        if (!(file.getName().endsWith(".jpeg") || file.getName().endsWith(".png"))) {
            log.warning("file is not a valid image type");
            throw new IllegalArgumentException("The file must be an image");
        }
        
        //convert file
        BufferedImage image = ImageIO.read(file);
        
        BufferedImage convertedImage;

        if (image == null) {
            log.severe("file could not be converted, using original file as fallback");
            return file;
        }

        if (file.getName().toLowerCase().endsWith(".png")) {
            log.info("Converting PNG to RGB format for JPEG compatibility");
            
            // Create a new RGB image
            convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            
            // Get the graphics context of the new image and fill with white background
            Graphics2D g2d = convertedImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, convertedImage.getWidth(), convertedImage.getHeight());
            
            // Draw the original image on top of the white background
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }
        else {
            convertedImage = image; //no extra steps needed if not png
        }

        //create new temp file for converted
        log.info("creating temp file to house converted file");
        File convertedFile = File.createTempFile("converted-file", ".jpg");
        boolean isSuccessful = ImageIO.write(convertedImage, "jpg", convertedFile);

        if (!isSuccessful) {
            log.severe("Failed to write to new file");
            throw new IOException("ImageIO.write() failed to write over file");
        }

        log.info("file successfully converted to jpg");
        log.info("new file: "+ convertedFile.getName());
        return convertedFile;
    }
}
