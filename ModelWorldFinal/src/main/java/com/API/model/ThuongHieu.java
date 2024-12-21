package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ThuongHieu")
public class ThuongHieu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String tenThuongHieu;
	
	public ThuongHieu() {
		
	}
	
	public ThuongHieu(int id) {
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTenMau() {
		return tenThuongHieu;
	}
	public void setTenMau(String tenMau) {
		this.tenThuongHieu = tenMau;
	}
}
