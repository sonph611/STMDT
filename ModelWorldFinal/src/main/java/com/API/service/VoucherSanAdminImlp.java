package com.API.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.API.DTO.voucher.VoucherSanAdminDTO;
import com.API.model.VoucherSan;
import com.API.repository.VoucherSanRepository;

@Service
public class VoucherSanAdminImlp implements VoucherSanAdminService{
	
	@Autowired
	VoucherSanRepository voucherSanRepository;

//	@Override
//	 public Page<VoucherSan> getVouchers(Pageable pageable) {
//        return voucherSanRepository.findAll(pageable);
//    }

	@Override
	public Optional<VoucherSan> finById(Integer id) {
		return voucherSanRepository.findById(id);
	}

	@Override
	public Page<VoucherSan> getVouchers(Pageable pageable) {
		return voucherSanRepository.findAll(pageable);
	}

	@Override
	public VoucherSan saveOrUpdateVoucher(Integer id, VoucherSanAdminDTO voucherDTO) {
	    // Kiểm tra các thông tin cần thiết từ voucherDTO
	    if (voucherDTO.getTenVoucher() == null || voucherDTO.getTenVoucher().isEmpty()) {
	        throw new IllegalArgumentException("Tên voucher không được để trống");
	    }
	    if (voucherDTO.getMaVoucher() == null || voucherDTO.getMaVoucher().isEmpty()) {
	        throw new IllegalArgumentException("Mã voucher không được để trống");
	    }
	    if (voucherDTO.getNgayBatDau() == null || voucherDTO.getNgayKetThuc() == null) {
	        throw new IllegalArgumentException("Thời gian bắt đầu và kết thúc không được để trống");
	    }

	    // Kiểm tra thời gian bắt đầu và kết thúc
	    if (voucherDTO.getNgayBatDau().after(voucherDTO.getNgayKetThuc())) {
	        throw new IllegalArgumentException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
	    }

	    // Kiểm tra giá trị giảm (Giảm theo % hoặc VND)
	    if (voucherDTO.getLoaiVoucher() == 1) {
	        // Giảm theo phần trăm
	        if (voucherDTO.getGiaTriGiam() < 1 || voucherDTO.getGiaTriGiam() > 50) {
	            throw new IllegalArgumentException("Giảm theo phần trăm phải trong khoảng từ 1% đến 50%");
	        }
	    } else if (voucherDTO.getLoaiVoucher() == 0) {
	        // Giảm theo VND
	        if (voucherDTO.getGiaTriGiam() < 1000 || voucherDTO.getGiaTriGiam() > 1000000) {
	            throw new IllegalArgumentException("Giảm theo VND phải trong khoảng từ 1,000 VND đến 1,000,000 VND");
	        }
	    } else {
	        throw new IllegalArgumentException("Loại giảm không hợp lệ");
	    }

	    // Kiểm tra các giá trị khác
	    if (voucherDTO.getDonToiThieu() < 0) {
	        throw new IllegalArgumentException("Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0");
	    }
	    // Kiểm tra các giá trị khác
	    if (voucherDTO.getGioiHanGiam() < 0) {
	        throw new IllegalArgumentException("Giá trị giới hạn phải lớn hơn hoặc bằng 0");
	    }
	    if (voucherDTO.getSoLuocDung() <= 0) {
	        throw new IllegalArgumentException("Tổng lượt sử dụng tối đa phải lớn hơn 0");
	    }
	    if (voucherDTO.getSoLuocDungMoiNguoi() <= 0) {
	        throw new IllegalArgumentException("Tổng lượt sử dụng tối đa cho mỗi người phải lớn hơn 0");
	    }

	    // Kiểm tra tên voucher có bị trùng không
	    VoucherSan existingVoucher = voucherSanRepository.findByTenVoucher(voucherDTO.getTenVoucher());
	    if (existingVoucher != null && (id == 0 || !existingVoucher.getId().equals(id))) {
	        throw new IllegalArgumentException("Tên voucher đã tồn tại");
	    }

	    // Nếu id có giá trị thì tìm voucher để cập nhật, nếu không thì tạo mới
	    VoucherSan voucher = id != null ? voucherSanRepository.findById(id).orElse(new VoucherSan()) : new VoucherSan();

	    // Cập nhật thông tin voucher từ DTO
	    voucher.setTenVoucher(voucherDTO.getTenVoucher());
	    voucher.setMaVoucher(voucherDTO.getMaVoucher());
	    voucher.setNgayBatDau(voucherDTO.getNgayBatDau());
	    voucher.setNgayKetThuc(voucherDTO.getNgayKetThuc());
	    voucher.setLoaiVoucher(voucherDTO.getLoaiVoucher());
	    voucher.setGiaTriGiam(voucherDTO.getGiaTriGiam());
	    voucher.setDonToiThieu(voucherDTO.getDonToiThieu());
	    voucher.setSoLuocDung(voucherDTO.getSoLuocDung());
	    voucher.setSoLuocDungMoiNguoi(voucherDTO.getSoLuocDungMoiNguoi());
	    voucher.setGioiHanGiam(voucherDTO.getGioiHanGiam());

	    // Trạng thái mặc định cho voucher có id = 0
	    if (id == 0) {
	        voucher.setTrangThai(0);
	        voucher.setSoLuocDaDung(0);
	    } else {
	        voucher.setTrangThai(voucherDTO.getTrangThai());
	    }

	    voucher.setHinhThucApDung(1); // Đặt giá trị hinhThucApDung mặc định là 1

	    // Lưu hoặc cập nhật voucher
	    return voucherSanRepository.saveAndFlush(voucher);
	}

	@Override
	public boolean updateVoucherStatus(Integer id, int status) {
		VoucherSan voucher = voucherSanRepository.findById(id).orElse(null);

        if (voucher != null) {
            // Cập nhật trạng thái
            voucher.setTrangThai(status);
            voucherSanRepository.save(voucher);
            return true;
        }
        return false; 
	}



}
