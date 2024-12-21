package com.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.ViPhamRepository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class ViPhamController {
	@Autowired
	ViPhamRepository viPhamRepository;
	
	@GetMapping("/user/vipham/getall")
	public ObjectRespone getAllViPham() {
		return new ObjectRespone(200,"success", viPhamRepository.findAll());
	}
	
	@GetMapping("/vipham/getall")
	public ObjectRespone getAllViPhamByUser() {
		return new ObjectRespone(200,"success", viPhamRepository.findAll());
	}
}
