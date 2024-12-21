package com.API.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.API.repository.DonHangRepository;

@Service
public class DashboardServicelmpl implements DashboardService {
	
	@Autowired
	DonHangRepository donHangRepository;

	@Override
	public Double getRevenueToday() {
		double revenue = 0;
		try {
			revenue = donHangRepository.getTodayRevenue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return revenue;
	}

	@Override
	public Double getTodayRevenuePercentage() {
		double revenuePercentage = 0;
		try {
			revenuePercentage = donHangRepository.getTodayRevenuePercentage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return revenuePercentage;
	}

	@Override
	public Long countCompletedOrdersToday() {
		long CompletedOrdersToday = 0;
		try {
			CompletedOrdersToday = donHangRepository.countCompletedOrdersToday();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CompletedOrdersToday;
	}

	@Override
	public Long countActiveAccounts() {
		long activeAccounts = 0;
		try {
			activeAccounts = donHangRepository.countActiveAccounts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activeAccounts;
	}

	@Override
	public Long countActiveSeller() {
		long activeSeller = 0;
		try {
			activeSeller = donHangRepository.countActiveSeller();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activeSeller;
	}

	@Override
	public Long countActiveProducts() {
		long activeProducts = 0;
		try {
			activeProducts = donHangRepository.countActiveProducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activeProducts;
	}

	@Override
	public List<Object[]> calculateRevenueAndProfit(String filterType, String startDate, String endDate) {
		 // Nếu filterType không có giá trị hoặc rỗng, mặc định sẽ là 'day'
        if (filterType == null || filterType.isEmpty()) {
            filterType = "day"; // Mặc định lọc theo ngày
        }
        
        if (startDate == null || startDate.isEmpty()) {
            // Đặt startDate thành NULL nếu không có giá trị
            startDate = null;
        }
        if (endDate == null || endDate.isEmpty()) {
            // Đặt endDate thành NULL nếu không có giá trị
            endDate = null;
        }


        // Kiểm tra nếu filterType là 'custom', phải truyền vào startDate và endDate
        if ("custom".equalsIgnoreCase(filterType)) {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("startDate and endDate must be provided for custom filterType.");
            }
        }

        // Gọi repository để thực hiện truy vấn
        return donHangRepository.calculateRevenueAndProfit(filterType, startDate, endDate);
	    
	}

	@Override
	public Object[] getOrderStatus(String filterType, String startDate, String endDate) {
		 if (filterType == null || filterType.isEmpty()) {
	            filterType = "day"; // Mặc định lọc theo ngày
	        }
	        
	        if (startDate == null || startDate.isEmpty()) {
	            // Đặt startDate thành NULL nếu không có giá trị
	            startDate = null;
	        }
	        if (endDate == null || endDate.isEmpty()) {
	            // Đặt endDate thành NULL nếu không có giá trị
	            endDate = null;
	        }


	        // Kiểm tra nếu filterType là 'custom', phải truyền vào startDate và endDate
	        if ("custom".equalsIgnoreCase(filterType)) {
	            if (startDate == null || endDate == null) {
	                throw new IllegalArgumentException("startDate and endDate must be provided for custom filterType.");
	            }
	        }
		 return donHangRepository.countOrderStatus(filterType, startDate, endDate);
	}

	
	

	

}
