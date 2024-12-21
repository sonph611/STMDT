package com.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.AccountRepository;
import com.API.repository.KichThuocRespository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class KichThuocController {
	@Autowired
	private KichThuocRespository kichThuocRespository;
	@GetMapping(value="/kichthuoc/getall")
	@ResponseBody
	public ResponseEntity<ObjectRespone> getAllAccount() {
		return ResponseEntity.ok(new ObjectRespone(200,"success",kichThuocRespository.findAll()));
	}
}
