package com.API.controller.saler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.Shop;
import com.API.repository.ShopRepository;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class AuthSeller {
	@Autowired
	UserFilter userFilter;
	@Autowired
	ShopRepository shopRepository;
	
	@GetMapping("/user/auth/checkseller")
	public Object getMethodName() {
		Optional<Shop> shop=shopRepository.getInfoShopByAccountId(userFilter.getAccount().getId());
		if(shop.isPresent()) {
			Shop a=shop.get();
			if(a.getTrangThai()==1) {
				return new ObjectRespone(200,"fail",a);
			}else {
				return new ObjectRespone(400,"Shop đã bị ngưng hoạt động",null);
			}
		}else {
			return new ObjectRespone(400,"Shop không tồn tại",null);
		}
	}
	
	@GetMapping("/getshopinfo")
	public Object getShopInfo(@RequestParam("id")Integer id) {
		Shop a= shopRepository.findById(60).orElse(null);
		a.setAnhDangCam(null);
		a.setAccount(null);
		a.setDiaChi(null);
		a.setHinhChupThe(null);
		a.setLoaiHinhKinhDoanh(null);
		a.setGiayPhepKinhDoanh(null);
		return a;
	}
}
