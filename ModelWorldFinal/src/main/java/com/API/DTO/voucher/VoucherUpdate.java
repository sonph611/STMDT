package com.API.DTO.voucher;

import java.util.Date;
import java.util.Set;

import com.API.model.VoucherShop;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.val;

public class VoucherUpdate {
	
	
	
	@Valid
	private VoucherShop voucherShop;
	
//	private Integer type;
	
//	public Integer getType() {
//		return type;
//	}
//	public void setType(Integer type) {
//		this.type = type;
//	}
	@NotNull(message="Không tìm thấy tham số Id !!!")
	private Integer id;
	
	public VoucherShop getVoucherShop() {
		return voucherShop;
	}
	public void setVoucherShop(VoucherShop voucherShop) {
		this.voucherShop = voucherShop;
	}
//	private String moTa;
//	
//	public String getMoTa() {
//		return moTa;
//	}
//	public void setMoTa(String moTa) {
//		this.moTa = moTa;
//	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Set<Integer> getProductDeleteVoucher() {
		return productDeleteVoucher;
	}
	public void setProductDeleteVoucher(Set<Integer> productDeleteVoucher) {
		this.productDeleteVoucher = productDeleteVoucher;
	}
//	@NotNull(message = "Vui lòng nhập trường ngày kết thúc voucher")
//	private Date date;
//	@NotBlank(message = "Vui lòng không để trống tên khuyến mãi")
//	private String voucherName;
	
//	@Min(value = 0,message="Vui lòng nhập không lớn hơn ")
//	private int maxCount;
	
//	public String getVoucherName() {
//		return voucherName;
//	}
//	public void setVoucherName(String voucherName) {
//		this.voucherName = voucherName;
//	}
//	public int getMaxCount() {
//		return maxCount;
//	}
//	public void setMaxCount(int maxCount) {
//		this.maxCount = maxCount;
//	}
	private Set<Integer> productInsertVoucher;
	private Set<Integer> productDeleteVoucher;
//	public Date getDate() {
//		return date;
//	}
//	public void setDate(Date date) {
//		this.date = date;
//	}
	public Set<Integer> getProductInsertVoucher() {
		return productInsertVoucher;
	}
	public void setProductInsertVoucher(Set<Integer> productInsertVoucher) {
		this.productInsertVoucher = productInsertVoucher;
	}
	public void setProductDelete(Set<Integer> productDelete) {
		this.productDeleteVoucher = productDelete;
	}
}
