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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "diaChi")
public class DiaChi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id ;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "taiKhoanid")
	private Account account;
	@NotNull(message = "Không được để trống Thành phố")
	@Min(value = 1,message = "Địa chỉ thành phố không hợp lệ")
	private int districtId;
	@NotNull(message = "Không được để trong huyện")
	@Min(value = 1,message = "Địa chỉ huyện không hợp lệ")
	private int provinceId;
	@NotNull(message = "Không được để trong xã")
	@Min(value = 1,message = "Địa chỉ xã không hợp lệ")
	private String wardCode;
	@NotNull(message = "Không được để trống chi tiết địa chỉ")
	@Length(min = 5,message = "Địa chỉ chi tiết không hợp lệ")
	private String toanBoDiaChi;
	@Min(value=0,message = "Giá trị default không hợp lệ")
	@Max(value=1,message = "Giá trị default không hợp lệ")
	private Integer isDefault;
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	private Integer isShop;
	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "cuaHangId")
	private Shop shop;
	
	private String ChiTietDiaChi;
	private String ghiChu;
	
    public String getDiaChiChiTiet() {
		return ChiTietDiaChi;
	}

	public void setDiaChiChiTiet(String diaChiChiTiet) {
		this.ChiTietDiaChi = diaChiChiTiet;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	@NotNull(message = "Không được bỏ trống số điện thoại")
    @Pattern(regexp = "^(0|\\+84)(9[0-9]{8}|1[2-9][0-9]{7}|3[2-9][0-9]{7}|5[0-9]{8}|7[0-9]{8}|8[0-9]{8})$", 
             message = "Số điện thoại không hợp lệ, vui lòng nhập lại")
//    @Size(min = 10, max = 11, message = "SĐT phải có từ 10-11 ký tự")
	@Column(name="soDienThoai")
    private String soDienThoai;
	public String getChiTietDiaChi() {
		return ChiTietDiaChi;
	}

	public void setChiTietDiaChi(String chiTietDiaChi) {
		ChiTietDiaChi = chiTietDiaChi;
	}

	public Integer getIsShop() {
		return isShop;
	}

	public void setIsShop(Integer isShop) {
		this.isShop = isShop;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public DiaChi() {
		
	}
	
	public DiaChi(int id) {
		this.id=id;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public String getWardCode() {
		return wardCode;
	}
	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}
	public String getToanBoDiaChi() {
		return toanBoDiaChi;
	}
	public void setToanBoDiaChi(String toanBoDiaChi) {
		this.toanBoDiaChi = toanBoDiaChi;
	}
}
