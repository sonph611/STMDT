package com.API.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.API.DTO.ShopReportRequest;
import com.API.model.Category;
import com.API.model.ProductImage;
import com.API.model.ShopReport;
import com.API.repository.CategoryRepository;
import com.API.service.CategoryAdminService;
import com.API.utils.ObjectRespone;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.jsonwebtoken.io.IOException;

@RestController
@CrossOrigin("*")
public class CategoryAdminController {

	@Autowired
	CategoryAdminService categoryAdminService;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	private Cloudinary cloudinary;

	@GetMapping("/admin/getAllCategories")
	public ResponseEntity<Page<Object[]>> getDanhMucTheoCay(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);

		// Gọi service để lấy danh sách danh mục theo cây
		Page<Object[]> categories = categoryAdminService.getDanhMucTheoCay(pageable);

		return ResponseEntity.ok(categories);
	}

	@GetMapping("/admin/roots")
	public ResponseEntity<?> getRootCategories() {
		List<Map<String, Object>> rootCategories = categoryAdminService.getRootCategories();
		return ResponseEntity.ok(rootCategories);
	}

	// API lấy danh mục con dựa trên id danh mục cha
	@PostMapping("/admin/subcategories")
	public ResponseEntity<?> getSubCategories(@RequestParam Integer parentId) {
		List<Map<String, Object>> subCategories = categoryAdminService.getSubCategories(parentId);
		return ResponseEntity.ok(subCategories);
	}

	@PostMapping("/admin/createCategory")
	public ResponseEntity<String> saveCategory(
	        @RequestParam(value = "files", required = false) MultipartFile[] files,
	        @RequestParam(value = "filesUpdate", defaultValue = "") String imageUpdate,
	        @RequestParam(value = "categoryName", defaultValue = "") String categoryName,
	        @RequestParam(value = "parent_Id", required = false) Integer parentId,
	        @RequestParam(value = "id", required = false) Integer id,
	        @RequestParam(value = "isUpdate", defaultValue = "false") boolean isUpdate) throws IOException {

	    // Tìm parent category (nếu có)
	    Category category = new Category();
	    if (parentId != null) {
	        categoryRepository.findById(parentId).ifPresent(category::setParentCategory);
	    }

	    // Kiểm tra tên trùng
	    Optional<Category> existingCategory = categoryRepository.findByTenLoai(categoryName);
	    if (existingCategory.isPresent() && (!isUpdate || existingCategory.get().getId() != id)) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Tên danh mục đã tồn tại, vui lòng chọn tên khác.");
	    }

	    // Xử lý thêm hoặc cập nhật
	    if (isUpdate) {
	        if (id == null || !categoryRepository.existsById(id)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("ID danh mục không hợp lệ.");
	        }
	        category.setId(id);
	        category.setAnhLoai(files != null ? uploadFiles(files) : imageUpdate);
	    } else {
	        category.setAnhLoai(uploadFiles(files));
	    }

	    category.setTenLoai(categoryName);
	    category.setTrangThai(1);

	    // Lưu vào cơ sở dữ liệu
	    int result = categoryAdminService.addTheLoai(category);
	    String message = isUpdate ? "Category updated successfully with ID: " : "Category created successfully with ID: ";
	    return result > 0 ? ResponseEntity.ok(message + result)
	                      : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Category creation failed.");
	}

	// Hàm xử lý upload file
	private String uploadFiles(MultipartFile[] files) throws IOException, java.io.IOException {
	    List<String> imageUrls = new ArrayList<>();
	    for (MultipartFile file : files) {
	        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
	        imageUrls.add(uploadResult.get("url").toString());
	    }
	    return String.join(",", imageUrls);
	}

    


}
