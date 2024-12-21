package com.API.controller.user;

import org.springframework.web.bind.annotation.RestController;

import com.API.repository.ThongBaoRepository;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin("*")
public class ThongBaoController {
	
	@Autowired
	ThongBaoRepository thongBaoRepository;
	@Autowired
	UserFilter userFilter;
	
	
	@GetMapping("/user/auth/gettrangthaithongbao")
	public Object getThongBaoHeader() {
		Map<String,Object>map=new HashMap<String, Object>();
		map.put("data",thongBaoRepository.getThongBaoHeader(PageRequest.of(0, 5),userFilter.getAccount().getId()));
		map.put("countNotReaded",thongBaoRepository.countNotReadedByAccountId(userFilter.getAccount().getId()));
		return map;
	}
	
	
	@GetMapping("/user/auth/getthongbao")
	public Object getMethodName(@RequestParam("type")String type,@RequestParam(name = "page",defaultValue = "0")Integer page) {
		System.out.println(thongBaoRepository.getThongBaoByType(type,PageRequest.of(page, 10),userFilter.getAccount().getId()).hasContent());
		return thongBaoRepository.getThongBaoByType(type,PageRequest.of(page, 10),userFilter.getAccount().getId());
	}
	
	@PostMapping("/user/auth/pinreaded")
	public Object postMethodName(@RequestParam("id")Integer id) {
		Integer a=thongBaoRepository.updateReadedByIdAnUserId(id,userFilter.getAccount().getId());
		if(a==1) {
			return new ObjectRespone(200,"",null);
		}else {
			return new ObjectRespone(400,"Thông báo cập nhật không hợp lệ",null);
		}
	}
	
	@PostMapping("/user/auth/pinreadedAll")
	public Object postMethodName(@RequestBody List<Integer>ids) {
		Integer a=thongBaoRepository.updateReadedByIdAnUserInIds(ids,userFilter.getAccount().getId());
		if(a==ids.size()) {
			return new ObjectRespone(200,"",null);
		}else {
			return new ObjectRespone(400,"Thông báo cập nhật không hợp lệ",null);
		}
	}
	
	
}
