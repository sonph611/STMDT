package com.API.DTO;

import com.API.model.Product;

public class ProductLiveDTO {
	private Product p;
	private Integer soLuongLive;
	private Integer giaTriKhuyenMai;
	private Integer id;
	private Double giaGiam;
	public Double getGiaGiam() {
		return giaGiam;
	}
	public void setGiaGiam(Double giaGiam) {
		this.giaGiam = giaGiam;
	}
	public ProductLiveDTO(Integer id,Integer soLuongLive,Product p, Integer giaTriKhuyenMai,double giaGiam) {
		this.p = p;
		this.soLuongLive = soLuongLive;
		this.giaTriKhuyenMai = giaTriKhuyenMai;
		this.id = id;
		this.giaGiam=giaGiam;
	}
	public Product getP() {
		return p;
	}
	public void setP(Product p) {
		this.p = p;
	}
	public Integer getSoLuongLive() {
		return soLuongLive;
	}
	public void setSoLuongLive(Integer soLuongLive) {
		this.soLuongLive = soLuongLive;
	}
	public Integer getGiaTriKhuyenMai() {
		return giaTriKhuyenMai;
	}
	public void setGiaTriKhuyenMai(Integer giaTriKhuyenMai) {
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
