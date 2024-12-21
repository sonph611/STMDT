package com.API.controller.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.ReportOrder;
import com.API.repository.DonHangRepository;
import com.API.repository.OrdeReportRepository;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;


@RestController
@CrossOrigin("*")
public class ReportController {
	@Autowired
	UserFilter userFilter;
	@Autowired
	OrdeReportRepository orderReportRepository;
	
	@Autowired
	DonHangRepository o;
	
	@PostMapping("/user/auth/addreport")
	public Object addReport(@RequestBody ReportOrder reportOrder) {
		if(o.getIdOrderById(reportOrder.getOrder().getId(),userFilter.getAccount().getId()).orElse(null)!=null) {
			reportOrder.setAccount(userFilter.getAccount());
			reportOrder.setId(null);
			reportOrder.setNgayReport(new Date());
			orderReportRepository.save(reportOrder);
			return new ObjectRespone(200,"success",null);
		}
		return new ObjectRespone(400,"fail",null);
	}
}
