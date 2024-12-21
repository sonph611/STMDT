package com.API.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.LiveSession;
import com.API.repository.LiveSessionRepository;
import com.API.utils.ObjectRespone;

@RestController("customLiveController")
@CrossOrigin("*")
public class LiveController {
	@Autowired
	private LiveSessionRepository liveReository;
	
	@GetMapping("/user/auth/getalllive")
	public Object getMethodName(@RequestParam(name="id",defaultValue = "-1")Integer id) {
		List<LiveSession> list=liveReository.getAllLive(id);
		return new ObjectRespone(200,"", list);
	}
	@GetMapping("/user/auth/checkalive")
	public Object checkLiveIsAlive(@RequestParam(name = "id",defaultValue = "-1")Integer id) {
		if(liveReository.checkLiveIsAlive(id).or(-1)!=-1) {
			return new ObjectRespone(200,"",null);
		}
		return new ObjectRespone(400,"Phiên live không hợp lệ",null);
	}
	
}
