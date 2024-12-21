package com.API.service;

import java.util.List;

public interface  DashboardService {
		public Double getRevenueToday();
		public Double getTodayRevenuePercentage();
		public Long countCompletedOrdersToday();
		public Long countActiveAccounts();
		public Long countActiveSeller();
		public Long countActiveProducts();
		public List<Object[]> calculateRevenueAndProfit(String filterType, String startDate, String endDate);
		public Object[] getOrderStatus(String filterType, String startDate, String endDate);
}
