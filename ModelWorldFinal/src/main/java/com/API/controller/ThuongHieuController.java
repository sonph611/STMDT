package com.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.MauSacRepository;
import com.API.repository.ThuongHieuRepository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class ThuongHieuController {
	@Autowired
	private ThuongHieuRepository thuongHieuRepository;
	@GetMapping(value="/thuonghieu/getall")
	@ResponseBody
	public ResponseEntity<ObjectRespone> getAllAccount() {
		return ResponseEntity.ok(new ObjectRespone(200,"success",thuongHieuRepository.findAll()));
	}
}
