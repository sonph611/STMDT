package com.API.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.VoucherSanNguoiDung;
import com.API.model.VoucherShopNguoiDung;
import com.API.repository.VoucherCuaHangguoiDungRepository;
import com.API.repository.VoucherRepository;
import com.API.repository.VoucherSanNguoiDungRepository;
import com.API.repository.VoucherSanRepository;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class UserVoucherController {
	
	@Autowired
	VoucherRepository v;
	
	@Autowired
	VoucherCuaHangguoiDungRepository vv;
	
	
	@Autowired
	VoucherSanRepository vs;
	
	@Autowired
	VoucherSanNguoiDungRepository vn;
	
	
	@Autowired
	UserFilter u;
	
	@PostMapping("/user/auth/voucher/getvoucher/{voucherId}")
	public Object getVoucher(@PathVariable("voucherId")Integer id) {
//		System.out.println("Xin chào bận h");
		Integer ids=v.getVoucherForUser(id,u.getAccount().getId()).orElse(-1);
		if(ids!=-1) {
			vv.save(new VoucherShopNguoiDung(id,u.getAccount()));
			return new ObjectRespone(200,"Thêm thành công", null);
		}
		return new ObjectRespone(400,"Không thể lấy voucher, vui lòng kiểm tra lại voucher", null);
	}
	
	
	
	@PostMapping("/user/auth/voucher/getvouchersan/{voucherId}")
	public Object layVoucherSan(@PathVariable("voucherId")Integer id) {
		Integer ids=vs.getVoucher(id,u.getAccount().getId()).orElse(-1);
		if(ids!=-1) {
			vn.save(new VoucherSanNguoiDung(id, u.getAccount()));
			return new ObjectRespone(200,"Thêm thành công", null);
		}
		return new ObjectRespone(400,"Không thể lấy voucher, vui lòng kiểm tra lại voucher", null);
	}
	
}
