package com.API.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.ShopReportRequest;
import com.API.model.ProductReport;
import com.API.model.TaiKhoanReport;
import com.API.model.ViPham;
import com.API.repository.ProductRepository;
import com.API.service.ProductService;

@RestController
@CrossOrigin("*")
public class ProductAdminController {

	@Autowired
	ProductService productService;
	@Autowired
	ProductRepository productRepository;

	@GetMapping("/admin/productsAdmin")
	public ResponseEntity<Page<Object[]>> getProductsWithMaxAndMinPrice(
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false,defaultValue =  "") String name,
	        @RequestParam(required = false) Integer trangThai,
	        @RequestParam(required = false,defaultValue =  "") String shopName,
	        @RequestParam(required = false, defaultValue = "0") Double giaTu,
	        @RequestParam(required = false, defaultValue = "0") Double giaDen) {
		Page<Object[]> products;
		Pageable pageable = PageRequest.of(page,size);

		if(name.isEmpty() && trangThai == null && shopName.isEmpty() && giaTu == 0 && giaDen == 0) {
			products = productService.getProductsWithMaxAndMinPrice(page, size);
		}else {	
		
			products = productRepository.FillterProductsWithMaxAndMinPrice(name, trangThai, shopName,pageable);
		}
		
	
		return ResponseEntity.ok(products);
	}

	@GetMapping("/admin/productDetail/{id}")
	public ResponseEntity<Object[]> getProductDetails(@PathVariable Integer id) {
		Object[] productDetails = productService.getProductDetailsById(id);
		if (productDetails == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(productDetails);
	}

	@GetMapping("/admin/productDetails/{id}")
	public ResponseEntity<Page<Object[]>> getChiTietSanPhamBySanPhamId(@PathVariable Integer id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<Object[]> response = productService.getChiTietSanPhamBySanPhamId(id, page, size);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/admin/colorAndSizeProduct/{id}")
	public ResponseEntity<List<Object[]>> getMauVaKichThuocBySanPhamId(@PathVariable Integer id) {
		List<Object[]> response = productService.findMauVaKichThuocBySanPhamId(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/admin/sale/{id}")
	public ResponseEntity<?> getPromotionByProductId(@PathVariable("id") int productId) {
		List<Map<String, Object>> promotions = productService.getPromotionByProductId(productId);

		if (promotions.isEmpty()) {
			// Trả về thông báo nếu không có khuyến mãi
			return ResponseEntity.ok(Map.of("message", "Sản phẩm không có khuyến mãi"));
		}

		// Trả về danh sách khuyến mãi
		return ResponseEntity.ok(promotions);
	}
	
	@PutMapping("/admin/{id}/updateStatusProduct")
	public ResponseEntity<String> updateTrangThai(@PathVariable("id") Integer id,
			@RequestParam("trangThai") Integer trangThai) {
//		System.out.println(id + trangThai);
		boolean isUpdated = productService.updateTrangThai(id, trangThai);
		if (isUpdated) {
			return ResponseEntity.ok("Trạng thái được cập nhật thành công.");
		} else {
			return ResponseEntity.ok("Cập nhật trạng thái thất bại");
		}
	}
	
	 @GetMapping("/admin/productReport/{loai}")
	    public ResponseEntity<List<ViPham>> getViPhamByLoai(@PathVariable Integer loai) {
	        List<ViPham> viPhams = productService.getViPhamByLoai(loai);
	        return ResponseEntity.ok(viPhams);
	    }
	 
	 @PostMapping("/admin/createReportProduct")
	 public ResponseEntity<Integer> saveTaiKhoanReport(@RequestBody ShopReportRequest request) {
		 ProductReport taiKhoanReport = new ProductReport();
		 taiKhoanReport.setProductId(request.getShopId());
		 taiKhoanReport.setReportId(request.getReportId());
		 taiKhoanReport.setReportedBy(request.getReportedBy());
		 taiKhoanReport.setStatus(request.getStatus());
		 taiKhoanReport.setContent(request.getContent());
		 taiKhoanReport.setImage(request.getImage());
		 System.out.println("id" + request.getReportId());
	     int result = productService.saveProductReport(taiKhoanReport);
	     if (result > 0) {
	         return ResponseEntity.ok(result); 
	     } else {
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
	     }
	 }
}