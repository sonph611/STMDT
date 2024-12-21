package com.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.AccountRepository;
import com.API.repository.MauSacRepository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class MauSacController {
	@Autowired
	private MauSacRepository mauSacRepository;
	@GetMapping(value="/mausac/getall",produces = "application/json")
	@ResponseBody
	public ResponseEntity<ObjectRespone> getAllAccount() {
		return ResponseEntity.ok(new ObjectRespone(200,"success",mauSacRepository.findAll()));
	}
}
