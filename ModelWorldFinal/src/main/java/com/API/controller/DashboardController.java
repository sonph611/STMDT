package com.API.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.ProductRepository;
import com.API.repository.ShopRepository;
import com.API.service.DashboardService;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class DashboardController {

	@Autowired
	DashboardService dashboardService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ShopRepository shopRepository;

	@GetMapping("/admin/dashboard/getRevenueToday")
	public ResponseEntity<ObjectRespone> getRevenueToday() {

		return ResponseEntity.ok(new ObjectRespone(200, "success", dashboardService.getRevenueToday()));
	}

	@GetMapping("/admin/dashboard/getTodayRevenuePercentage")
	public ResponseEntity<ObjectRespone> getTodayRevenuePercentage() {

		return ResponseEntity.ok(new ObjectRespone(200, "success", dashboardService.getTodayRevenuePercentage()));
	}

	@GetMapping("/admin/dashboard/countCompletedOrdersToday")
	public ResponseEntity<ObjectRespone> countCompletedOrdersToday() {

		return ResponseEntity.ok(new ObjectRespone(200, "success", dashboardService.countCompletedOrdersToday()));
	}

	@GetMapping("/admin/dashboard/countActiveAccounts")
	public ResponseEntity<ObjectRespone> countActiveAccounts() {

		return ResponseEntity.ok(new ObjectRespone(200, "success", dashboardService.countActiveAccounts()));
	}

	@GetMapping("/admin/dashboard/countActiveSeller")
	public ResponseEntity<ObjectRespone> countActiveSeller() {

		return ResponseEntity.ok(new ObjectRespone(200, "success", dashboardService.countActiveSeller()));
	}
	
	@GetMapping("/admin/dashboard/activeProducts")
	public ResponseEntity<ObjectRespone> activeProducts() {

		return ResponseEntity.ok(new ObjectRespone(200, "success", dashboardService.countActiveProducts()));
	}
	
	@GetMapping("/admin/revenueReport")
	public ResponseEntity<List<Object[]>> getRevenueAndProfit(
			 @RequestParam(value = "filterType", defaultValue = "day") String filterType,
	            @RequestParam(value = "startDate", required = false) String startDate,
	            @RequestParam(value = "endDate", required = false) String endDate) {
	        try {
	            // Kiểm tra nếu filterType là 'custom' thì bắt buộc có startDate và endDate
	            if ("custom".equalsIgnoreCase(filterType) && (startDate == null || endDate == null)) {
	                return ResponseEntity.badRequest().body(null);
	            }

	            List<Object[]> results = dashboardService.calculateRevenueAndProfit(filterType, startDate, endDate);
	            return ResponseEntity.ok(results);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	
	@GetMapping("/admin/orderTotal")
	public Object[] getOrderStatus(
			 @RequestParam(value = "filterType", defaultValue = "day") String filterType,
	            @RequestParam(value = "startDate", required = false) String startDate,
	            @RequestParam(value = "endDate", required = false) String endDate) {  // Chỉ dùng cho 'custom'
        
        return dashboardService.getOrderStatus(filterType, startDate, endDate);
    }
	
	@GetMapping("admin/getTopReport")
    public ResponseEntity<?> getTopShop(
            @RequestParam(value = "filterType", defaultValue = "DAY") String filterType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "48") int size) {

       	
	 	Pageable pageable = PageRequest.of(page, Math.min(size, 30));
        Page<Object[]> result = shopRepository.getTop30ShopBestSell(filterType.toUpperCase(), pageable);

       
        return ResponseEntity.ok(result);
    }
 
 @GetMapping("admin/getTopReportProduct")
    public ResponseEntity<?> getTopProduct(
            @RequestParam(value = "filterType", defaultValue = "DAY") String filterType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "48") int size) {

	 	Pageable pageable = PageRequest.of(page, Math.min(size, 30));
        Page<Object[]> result = productRepository.getTop30ProductBestSell(filterType.toUpperCase(), pageable);

       
        return ResponseEntity.ok(result);
    }
}
