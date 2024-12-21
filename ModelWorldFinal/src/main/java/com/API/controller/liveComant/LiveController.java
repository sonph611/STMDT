//package com.API.controller.liveComant;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.API.repository.LiveDetail;
//import com.API.utils.ObjectRespone;
//@RestController
//@CrossOrigin("*")
//public class LiveController {
//	
//	@Autowired
//	LiveDetail ld;
//	
//	@GetMapping("user/auth/getalllive")
//	public ObjectRespone getProductInLive(@RequestParam("id")Integer id) {
//		return new ObjectRespone(200,"",ld.findByLiveId(id));
//	}
//}
//
//
