package com.API.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.API.DTO.shopDTO;
import com.API.model.Order;
import com.API.model.Shop;
import com.API.model.ShopReport;
import com.API.model.ViPham;
import com.API.repository.DonHangRepository;
import com.API.repository.ProductRepository;
import com.API.repository.ShopReportRepository;
import com.API.repository.ShopRepository;
import com.API.repository.ViPhamRepository;

import jakarta.persistence.Tuple;

@Service
public class SellerServicelmpl implements SellerService {

	@Autowired
	ShopRepository shopRepository;
	@Autowired
	DonHangRepository donHangRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ViPhamRepository viPhamRepository;
	@Autowired
	ShopReportRepository shopRepoertRepository;

	@Override
	public Page<Shop> findAllSeller(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("ngayDangKy")));

		return shopRepository.findAll(pageable);
	}

	@Override
	public Page<Object[]> getShopsWithPagination(int page, int size) {
		List<Object[]> allShops = shopRepository.findAllShops();
		Pageable pageable = PageRequest.of(page, size);
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), allShops.size());

		return new PageImpl<>(allShops.subList(start, end), pageable, allShops.size());
	}

	@Override
	public Optional<Object[]> getShopDetailsById(Integer shopId) {
		return shopRepository.findShopDetailsById(shopId);
	}

	@Override
	public Page<Object[]> getOrdersByShopIdWithPagination(Integer shopId, int page, int size) {
		List<Object[]> allOrders = donHangRepository.findDonHangsByShopId(shopId);
		Pageable pageable = PageRequest.of(page, size);
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), allOrders.size());
		return new PageImpl<>(allOrders.subList(start, end), pageable, allOrders.size());

	}

	@Override
	public Map<String, Object> getShopStatistics(Integer id) {
		List<Object[]> results = shopRepository.findStatisticsByShopIdAndCuaHangId(id);

		if (results.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, Object> statistics = new HashMap<>();
		for (Object[] row : results) {
			statistics.put("tongSoDonHang", row[0]);
			statistics.put("tongDoanhThu", row[1]);
			statistics.put("tongSoSanPham", row[2]);
		}

		return statistics;
	}

	@Override
	public Page<Object[]> findAllProductByShopId(Integer id, int page, int size) {
		List<Object[]> allOrders = productRepository.findAllByCuHangOrderByIdDesc(id);
		Pageable pageable = PageRequest.of(page, size);
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), allOrders.size());
		return new PageImpl<>(allOrders.subList(start, end), pageable, allOrders.size());
	}

	@Override
	public boolean updateTrangThai(Integer id, Integer trangThai) {
		int rowsUpdated = shopRepository.updateTrangThaiById(id, trangThai);
		return rowsUpdated > 0;
	}

	@Override
	public List<ViPham> getViPhamByLoai(Integer loai) {
		return viPhamRepository.findByLoai(loai);
	}

	@Override
	public int saveShopReport(ShopReport shopReport) {
		shopReport.setCreateAt(java.time.LocalDateTime.now());
		ShopReport savedReport = shopRepoertRepository.save(shopReport);
		if (savedReport.getId() != null) {
			return savedReport.getId();
		} else {
			return 0;
		}
	}

}
