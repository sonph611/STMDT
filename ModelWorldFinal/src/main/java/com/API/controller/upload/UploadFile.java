package com.API.controller.upload;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadFile {
	@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")List<MultipartFile>  file) {
        System.out.println(file.size());
        // Lấy định dạng (loại MIME type) của tệp
//        String contentType = file.getContentType();
//        if ("image/jpeg".equals(contentType) || "image/png".equals(contentType)) {
//            System.out.println("Ảnh của bạn hợp lệ");
//        }
        return null;
    }
}
