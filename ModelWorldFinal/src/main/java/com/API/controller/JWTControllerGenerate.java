package com.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.Account;
import com.API.service.AccountService;
import com.API.service.TokenService;
import com.API.utils.ObjectRespone;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin("*")
public class JWTControllerGenerate {
	@Autowired
	TokenService t;
	
	@Autowired
	AccountService accountService;
	
//	@Autowired
	
	@PostMapping("/user/auth/")
	public Object postMethodName(@RequestBody String token) {
		if(t.validateToken(token)) {
			try {
//				String tt=t.getOrderInfoFromToken(token);
				Integer a=Integer.parseInt(t.getOrderInfoFromToken(token));
				Account account = accountService.getAccountById(a);
				if(account!=null&&account.getTrangThai().equals("HoatDong")) {
					account.setMatKhau(null);
					return new ObjectRespone(200,"",account);
				}
			} catch (Exception e) {
				return new ObjectRespone(400,"Token is not valid", null);
			}
		}
		return new ObjectRespone(400,"Token is not valid ", null);
	}
	
}
