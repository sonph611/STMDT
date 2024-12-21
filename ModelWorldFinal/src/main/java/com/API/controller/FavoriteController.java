package com.API.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.API.repository.ProductRepository;
import com.API.utils.ObjectRespone;

@Controller
@CrossOrigin("*")
public class FavoriteController {
	@Autowired
	ProductRepository productRepository;
	@GetMapping("/home/get50FavoriteProduct")
	public ResponseEntity<ObjectRespone> get50FavoriteProduct(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "48") int size) {

	    List<Object[]> listProduct = productRepository.get50FavoriteProduct();
	   
	    Map<String, Object> responseData = new HashMap<>();
	    responseData.put("listProduct", listProduct);
	    return ResponseEntity.ok(new ObjectRespone(200, "success", responseData));
	}
}
