package com.API.model;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Entity
@Table(name = "taikhoan")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
//	@NotNull
//	@Length(min = 5,message="Tên tài khoản ít nhất 5 ký tự..")
	private String tenTaiKhoan;
	@JsonIgnore
	private String matKhau;
	private String email;
	@NotNull(message = "Họ và tên không được phéo rõno")
	@NotBlank(message="Họ tên không được phép rỗng")
	@Length(min = 5,message="Tên tài khoản ít nhất 5 ký tự..")
	private String hoVaTen;
	private String soDienThoai;
	@NotNull
	@Min(value = 0,message="Giới tính 0->2")
	@Max(value = 2,message="Giới tính 0->2")
	private String gioiTinh;
	private Date sinhNhat;
	private String trangThai;
	@JsonIgnore
	private String vaiTro;
	private String hinhAnh;
	
	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public Account() {
		
	}
	
	public Account(Integer id ,String tens,String SDT) {
		this.hoVaTen=tens;
		this.soDienThoai=SDT;
		this.id=id;
	}
	
	public Account(Integer id ,String tens) {
		this.hoVaTen=tens;
		this.id=id;
	}
	
	public Account(String tens,String SDT,Integer id ) {
		this.matKhau=tens;
		this.soDienThoai=SDT;
	}
	
	public Account(String tens,String SDT) {
		this.hoVaTen=tens;
		this.soDienThoai=SDT;
	}
	public Account(String tens,String SDT,String hinhAnh) {
		this.hoVaTen=tens;
		this.soDienThoai=SDT;
		this.hinhAnh=hinhAnh;
	}
	
	public Account(int id ) {
		this.id=id;
	}
	
	public Account(int id, String tenTaiKhoan, String matKhau, String email, String hoVaTen, String soDienThoai,
			String gioiTinh, Date sinhNhat, String trangThai, String vaiTro) {
		this.id = id;
		this.tenTaiKhoan = tenTaiKhoan;
		this.matKhau = matKhau;
		this.email = email;
		this.hoVaTen = hoVaTen;
		this.soDienThoai = soDienThoai;
		this.gioiTinh = gioiTinh;
		this.sinhNhat = sinhNhat;
		this.trangThai = trangThai;
		this.vaiTro = vaiTro;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTenTaiKhoan() {
		return tenTaiKhoan;
	}
	public void setTenTaiKhoan(String tenTaiKhoan) {
		this.tenTaiKhoan = tenTaiKhoan;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHoVaTen() {
		return hoVaTen;
	}
	public void setHoVaTen(String hoVaTen) {
		this.hoVaTen = hoVaTen;
	}
	public String getSoDienThoai() {
		return soDienThoai;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
	public String getGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public Date getSinhNhat() {
		return sinhNhat;
	}
	public void setSinhNhat(Date sinhNhat) {
		this.sinhNhat = sinhNhat;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public String getVaiTro() {
		return vaiTro;
	}
	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}}