package com.API.DTO.voucher;

import java.util.List;
import java.util.Set;

import com.API.DTO.khuyenMai.KhuyenMaiItem;
import com.API.model.VoucherShop;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VoucherInsert {
	@Valid
	private VoucherShop voucher;
	private String moTaThongBao;
	
	public String getMoTaThongBao() {
		return moTaThongBao;
	}
	public void setMoTaThongBao(String moTaThongBao) {
		this.moTaThongBao = moTaThongBao;
	}
	public VoucherInsert() {
	}
	public VoucherInsert(@Valid VoucherShop voucher, List<KhuyenMaiItem> voucherDetail) {
		super();
		this.voucher = voucher;
		this.voucherDetail = voucherDetail;
	}
	public VoucherShop getVoucher() {
		return voucher;
	}
	public void setVoucher(VoucherShop voucher) {
		this.voucher = voucher;
	}
	public Set<Integer> getVoucherProducts() {
		return voucherProducts;
	}
	public void setVoucherProducts(Set<Integer> voucherProducts) {
		this.voucherProducts = voucherProducts;
	}
	
	public List<KhuyenMaiItem> getVoucherDetail() {
		return voucherDetail;
	}
	public void setVoucherDetail(List<KhuyenMaiItem> voucherDetail) {
		this.voucherDetail = voucherDetail;
	}
	
	private List<KhuyenMaiItem> voucherDetail;
	@NotNull(message = "Chưa điền trường products")
	@Size(min = 1,message = "Vui lòng chọn ít nhất 1 product cho mã giảm giá này !!!")
	private Set<Integer> voucherProducts;
}
