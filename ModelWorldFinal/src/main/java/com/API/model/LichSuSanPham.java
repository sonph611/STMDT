package com.API.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "viewedcategories")
public class LichSuSanPham {
	@Id
	private int id;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "category_id")
	private int categoryId;
	private LocalDateTime viewed_at;
	private int product_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getuserId() {
		return userId;
	}
	public void setuserId(int userId) {
		this.userId = userId;
	}
	public int getCategory_id() {
		return categoryId;
	}
	public void setCategory_id(int category_id) {
		this.categoryId = category_id;
	}
	public LocalDateTime getViewed_at() {
		return viewed_at;
	}
	public void setViewed_at(LocalDateTime viewed_at) {
		this.viewed_at = viewed_at;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public LichSuSanPham(int id, int userId, int category_id, LocalDateTime viewed_at, int product_id) {
		
		this.id = id;
		this.userId = userId;
		this.categoryId = category_id;
		this.viewed_at = viewed_at;
		this.product_id = product_id;
	}
	public LichSuSanPham() {
		
	}

	
	
}
