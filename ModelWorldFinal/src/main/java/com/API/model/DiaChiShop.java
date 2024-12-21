package com.API.model;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="diaChiShop")
public class DiaChiShop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
//	@Column(name="shopId")
	@ManyToOne()
	@JoinColumn(name="shopId")
	@JsonIgnore
	private Shop shop;
	
	@NotNull(message = "Không được để trong thành phố")
	@Min(value = 1,message = "Địa chỉ thành phố không hợp lệ")
	@Column(name = "provice_Id")
	private Integer proviceId;
	
	@Column(name = "district_Id")
	@NotNull(message = "Không được để trong huyện")
	@Min(value = 1,message = "Địa chỉ huyện không hợp lệ")
	private Integer districtId;
	
	@Column(name = "ward_code")
	@NotNull(message = "Không được để trong Thị xã")
	@Min(value = 1,message = "Địa chỉ thị xã không hợp lệ")
	private Integer wardCode;
	
	@NotNull(message = "Không được để trống chi tiết địa chỉ")
	@Length(min = 20,message = "Chiều dài chi tiết địa chỉ vui lòng lớn hơn 20 ký tự")
	private String toanBoDiaChi;
	
	@NotNull(message = "Không được để trống chi tiết địa chỉ")
	@Length(min = 20,message = "Chiều dài chi tiết địa chỉ vui lòng lớn hơn 20 ký tự")
	private String chiTietDiaChi;
	
	public String getChiTietDiaChi() {
		return chiTietDiaChi;
	}

	public void setChiTietDiaChi(String chiTietDiaChi) {
		this.chiTietDiaChi = chiTietDiaChi;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	private String ghiChu;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProviceId() {
		return proviceId;
	}

	public void setProviceId(Integer proviceId) {
		this.proviceId = proviceId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getWardCode() {
		return wardCode;
	}

	public void setWardCode(Integer wardCode) {
		this.wardCode = wardCode;
	}

	public String getToanBoDiaChi() {
		return toanBoDiaChi;
	}

	public void setToanBoDiaChi(String toanBoDiaChi) {
		this.toanBoDiaChi = toanBoDiaChi;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
}
