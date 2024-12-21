package com.API.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.DonHangRepository;
import com.API.service.OdersServie;

@RestController
@CrossOrigin("*")
public class OdersController {

	@Autowired
	OdersServie odersServie;
	@Autowired
	DonHangRepository donHangRepository;

	@GetMapping("/admin/getAllOder")
	public ResponseEntity<Page<Object[]>> getOrdersByShopId(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			@RequestParam(required = false,defaultValue =  "") String id,               
	        @RequestParam(required = false) Integer trangThai,      
	        @RequestParam(required = false,defaultValue =  "") String shopName,         
	        @RequestParam(required = false,defaultValue =  "") String name              
			) {
		Pageable pageable = PageRequest.of(page,size);
		Page<Object[]> ordersPage;
		if(id.isEmpty() && trangThai == null && shopName.isEmpty() && name.isEmpty()) {
			ordersPage = odersServie.getAllOders(page, size);
		}else{
			ordersPage = donHangRepository.FillterAllOrders(id, trangThai, shopName, name,pageable);
		}
		
	
		return ResponseEntity.ok(ordersPage);
	}
	
	 @GetMapping("admin/invoices/{orderId}")
	    public ResponseEntity<?> getOrderDetails(@PathVariable Integer orderId) {
	        Optional<Object[]> orderDetails = odersServie.getOrderDetailsById(orderId);
	        if (orderDetails.isPresent()) {
	            return ResponseEntity.ok(orderDetails.get());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
	        }
	    }
	 
	 @GetMapping("/admin/productOrderDetail/{orderId}")
	    public ResponseEntity<List<Object[]>> getChiTietDonHangByDonHangId(
	    		@PathVariable Integer orderId) {
	        List<Object[]> result = odersServie.getChiTietDonHangByDonHangId(orderId);
	        return ResponseEntity.ok(result);
	    }

}
