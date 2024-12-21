package com.API.DTO.cart;

import com.API.model.VoucherSan;

public class VoucherSanAply {
	private VoucherSan voucher;
	private Integer accountId;
	public VoucherSan getVoucher() {
		return voucher;
	}
	public void setVoucher(VoucherSan voucher) {
		this.voucher = voucher;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public VoucherSanAply(VoucherSan voucher, Integer accountId) {
		super();
		this.voucher = voucher;
		this.accountId = accountId;
	}
	
}
