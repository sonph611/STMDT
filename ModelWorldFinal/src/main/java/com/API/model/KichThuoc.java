package com.API.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kichThuoc")
public class KichThuoc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JsonProperty("tenKichThuoc")
	private String tenKichThuoc;
	public int getId() {
		return id;
	}
	
	public KichThuoc(String tenKichThuoc) {
		this.tenKichThuoc=tenKichThuoc;
	}
	
	public KichThuoc() {
		
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTenMau() {
		return tenKichThuoc;
	}
	public void setTenMau(String tenMau) {
		this.tenKichThuoc = tenMau;
	}
}
