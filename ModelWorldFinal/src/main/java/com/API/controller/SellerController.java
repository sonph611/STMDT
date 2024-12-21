package com.API.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.API.DTO.shopDTO;
//import com.API.DTO.Admin.ShopReportRequest;
import com.API.model.Order;
import com.API.model.Shop;
import com.API.model.ShopReport;
import com.API.model.ViPham;
import com.API.repository.DonHangRepository;
import com.API.repository.ShopRepository;
import com.API.service.SellerService;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class SellerController {

	@Autowired
	SellerService sellerService;
	@Autowired
	ShopRepository shopRepository;

	@GetMapping("/admin/seller/getAllSeller")
	public Page<Object[]> getAllShops(
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "1") int size,
	        @RequestParam(required = false) Integer trangThai,
	        @RequestParam(required = false, defaultValue = "") String name) {

	    Pageable pageable = PageRequest.of(page, size);

	    if (trangThai != null || !name.isEmpty()) {
	        return shopRepository.getAdminShopFillter(trangThai, name, pageable);
	    } else {
	        return sellerService.getShopsWithPagination(page, size);
	    }
	}


	@GetMapping("/admin/sellerDetail/{shopId}")
	public ResponseEntity<Object[]> getShopDetails(@PathVariable Integer shopId) {
		Optional<Object[]> shopDetails = sellerService.getShopDetailsById(shopId);
		return shopDetails.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/admin/sellerOder/{shopId}")
	public ResponseEntity<Page<Object[]>> getOrdersByShopId(@PathVariable("shopId") Integer shopId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int size) {
		Page<Object[]> ordersPage = sellerService.getOrdersByShopIdWithPagination(shopId, page, size);
		return ResponseEntity.ok(ordersPage);
	}

	@GetMapping("/admin/seller/statistics/{shopId}")
	public ResponseEntity<Map<String, Object>> getShopStatistics(@PathVariable("shopId") Integer id) {

		Map<String, Object> statistics = sellerService.getShopStatistics(id);

		if (statistics.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(statistics);
	}

	@GetMapping("/admin/sellerProduct/{shopId}")
	public ResponseEntity<Page<Object[]>> getProductByShopId(@PathVariable("shopId") Integer shopId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		Page<Object[]> productPage = sellerService.findAllProductByShopId(shopId, page, size);
		return ResponseEntity.ok(productPage);
	}

	@PutMapping("/admin/{id}/updateStatus")
	public ResponseEntity<String> updateTrangThai(@PathVariable("id") Integer id,
			@RequestParam("trangThai") Integer trangThai) {
		boolean isUpdated = sellerService.updateTrangThai(id, trangThai);
		if (isUpdated) {
			return ResponseEntity.ok("Trạng thái được cập nhật thành công.");
		} else {
			return ResponseEntity.ok("Cập nhật trạng thái thất bại");
		}
	}
	
	 @GetMapping("/admin/sellerReport/{loai}")
	    public ResponseEntity<List<ViPham>> getViPhamByLoai(@PathVariable Integer loai) {
	        List<ViPham> viPhams = sellerService.getViPhamByLoai(loai);
	        return ResponseEntity.ok(viPhams);
	    }
	 
	 @PostMapping("/admin/createReport")
	 public ResponseEntity<Integer> saveShopReport(@RequestBody ShopReportRequest request) {
		 ShopReport shopReport = new ShopReport();
	        shopReport.setShopId(request.getShopId());
	        shopReport.setReportId(request.getReportId());
	        shopReport.setReportedBy(request.getReportedBy());
	        shopReport.setStatus(request.getStatus());
	        shopReport.setContent(request.getContent());
	        shopReport.setImage(request.getImage());
	     int result = sellerService.saveShopReport(shopReport);
	     if (result > 0) {
	         return ResponseEntity.ok(result); 
	     } else {
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
	     }
	 }
}
