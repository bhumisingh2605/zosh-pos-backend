package com.zosh.zosh.pos.system.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    String uploadImage(MultipartFile file) throws Exception;
}