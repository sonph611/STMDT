package com.API.DTO;

import java.util.Set;

import com.API.model.Product;
import com.API.model.ProductDetail;

import jakarta.validation.Valid;

public class ProductRequestDTO {
	@Valid
	private Product product;
	@Valid
	private Set<ProductDetail> productDetail;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Set<ProductDetail> getProductDetail() {
		return productDetail;
	}
	public void setProductDetail(Set<ProductDetail> productDetail) {
		this.productDetail = productDetail;
	}
	
}
