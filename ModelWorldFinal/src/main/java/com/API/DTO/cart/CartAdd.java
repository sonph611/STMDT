package com.API.DTO.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartAdd {
	
	private Integer id;
	@NotNull(message="Chưa chọn sản phẩm để thêm")
	private int productDetailId;
	@Min(value = 1,message = "Số lượng đặt hàng không được nhỏ hơn 1")
	private int soLuong;
	
	public int getProductDetailId() {
		return productDetailId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setProductDetailId(int productDetailId) {
		this.productDetailId = productDetailId;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	
}
