package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "AnhSanPham")
public class ProductImage {
	public ProductImage(@NotBlank(message = "Hình ảnh không hợp lệ") String hinhAnh) {
		super();
		this.hinhAnh = hinhAnh;
	}
	
	public ProductImage(@NotBlank(message = "Hình ảnh không hợp lệ") String hinhAnh,Product p) {
		super();
		this.hinhAnh = hinhAnh;
		this.product=p;
	}
	public ProductImage() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "Hình ảnh không hợp lệ")
	private String hinhAnh;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	private Product product;
}
