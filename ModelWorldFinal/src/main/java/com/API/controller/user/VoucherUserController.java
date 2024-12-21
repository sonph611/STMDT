package com.API.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.VoucherSan;
import com.API.model.VoucherSanNguoiDung;
import com.API.model.VoucherShop;
import com.API.model.VoucherShopNguoiDung;
import com.API.repository.VoucherCuaHangguoiDungRepository;
import com.API.repository.VoucherRepository;
import com.API.repository.VoucherSanNguoiDungRepository;
import com.API.repository.VoucherSanRepository;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class VoucherUserController {
	
	@Autowired
	UserFilter u;
	
	@Autowired
	VoucherSanRepository voucherSanRepo;
	
	@Autowired
	VoucherCuaHangguoiDungRepository voucherShopNguoiDung;
	
	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired
	VoucherSanNguoiDungRepository voucherSanNguoiDung;
	// lấy tất cả các voucher của người dùng đã lưu.
	@GetMapping("/user/auth/voucherhistory")
	public ObjectRespone getVoucherHistory(@RequestParam(name="page",defaultValue = "0")Integer page,@RequestParam(name="key",defaultValue = "")String key,@RequestParam(name ="type",defaultValue = "shop")String type) {
		key="%"+key+"%";
		if(type.equals("shop")) {
			return  new ObjectRespone(200,"", voucherRepository.getVoucherHistory(PageRequest.of(page, 10),u.getAccount().getId(),key));
		}else {
			return  new ObjectRespone(200,"", voucherSanRepo.getVoucherHistory(u.getAccount().getId(),PageRequest.of(page, 10),key));
		}
		
	}
	
	@GetMapping("/user/auth/getvouchernotinmyvoucher")
	public ObjectRespone getVoucherNotInMyVoucher(@RequestParam(name="page",defaultValue = "0")Integer page,
			@RequestParam(name="key",defaultValue = "")String key,@RequestParam(name ="type",defaultValue = "shop")String type) {
		key="%"+key+"%";
		if(type.equals("shop")) {
//			System.out.println(voucherRepository.getVoucherNotInMyVoucher(PageRequest.of(page, 10),u.getAccount().getId(),key).getContent().size());
			return  new ObjectRespone(200,"", voucherRepository.getVoucherNotInMyVoucher(PageRequest.of(page, 10),u.getAccount().getId(),key));
		}else {
			return  new ObjectRespone(200,"", voucherSanRepo.getVoucherSanNotInVoucherNguoiDung(u.getAccount().getId(),PageRequest.of(page, 10),key));
		}
		
	}
	
	@PostMapping("/user/auth/layvouchershop")
	public Object layVoucher(@RequestParam(name="voucherId",defaultValue = "-1")Integer voucherId) {
		Integer a=voucherRepository.getVoucherIdById(voucherId).orElse(-1);
		if(a!=-1) {
			if(voucherShopNguoiDung.getIdByVoucherIdAndAccountId(u.getAccount().getId(),voucherId).or(-1)==-1) {
				voucherShopNguoiDung.save(new VoucherShopNguoiDung(0,new VoucherShop(voucherId),u.getAccount()));
				return new ObjectRespone(200,"Lấy thành công voucher",null);
			}
			return new ObjectRespone(200,"Bạn đã nhận mã vouucher này",null);
		}else {
			return new ObjectRespone(400,"Voucher không hợp lệ",null);
		}
	}
	
	@PostMapping("/user/auth/layvouchersan")
	public Object layVoucherSan(@RequestParam(name="voucherId",defaultValue = "-1")Integer voucherId) {
		Integer a=voucherSanRepo.getVoucherIdById(voucherId).orElse(-1);
		if(a!=-1) {
			if(voucherSanRepo.getIdByVoucherIdAndAccountIsssd(u.getAccount().getId(),voucherId).orElse(-1)==-1) {
				voucherSanNguoiDung.save(new VoucherSanNguoiDung(0,new VoucherSan(voucherId),u.getAccount()));
				return new ObjectRespone(200,"Lấy thành công voucher",null);
			}
			return new ObjectRespone(200,"Bạn đã nhận mã vouucher này",null);
		}else {
			return new ObjectRespone(400,"Voucher không hợp lệ",null);
		}
	}
	
//	getVoucherSanNotInVoucherNguoiDung
//	@GetMapping("/user/auth/vouchersanhistory")
//	public ObjectRespone getVoucherHistorySan(@RequestParam(name="page",defaultValue = "0")Integer page,@Param("key")String key) {
//		Page<VoucherShopNguoiDung> vouchers=voucherRepository.getVoucherHistory(PageRequest.of(page, 10),u.getAccount().getId());
//		return new ObjectRespone(200,"", vouchers);
//	}
}
