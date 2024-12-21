package com.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.ProductDetailRepository;
import com.API.repository.ShopRepository;
import com.API.utils.ObjectRespone;

@RestController
public class ProductDetailController {
	@Autowired
	private ProductDetailRepository productDetailRepository;
	@GetMapping(value="/productdetail/getall")
	public ResponseEntity<ObjectRespone> getAllAccount() {
		return ResponseEntity.ok(new ObjectRespone(200,"success",productDetailRepository.findAll()));
	}
}
