package com.API.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.utils.ObjectRespone;

@RestController

public class ControllerHandlerAuth {
	@PostMapping("/error")
	public ResponseEntity<ObjectRespone> g() {
	    ObjectRespone response = new ObjectRespone(401, null, null);
	    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/error")
	public ResponseEntity<ObjectRespone> gs() {
	    ObjectRespone response = new ObjectRespone(401, null, null);
	    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
}
