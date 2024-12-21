package com.API.controller.saler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.DonHangRepository;
import com.API.repository.ProductRepository;
import com.API.service.saler.ShopFilter;

@RestController
@CrossOrigin("*")
public class Report {
	
	@Autowired
	DonHangRepository d;
	@Autowired
	ProductRepository p;
	
	@Autowired
	ShopFilter s;
	
	@GetMapping("/sale/report")
	public Map<String,Object> getReport(){
		Map<String, Object> m=new HashMap();
		m.put("OrderSoLuong", d.getCountOrderByStatus(s.getShopId()).stream()
				.filter(array -> array.length >= 2).collect(Collectors.toMap(array -> array[0], array -> array[1])));
		m.put("sanPhamHetHang",p.getReportSaleProduct(s.getShopId()));
		m.put("month",d.getCountOrderByMonth(s.getShopId()));
		m.put("productTopSale", p.getProductTopSale(s.getShopId()));
		return m;
	}
	
	@GetMapping("/sale/reportorder")
	public Object getReportDonHang(@RequestParam("year")Integer year){
		if(year==null) {
			year=new Date().getYear();
		}
		return d.getReportOrder(s.getShopId(), year);
	}
	
}
//SELECT COUNT(*)
//FROM (
//    SELECT d.sanPhamId
//    FROM chitietsanpham d
//    GROUP BY d.sanPhamId
//    HAVING SUM(d.soLuong) < 10000000000000
//) AS subquery;