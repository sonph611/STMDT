package com.API.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import com.API.model.ProductReport;
import com.API.model.TaiKhoanReport;
import com.API.model.ViPham;

public interface ProductService {
	 public Page<Object[]> getProductsWithMaxAndMinPrice(int page, int size);
	 public Object[] getProductDetailsById(Integer id);
	 public Page<Object[]> getChiTietSanPhamBySanPhamId(Integer id, int page, int size);
	 public List<Object[]> findMauVaKichThuocBySanPhamId(@Param("id") Integer id);
	  public List<Map<String, Object>> getPromotionByProductId(int productId);
		 public boolean updateTrangThai(Integer id, Integer trangThai);
			public List<ViPham> getViPhamByLoai(Integer loai);
			public int saveProductReport(ProductReport productReport);
}
