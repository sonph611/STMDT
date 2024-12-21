package com.API.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;


//import com.API.DTO.shopDTO;
//import com.API.DTO.Admin.ShopReportRequest;
import com.API.model.Shop;
import com.API.model.ShopReport;
import com.API.model.ViPham;

public interface SellerService {
	public  Page<Shop> findAllSeller(int page, int size);
	
	public Page<Object[]> getShopsWithPagination(int page, int size);
	public Optional<Object[]> getShopDetailsById(Integer shopId);
	public Page<Object[]> getOrdersByShopIdWithPagination(Integer shopId, int page, int size);
	public Map<String, Object> getShopStatistics(Integer id);
	public Page<Object[]> findAllProductByShopId(Integer id, int page, int size);
	public boolean updateTrangThai(Integer id, Integer trangThai);
	public List<ViPham> getViPhamByLoai(Integer loai);
	public int saveShopReport(ShopReport shopReport);
}
