package com.API.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.DiaChi;
import com.API.repository.DiaChiRepository;
import com.API.service.saler.UserFilter;

@RestController
@CrossOrigin("*")
public class DiaChiController {
	@Autowired
	private DiaChiRepository diaChiRepository;
	
	@Autowired
	private UserFilter userFilter;
	@GetMapping("/user/auth/diachi/getdiachi")
	public List<DiaChi> getDiaChiByUserId(){
		return diaChiRepository.getsDiaChiId(userFilter.getAccount().getId());
	}
}
