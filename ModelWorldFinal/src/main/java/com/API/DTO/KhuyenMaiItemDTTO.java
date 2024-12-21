package com.API.DTO;

import com.API.model.Product;

import jakarta.validation.constraints.NotNull;

public class KhuyenMaiItemDTTO {
	
	
	public Integer getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	@NotNull(message = "Chưa chọn product")
	private Integer product;
	@NotNull(message = "Sản phẩm limit giới hạn chưa hợp lệ")
	private int limit;
}
