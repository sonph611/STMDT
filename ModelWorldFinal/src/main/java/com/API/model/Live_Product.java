package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "live_product")
public class Live_Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LiveSession getLive() {
		return live;
	}
	public void setLive(LiveSession live) {
		this.live = live;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Double getGiaGiam() {
		return giaGiam;
	}
	public void setGiaGiam(Double giaGiam) {
		this.giaGiam = giaGiam;
	}
	private Integer soLuocDaDung;
	
	public Integer getSoLuocDaDung() {
		return soLuocDaDung;
	}
	public void setSoLuocDaDung(Integer soLuocDaDung) {
		this.soLuocDaDung = soLuocDaDung;
	}
	public Live_Product(Integer productId,LiveSession l) {
		this.live=l;
		this.product=new Product(productId);
		this.giaGiam=0.0;
	}
	
	public Live_Product() {
	}
	
	@ManyToOne
	@JoinColumn(name="liveId")
	
	private LiveSession live;
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	public Integer getSoLuongGioiHan() {
		return soLuongGioiHan;
	}
	public void setSoLuongGioiHan(Integer soLuongGioiHan) {
		this.soLuongGioiHan = soLuongGioiHan;
	}
	private Double giaGiam;
	private Integer soLuongGioiHan;
}
