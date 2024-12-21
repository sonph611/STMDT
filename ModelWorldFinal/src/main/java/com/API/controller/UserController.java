package com.API.controller;

import java.util.List;
import java.util.Map;

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
import com.API.model.ShopReport;
import com.API.model.TaiKhoanReport;
import com.API.model.ViPham;
import com.API.repository.AccountRepository;
import com.API.service.UserAdminService;

@RestController
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	UserAdminService userAdminService;
	@Autowired
	AccountRepository accountRepository;

	@GetMapping("/admin/user")
	public ResponseEntity<Page<Object[]>> getUsersWithOrderStats(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "desc") String direction,
			@RequestParam(required = false, defaultValue = "") String trangThai,
			@RequestParam(required = false, defaultValue = "") String sdt
			) {
		Pageable pageable = PageRequest.of(page, size,
				
				direction.equalsIgnoreCase("desc") ? Sort.unsorted() : Sort.unsorted());
		Page<Object[]> users;
		
		if(trangThai.isEmpty() && sdt.isEmpty()) {
			users = userAdminService.getUsersWithOrderStats(pageable);
		}else {
			if(trangThai.isEmpty()) {
				trangThai = null;
			}
			 users = accountRepository.FillterUsersWithOrderStats(trangThai, sdt, pageable);
		}
		
		
		return ResponseEntity.ok(users);
	}

	@GetMapping("/admin/userDetail/{id}")
	public ResponseEntity<Map<String, Object>> getAccountWithAddresses(@PathVariable Integer id) {
		Map<String, Object> accountData = userAdminService.getAccounAndAdress(id);
		return ResponseEntity.ok(accountData);
	}

	@GetMapping("/admin/orderStatistics/{taiKhoanId}")
	public ResponseEntity<Object[]> getOrderStatistics(@PathVariable Integer taiKhoanId) {
		Object[] statistics = userAdminService.getOrderStatistics(taiKhoanId);
		return ResponseEntity.ok(statistics);
	}

	@GetMapping("/admin/orders/{taiKhoanId}")
	public ResponseEntity<Page<Object[]>> getOrders(@PathVariable Integer taiKhoanId, Pageable pageable) {
		Page<Object[]> orderDetails = userAdminService.getOrderDetailsByTaiKhoanId(taiKhoanId, pageable);
		return ResponseEntity.ok(orderDetails);
	}
	
	@PutMapping("/admin/{id}/updateStatusTaiKhoan")
	public ResponseEntity<String> updateTrangThai(@PathVariable("id") Integer id,
			@RequestParam("trangThai") String trangThai) {
		System.out.println(id + trangThai);
		boolean isUpdated = userAdminService.updateTrangThai(id, trangThai);
		if (isUpdated) {
			return ResponseEntity.ok("Trạng thái được cập nhật thành công.");
		} else {
			return ResponseEntity.ok("Cập nhật trạng thái thất bại");
		}
	}
	
	 @GetMapping("/admin/taiKhoanReport/{loai}")
	    public ResponseEntity<List<ViPham>> getViPhamByLoai(@PathVariable Integer loai) {
	        List<ViPham> viPhams = userAdminService.getViPhamByLoai(loai);
	        return ResponseEntity.ok(viPhams);
	    }
	 
	 @PostMapping("/admin/createReportTaiKhoan")
	 public ResponseEntity<Integer> saveTaiKhoanReport(@RequestBody ShopReportRequest request) {
		 TaiKhoanReport taiKhoanReport = new TaiKhoanReport();
		 taiKhoanReport.setTaiKhoanId(request.getShopId());
		 taiKhoanReport.setReportId(request.getReportId());
		 taiKhoanReport.setReportedBy(request.getReportedBy());
		 taiKhoanReport.setStatus(request.getStatus());
		 taiKhoanReport.setContent(request.getContent());
		 taiKhoanReport.setImage(request.getImage());
	     int result = userAdminService.saveTaiKhoanReport(taiKhoanReport);
	     if (result > 0) {
	         return ResponseEntity.ok(result); 
	     } else {
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
	     }
	 }
	
	
//	@GetMapping("admin/user")
//    public ResponseEntity<Page<Object[]>> getUsersWithOrderStats(
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "10") int size,
//        @RequestParam(defaultValue = "desc") String direction
//    		) {
//        Pageable pageable = PageRequest.of(page, size, 
//            direction.equalsIgnoreCase("desc") 
//                ? Sort.unsorted() 
//                : Sort.unsorted());
//
//        Page<Object[]> users = userAdminService.getUsersWithOrderStats(pageable);
//        return ResponseEntity.ok(users);
//    }
//	
//	 @GetMapping("/admin/userDetail/{id}")
//	    public ResponseEntity<Map<String, Object>> getAccountWithAddresses(@PathVariable Integer id) {
//	        Map<String, Object> accountData = userAdminService.getAccounAndAdress(id);
//	        return ResponseEntity.ok(accountData);
//	    }
}
