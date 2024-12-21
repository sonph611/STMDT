package com.API.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.API.model.ShopReport;
import com.API.model.TaiKhoanReport;
import com.API.model.ViPham;
import com.API.repository.AccountRepository;
import com.API.repository.DonHangRepository;
import com.API.repository.TaiKhoanReportRepository;
import com.API.repository.ViPhamRepository;

@Service
public class UserAdminServiceImpl implements UserAdminService {
	
//	@Autowired
//	AccountRepository accountRepository;
//
//	@Override
//	public Page<Object[]> getUsersWithOrderStats(Pageable pageable) {
//		Page<Object[]> results = accountRepository.findUsersWithOrderStats(pageable);
//		return results;
//	}
//
//	@Override
//	public Map<String, Object> getAccounAndAdress(Integer id) {
//		 List<Object[]> results = accountRepository.findAccountWithAddressesById(id);
//
//	        Map<String, Object> account = new HashMap<>();
//	        List<Map<String, Object>> addresses = new ArrayList<>();
//
//	        for (Object[] row : results) {
//	            if (account.isEmpty()) {
//	                account.put("email", row[0]);
//	                account.put("matKhau", row[1]);
//	                account.put("tenTaiKhoan", row[2]);
//	                account.put("soDienThoai", row[3]);
//	                account.put("gioiTinh", row[4]);
//	                account.put("sinhNhat", row[5]);
//	                account.put("hoVaTen", row[6]);
//	                account.put("vaiTro", row[7]);
//	                account.put("trangThai", row[8]);
//	                account.put("idUser", row[9]);
//	                account.put("hinhAnh", row[10]);
//	            }
//
//	            if (row[11] != null) {
//	                Map<String, Object> address = new HashMap<>();
//	                address.put("idAddress", row[11]);
//	                address.put("soDienThoai", row[12]);
//	                address.put("toanBoDiaChi", row[13]);
//	                addresses.add(address);
//	            }
//	        }
//
//	        account.put("addresses", addresses);
//	        return account;
//	}
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	DonHangRepository donHangRepository;
	@Autowired
	ViPhamRepository viPhamRepository;
	@Autowired
	TaiKhoanReportRepository taiKhoanReportRepository;

	@Override
	public Page<Object[]> getUsersWithOrderStats(Pageable pageable) {
		Page<Object[]> results = accountRepository.findUsersWithOrderStats(pageable);
		return results;
	}

	@Override
	public Map<String, Object> getAccounAndAdress(Integer id) {
		 List<Object[]> results = accountRepository.findAccountWithAddressesById(id);

	        Map<String, Object> account = new HashMap<>();
	        List<Map<String, Object>> addresses = new ArrayList<>();

	        for (Object[] row : results) {
	            if (account.isEmpty()) {
	                account.put("email", row[0]);
	                account.put("matKhau", row[1]);
	                account.put("tenTaiKhoan", row[2]);
	                account.put("soDienThoai", row[3]);
	                account.put("gioiTinh", row[4]);
	                account.put("sinhNhat", row[5]);
	                account.put("hoVaTen", row[6]);
	                account.put("vaiTro", row[7]);
	                account.put("trangThai", row[8]);
	                account.put("idUser", row[9]);
	                account.put("hinhAnh", row[10]);
	            }

	            if (row[11] != null) {
	                Map<String, Object> address = new HashMap<>();
	                address.put("idAddress", row[11]);
	                address.put("soDienThoai", row[12]);
	                address.put("toanBoDiaChi", row[13]);
	                addresses.add(address);
	            }
	        }

	        account.put("addresses", addresses);
	        return account;
	}

	@Override
	public Object[] getOrderStatistics(Integer taiKhoanId) {		
        return donHangRepository.calculateOrderStats(taiKhoanId);
	}

	

	@Override
	public Page<Object[]> getOrderDetailsByTaiKhoanId(Integer taiKhoanId, Pageable pageable) {
		int pageSize = pageable.getPageSize(); // Kích thước trang
	    int currentPage = pageable.getPageNumber(); // Số trang hiện tại
	    int offset = currentPage * pageSize; // Vị trí bắt đầu
	    
	    List<Object[]> results = donHangRepository.findOrderDetailsByTaiKhoanIdWithPagination(taiKhoanId, pageSize, offset);
	    
	    long totalRecords = donHangRepository.countOrdersByTaiKhoanId(taiKhoanId);

	    return new PageImpl<>(results, pageable, totalRecords);
	}

	@Override
	public boolean updateTrangThai(Integer id, String trangThai) {
		int rowsUpdated = accountRepository.updateTrangThaiById(id, trangThai);
		return rowsUpdated > 0;
	}

	@Override
	public List<ViPham> getViPhamByLoai(Integer loai) {
		return viPhamRepository.findByLoai(loai);
	}

	@Override
	public int saveTaiKhoanReport(TaiKhoanReport taiKhoan) {
		taiKhoan.setCreateAt(java.time.LocalDateTime.now());
		try {
			TaiKhoanReport savedReport = taiKhoanReportRepository.save(taiKhoan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		TaiKhoanReport savedReport = taiKhoanReportRepository.save(taiKhoan);
		if (savedReport.getId() != null) {
			return savedReport.getId();
		} else {
			return 0;
		}
	}

}
