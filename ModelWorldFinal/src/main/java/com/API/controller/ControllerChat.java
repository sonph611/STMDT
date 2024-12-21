package com.API.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.Account;
import com.API.model.Shop;
import com.API.repository.AccountRepository;
import com.API.repository.CategoryRepository;
import com.API.repository.KichThuocRespository;
import com.API.repository.MauSacRepository;
import com.API.repository.ShopRepository;
import com.API.repository.ThuongHieuRepository;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")

public class ControllerChat {
	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	UserFilter userFilter;
	
	
	@GetMapping("/user/auth/checkseller2")
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

	@GetMapping("/getshopinfo1")
	public Object getUserInfo(@RequestParam("id") Integer id) {
	    
	    Shop shop = shopRepository.findById(id).orElse(null);

	    if (shop == null) {
	        return new ObjectRespone(404, "Shop not found", null);
	    }


	    return shop;
	}
	@GetMapping("/getaccountinfo")
		public Object getShopInfo(@RequestParam("id") Integer id) {
	    
	    Account account = accountRepository.findById(id).orElse(null);

	    	    return account;
	}

	
	 
}


