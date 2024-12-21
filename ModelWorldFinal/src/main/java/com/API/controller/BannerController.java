package com.API.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.API.model.OtherImageBanner;
import com.API.model.SwiperImage;
import com.API.repository.OtherImageRepository;
import com.API.repository.SwiperImageRepository;
import com.API.utils.ObjectRespone;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@RestController
@CrossOrigin("*")
public class BannerController {

	  @Autowired
	    private Cloudinary cloudinary;

	    @Autowired
	    private SwiperImageRepository swiperImageRepository;

	    @Autowired
	    private OtherImageRepository otherImageRepository;

	    // API để xóa ảnh Swiper cũ trong Cloudinary và DB
	    private void deleteOldSwiperImages() {
	        // Xóa ảnh trong Cloudinary
	        List<SwiperImage> swiperImages = swiperImageRepository.findAll();
	        for (SwiperImage image : swiperImages) {
	            try {
	                String publicId = extractPublicId(image.getImageUrl());
	                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	            } catch (Exception e) {
	                System.out.println("Error deleting image in Cloudinary: " + e.getMessage());
	            }
	        }
	        // Xóa ảnh trong database
	        swiperImageRepository.deleteAll();
	    }

	    // API để xóa ảnh Banner cũ trong Cloudinary và DB
	    private void deleteOldBannerImages() {
	        // Xóa ảnh trong Cloudinary
	        List<OtherImageBanner> otherImages = otherImageRepository.findAll();
	        for (OtherImageBanner image : otherImages) {
	            try {
	                String publicId = extractPublicId(image.getImageUrl());
	                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	            } catch (Exception e) {
	                System.out.println("Error deleting image in Cloudinary: " + e.getMessage());
	            }
	        }
	        // Xóa ảnh trong database
	        otherImageRepository.deleteAll();
	    }

	    private String extractPublicId(String imageUrl) {
	        String[] urlParts = imageUrl.split("/v[0-9]+/");
	        String publicIdWithExtension = urlParts[urlParts.length - 1];
	        return publicIdWithExtension.split("\\.")[0];
	    }

	    // API để upload ảnh Swiper
	    @PostMapping("/addSwiper")
	    public ResponseEntity<ObjectRespone> addSwiper(@RequestParam("swiperImages") MultipartFile[] swiperImages) {
	        try {
	            // Xóa các ảnh Swiper cũ
	            deleteOldSwiperImages();

	            for (MultipartFile file : swiperImages) {
	                var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
	                String imageUrl = (String) uploadResult.get("secure_url");

	                SwiperImage swiperImage = new SwiperImage();
	                swiperImage.setImageUrl(imageUrl);
	                swiperImageRepository.save(swiperImage);
	            }

	            return ResponseEntity.ok(new ObjectRespone(200, "success", "Swiper images uploaded and saved to DB"));
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(new ObjectRespone(500, "error", e.getMessage()));
	        }
	    }

	    // API để upload ảnh Banner
	    @PostMapping("/addBanner") 
	    public ResponseEntity<ObjectRespone> addBanner(@RequestParam("otherImages") MultipartFile[] otherImages) {
	        try {
	            // Xóa các ảnh Banner cũ
	            deleteOldBannerImages();

	            for (MultipartFile file : otherImages) {
	                var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
	                String imageUrl = (String) uploadResult.get("secure_url");

	                OtherImageBanner otherImageBanner = new OtherImageBanner();
	                otherImageBanner.setImageUrl(imageUrl);
	                otherImageRepository.save(otherImageBanner);
	            }

	            return ResponseEntity.ok(new ObjectRespone(200, "success", "Banner images uploaded and saved to DB"));
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(new ObjectRespone(500, "error", e.getMessage()));
	        }
	    }

	    // API lấy ảnh Swiper và Banner
	    @GetMapping("/getBannerSwipper")
	    public ResponseEntity<ObjectRespone> getBannerAndSwipper() {
	        List<SwiperImage> swiperImages = swiperImageRepository.findAll();
	        List<OtherImageBanner> otherImages = otherImageRepository.findAll();
	        Map<String, Object> responseData = new HashMap<>();
	        responseData.put("swiperImages", swiperImages);
	        responseData.put("otherImages", otherImages);

	        return ResponseEntity.ok(new ObjectRespone(200, "success", responseData));
	    }
}
