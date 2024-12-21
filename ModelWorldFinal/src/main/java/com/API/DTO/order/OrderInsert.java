package com.API.DTO.order;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.API.model.DiaChi;
import com.API.model.Order;
import com.API.model.ThanhToan;
import com.API.model.VoucherSan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderInsert {
	@NotNull(message="Thuộc tính items là bắt buộc")
	@Size(min = 1,message = "Vui lòng chọn ít nhất một sản phẩm khi đặt hàng")
	Set<Integer> items;
	@Size(min = 1)
	Map<Integer,Order> orderShop;
	private VoucherSan voucherSan;
	@NotNull(message = "Chưa chọn hình thức thanh toán")
	private ThanhToan thanhToan;
	@NotNull(message = "Chưa chọn địa chỉ nhận hàng")
	private DiaChi diaChi;
	
	public Set<Integer> getItems() {
		return items;
	}
	public void setItems(Set<Integer> items) {
		this.items = items;
	}
	
	public VoucherSan getVoucherSan() {
		return voucherSan;
	}
	public void setVoucherSan(VoucherSan voucherSan) {
		this.voucherSan = voucherSan;
	}
	public ThanhToan getThanhToan() {
		return thanhToan;
	}
	public void setThanhToan(ThanhToan thanhToan) {
		this.thanhToan = thanhToan;
	}
	public Map<Integer, Order> getOrderShop() {
		return orderShop;
	}
	public void setOrderShop(Map<Integer, Order> orderShop) {
		this.orderShop = orderShop;
	}
	public DiaChi getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(DiaChi diaChi) {
		this.diaChi = diaChi;
	}
	
	
}

