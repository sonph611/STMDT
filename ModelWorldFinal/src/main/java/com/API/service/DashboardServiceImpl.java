//package com.API.service;
//
//package com.API.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.API.repository.DonHangRepository;
//
//@Service
//public class DashboardServicelmpl implements DashboardService {
//	
//	@Autowired
//	DonHangRepository donHangRepository;
//
//	@Override
//	public Double getRevenueToday() {
//		double revenue = 0;
//		try {
//			revenue = donHangRepository.getTodayRevenue();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return revenue;
//	}
//
//	@Override
//	public Double getTodayRevenuePercentage() {
//		double revenuePercentage = 0;
//		try {
//			revenuePercentage = donHangRepository.getTodayRevenuePercentage();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return revenuePercentage;
//	}
//
//	@Override
//	public Long countCompletedOrdersToday() {
//		long CompletedOrdersToday = 0;
//		try {
//			CompletedOrdersToday = donHangRepository.countCompletedOrdersToday();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return CompletedOrdersToday;
//	}
//
//	@Override
//	public Long countActiveAccounts() {
//		long activeAccounts = 0;
//		try {
//			activeAccounts = donHangRepository.countActiveAccounts();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return activeAccounts;
//	}
//
//	@Override
//	public Long countActiveSeller() {
//		long activeSeller = 0;
//		try {
//			activeSeller = donHangRepository.countActiveSeller();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return activeSeller;
//	}
//
//	@Override
//	public Long countActiveProducts() {
//		long activeProducts = 0;
//		try {
//			activeProducts = donHangRepository.countActiveProducts();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return activeProducts;
//	}
//	
//	
//
//}
