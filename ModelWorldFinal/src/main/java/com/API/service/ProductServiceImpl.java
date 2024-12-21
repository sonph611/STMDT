package com.API.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.API.model.ProductReport;
import com.API.model.TaiKhoanReport;
import com.API.model.ViPham;
import com.API.repository.ProductRepository;
import com.API.repository.ViPhamRepository;
import com.API.repository.productReportRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	ViPhamRepository viPhamRepository;
	@Autowired
	productReportRepository productReportRepository;

	@Override
	public Page<Object[]> getProductsWithMaxAndMinPrice(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return productRepository.findProductsWithMaxAndMinPrice(pageable);
	}

	@Override
	public Object[] getProductDetailsById(Integer id) {
		Object[] result = productRepository.findProductDetailsById(id);
		return result;
	}


	@Override
	public Page<Object[]> getChiTietSanPhamBySanPhamId(Integer id, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> result = productRepository.findChiTietSanPhamBySanPhamId(id, pageable);
		return result;
	}

	@Override
	public List<Object[]> findMauVaKichThuocBySanPhamId(Integer id) {
		List<Object[]> result = productRepository.findMauVaKichThuocBySanPhamId(id);
		return result;
	}

	@Override
	public List<Map<String, Object>> getPromotionByProductId(int productId) {
		List<Object[]> results = productRepository.findPromotionByProductId(productId);

        // Xử lý dữ liệu thành danh sách Map
        List<Map<String, Object>> promotions = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> promotion = new HashMap<>();
            promotion.put("chiTietKhuyenMaiId", row[0]);
            promotion.put("khuyenMaiId", row[1]);
            promotion.put("giaTriKhuyenMai", row[2]);
            promotion.put("trangThai", row[3]);
            promotions.add(promotion);
        }

        // Trả về danh sách, nếu không có sẽ là danh sách rỗng
        return promotions;

	}

	@Override
	public boolean updateTrangThai(Integer id, Integer trangThai) {
		int rowsUpdated = productRepository.updateTrangThaiById(id, trangThai);
		return rowsUpdated > 0;
	}

	@Override
	public List<ViPham> getViPhamByLoai(Integer loai) {
		return viPhamRepository.findByLoai(loai);
	}

	@Override
	public int saveProductReport(ProductReport productReport) {
		productReport.setCreateAt(java.time.LocalDateTime.now());
		try {
			ProductReport savedReport = productReportRepository.save(productReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProductReport savedReport = productReportRepository.save(productReport);
		if (savedReport.getId() != null) {
			return savedReport.getId();
		} else {
			return 0;
		}
	}

}
