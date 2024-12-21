package com.API.controller;

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

import com.API.DTO.voucher.VoucherSanAdminDTO;
import com.API.model.VoucherSan;
import com.API.repository.VoucherSanRepository;
import com.API.service.VoucherSanAdminService;

@RestController
@CrossOrigin("*")
public class VoucherSanController {
	@Autowired
	VoucherSanAdminService voucherSanAdminService;
	@Autowired
	VoucherSanRepository voucherSanRepository;

	@GetMapping("/admin/voucherSanAdmin")
	public ResponseEntity<Page<VoucherSan>> getVouchers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<VoucherSan> voucherSanPage = voucherSanAdminService.getVouchers(pageable);
		return ResponseEntity.ok(voucherSanPage);
	}

	@GetMapping("/admin/voucherSanAdminDetail/{id}")
	public ResponseEntity<Optional<VoucherSan>> getVouchers(@PathVariable("id") Integer id) {
		Optional<VoucherSan> voucherSanDetail = voucherSanAdminService.finById(id);
		return ResponseEntity.ok(voucherSanDetail);
	}

	@PostMapping("/admin/saveOrUpdate/{id}")
	public ResponseEntity<Object> saveOrUpdateVoucher(
	        @PathVariable(value = "id", required = false) Integer id,
	        @RequestBody VoucherSanAdminDTO voucherDTO) {
	    try {
	        // Lưu hoặc cập nhật voucher
	        VoucherSan savedVoucher = voucherSanAdminService.saveOrUpdateVoucher(id, voucherDTO);
	        return ResponseEntity.ok(savedVoucher); // Trả về voucher đã lưu hoặc cập nhật
	    } catch (IllegalArgumentException ex) {
	        // Nếu xảy ra lỗi (ví dụ: thông tin không hợp lệ), trả về thông báo lỗi
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	    } catch (Exception ex) {
	        // Nếu có lỗi không mong muốn, trả về thông báo lỗi chung
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi xử lý yêu cầu.");
	    }
	}
	
	@PostMapping("/admin/updateStatusCategory/{id}")
	public ResponseEntity<String> updateVoucherStatus(
	        @PathVariable Integer id,
	        @RequestBody Map<String, Integer> statusRequest) {

	    try {
	        // Kiểm tra và cập nhật trạng thái voucher
	        int status = statusRequest.get("status");
	        boolean isUpdated = voucherSanAdminService.updateVoucherStatus(id, status);

	        if (isUpdated) {
	            return ResponseEntity.ok("Trạng thái voucher đã được cập nhật thành công!");
	        } else {
	            return ResponseEntity.status(404).body("Voucher không tìm thấy!");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Lỗi khi cập nhật trạng thái voucher");
	    }
	}


}
