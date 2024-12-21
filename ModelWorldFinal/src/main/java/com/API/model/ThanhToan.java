package com.API.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "thanhToan")
public class ThanhToan {
	@Id
	@NotNull(message = "Id payment cant not null")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "TenHinhThucThanhToan")
	private String tebHinhThucThanhToan;
	private int trangThai;
	
	public ThanhToan() {
		
	}
	
	public ThanhToan(Integer id) {
		this.id=id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHinhThucThanhToan() {
		return tebHinhThucThanhToan;
	}
	public void setHinhThucThanhToan(String hinhThucThanhToan) {
		this.tebHinhThucThanhToan = hinhThucThanhToan;
	}
	public int getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}
}
