package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "giaoDich")
public class GiaoDich {
	@Transient
	private static final String success="SUCCESS";
	@Transient
	private static final String fail="FAIL";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Double tongTien;
	@ManyToOne()
	@JoinColumn(name="donHang_Id")
	private Order order;
	
	private String maGiaoDich;
	
	public String getMaGiaoDich() {
		return maGiaoDich;
	}
	
	public GiaoDich() {
		
	}
	
	public GiaoDich(Integer orderId,Double tongTien,String maGiaoDich) {
		this.order=new Order(orderId);
		this.maGiaoDich=maGiaoDich;
		this.tongTien=tongTien;
	}

	public void setMaGiaoDich(String maGiaoDich) {
		this.maGiaoDich = maGiaoDich;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getTongTien() {
		return tongTien;
	}

	public void setTongTien(Double tongTien) {
		this.tongTien = tongTien;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	private String trangThai;
//	private 
}
