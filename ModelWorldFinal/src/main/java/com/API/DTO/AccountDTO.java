package com.API.DTO;

import java.util.Date;

import com.API.model.Account;

public class AccountDTO {
	private int id;
    private String tenTaiKhoan;
    private String email;
    private String hoVaTen;
    private String soDienThoai;
    private String gioiTinh;
    private Date sinhNhat;
    private String trangThai;
    private String vaiTro; // Thêm vai trò ở đây
    private String hinhAnh;

    // Constructor
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.tenTaiKhoan = account.getTenTaiKhoan();
        this.email = account.getEmail();
        this.hoVaTen = account.getHoVaTen();
        this.soDienThoai = account.getSoDienThoai();
        this.gioiTinh = account.getGioiTinh();
        this.sinhNhat = account.getSinhNhat();
        this.trangThai = account.getTrangThai();
        this.vaiTro = account.getVaiTro(); // Copy vai trò từ Account
        this.hinhAnh = account.getHinhAnh();
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
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
    
}
