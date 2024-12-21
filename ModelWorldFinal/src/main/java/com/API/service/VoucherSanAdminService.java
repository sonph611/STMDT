package com.API.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.API.DTO.voucher.VoucherSanAdminDTO;
import com.API.model.VoucherSan;

public interface VoucherSanAdminService {
	public Page<VoucherSan> getVouchers(Pageable pageable);
	public Optional<VoucherSan> finById(Integer id);
	public VoucherSan saveOrUpdateVoucher(Integer id, VoucherSanAdminDTO voucherDTO);
	public boolean updateVoucherStatus(Integer id, int status);
}
