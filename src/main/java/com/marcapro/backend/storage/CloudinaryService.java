package com.marcapro.backend.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image",
                "quality", "auto",
                "fetch_format", "auto"
            ));
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }
    }

    public String uploadVideo(MultipartFile file, String folder) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "video"
            ));
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error uploading video to Cloudinary", e);
        }
    }

    public void deleteByUrl(String url) {
        try {
            String publicId = extractPublicId(url);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting from Cloudinary", e);
        }
    }

    private String extractPublicId(String url) {
        int uploadIndex = url.indexOf("/upload/");
        String afterUpload = url.substring(uploadIndex + 8);
        int dotIndex = afterUpload.lastIndexOf('.');
        return dotIndex > 0 ? afterUpload.substring(0, dotIndex) : afterUpload;
    }
}
