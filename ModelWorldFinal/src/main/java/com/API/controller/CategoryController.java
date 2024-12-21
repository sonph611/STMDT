package com.API.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.ViewedCategoryRequest;
import com.API.model.ViewedCategory;
import com.API.repository.CategoryRepository;
import com.API.repository.ViewedCategoryRepository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class CategoryController {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ViewedCategoryRepository viewedCategoryRepository;
	
	@GetMapping(value="/category/getall")
	public ResponseEntity<ObjectRespone> getAllAccount() {
		return ResponseEntity.ok(new ObjectRespone(200,"success",categoryRepository.findAllParents()));
	}
	
	@PostMapping(value = "/viewedcategories/add")
	public ResponseEntity<ObjectRespone> addViewedcategories(
	        @RequestParam("userId") int userId,
	        @RequestParam("categoryId") int categoryId,
	        @RequestParam("productId") int productId) {
	    try {
	        // Kiểm tra số lượng bản ghi của user trong bảng viewedcategories
	        long count = viewedCategoryRepository.countByUserId(userId);
	        
	        // Nếu số lượng bản ghi đã vượt quá 5, xóa bản ghi cũ nhất
	        if (count >= 5) {
	            // Tìm bản ghi cũ nhất và xóa
	            ViewedCategory oldestViewedCategory = viewedCategoryRepository.findTopByUserIdOrderByViewedAtAsc(userId);
	            viewedCategoryRepository.delete(oldestViewedCategory);
	        }

	        // Tạo đối tượng ViewedCategory và lưu vào database
	        ViewedCategory viewedCategory = new ViewedCategory();
	        viewedCategory.setUserId(userId);
	        viewedCategory.setCategoryId(categoryId);
	        viewedCategory.setProductId(productId);
	        viewedCategory.setViewedAt(new Timestamp(System.currentTimeMillis()));

	        // Lưu vào database
	        viewedCategoryRepository.save(viewedCategory);

	        return ResponseEntity.ok(new ObjectRespone(200, "Success", null));
	    } catch (DataIntegrityViolationException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ObjectRespone(400, "Lỗi khóa ngoại: " + e.getRootCause().getMessage(), null));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ObjectRespone(500, "Error: " + e.getMessage(), null));
	    }
	}

	
}
