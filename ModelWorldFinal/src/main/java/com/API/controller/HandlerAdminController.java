package com.API.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.API.utils.ObjectRespone;

@RestController
public class HandlerAdminController {

	@RequestMapping(value = "/emtpyAccount", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<Object> emtpyAccount() {
		System.out.println("có mà??");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // Trả về mã 401
				.body(new ObjectRespone(401, "Unauthorized access", null));
	}

	@RequestMapping(value = "/authoritiesAdmin", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<Object> authoritiesAdmin() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND) // Trả về mã 404
				.body(new ObjectRespone(404, "Resource not found", null));
	}

//	@PostMapping("/emtpyAccount")
//	public Object postEmtpyAccount() {
//		return new ObjectRespone(401,null,null);
//	}

//	@PostMapping("/authoritiesAdmin")
//	public Object postAuthoritiesAdmin() {
//		return new ObjectRespone(404,null,null);
//	}

}
