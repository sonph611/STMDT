package com.API.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.API.model.ShopReport;
import com.API.model.TaiKhoanReport;
import com.API.model.ViPham;

public interface UserAdminService {
	public Page<Object[]> getUsersWithOrderStats(Pageable pageable);
	 public Map<String, Object> getAccounAndAdress(Integer id);
	 public Object[] getOrderStatistics(Integer taiKhoanId);
	 public Page<Object[]> getOrderDetailsByTaiKhoanId(Integer taiKhoanId, Pageable pageable);
//	 public Page<Object[]> getUsersWithOrderStats(Pageable pageable);
//	 public Map<String, Object> getAccounAndAdress(Integer id);
	 public boolean updateTrangThai(Integer id, String trangThai);
		public List<ViPham> getViPhamByLoai(Integer loai);
		public int saveTaiKhoanReport(TaiKhoanReport shopReport);
}
