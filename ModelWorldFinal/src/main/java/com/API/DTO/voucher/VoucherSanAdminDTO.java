package com.API.DTO.voucher;

import java.util.Date;

public class VoucherSanAdminDTO {
    private Integer id;
    private String tenVoucher;
    private String maVoucher;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Integer loaiVoucher;  // Ví dụ: 1 - percentage, 2 - amount
    private Integer soLuocDung;   // Số lượt sử dụng
    private float giaTriGiam;     // Giá trị giảm
    private Integer hinhThucApDung; // Hình thức áp dụng (có thể là 1 - đơn hàng, 2 - sản phẩm)
    private float gioiHanGiam;    // Giới hạn giảm
    private double donToiThieu;   // Đơn hàng tối thiểu
    private Integer soLuocDaDung; // Số lượt đã sử dụng
    private Integer soLuocDungMoiNguoi;
    private Integer trangThai;
    
    

    public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	public Integer getSoLuocDungMoiNguoi() {
		return soLuocDungMoiNguoi;
	}

	public void setSoLuocDungMoiNguoi(Integer soLuocDungMoiNguoi) {
		this.soLuocDungMoiNguoi = soLuocDungMoiNguoi;
	}

	// Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenVoucher() {
        return tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Integer getLoaiVoucher() {
        return loaiVoucher;
    }

    public void setLoaiVoucher(Integer loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }

    public Integer getSoLuocDung() {
        return soLuocDung;
    }

    public void setSoLuocDung(Integer soLuocDung) {
        this.soLuocDung = soLuocDung;
    }

    public float getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(float giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public Integer getHinhThucApDung() {
        return hinhThucApDung;
    }

    public void setHinhThucApDung(Integer hinhThucApDung) {
        this.hinhThucApDung = hinhThucApDung;
    }

    public float getGioiHanGiam() {
        return gioiHanGiam;
    }

    public void setGioiHanGiam(float gioiHanGiam) {
        this.gioiHanGiam = gioiHanGiam;
    }

    public double getDonToiThieu() {
        return donToiThieu;
    }

    public void setDonToiThieu(double donToiThieu) {
        this.donToiThieu = donToiThieu;
    }

    public Integer getSoLuocDaDung() {
        return soLuocDaDung;
    }

    public void setSoLuocDaDung(Integer soLuocDaDung) {
        this.soLuocDaDung = soLuocDaDung;
    }
}
