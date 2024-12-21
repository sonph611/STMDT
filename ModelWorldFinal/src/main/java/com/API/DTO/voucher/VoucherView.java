package com.API.DTO.voucher;

import java.util.Date;

public class VoucherView {
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSoLuongSanPhamApDung(long soLuongSanPhamApDung) {
		this.soLuongSanPhamApDung = soLuongSanPhamApDung;
	}
	private String voucherName;
	private String maVoucher;
	private Date ngayBatDau;
	private Date ngayKetThuc;
	private Integer soLuocDungMoiNguoi;
	private Integer soLuocDaDung;
	private Integer trangThai;
	
	
	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	public Integer getSoLuocDaDung() {
		return soLuocDaDung;
	}

	public void setSoLuocDaDung(Integer soLuocDaDung) {
		this.soLuocDaDung = soLuocDaDung;
	}

	public Integer getSoLuocDungMoiNguoi() {
		return soLuocDungMoiNguoi;
	}

	public void setSoLuocDungMoiNguoi(Integer soLuocDungMoiNguoi) {
		this.soLuocDungMoiNguoi = soLuocDungMoiNguoi;
	}

	public VoucherView() {
		
	}
	public VoucherView(int id,String voucherName, String maVoucher, Date ngayBatDau, Date ngayKetThuc, int loaiVoucher,
			float giaTriGiam, Integer tongLuocDung,Integer soLuocMoiNguoi,Integer soLuocDaDung, Long soLuongSanPhamApDung) {
		this.id=id;
		this.voucherName = voucherName;
		this.maVoucher = maVoucher;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.loaiVoucher = loaiVoucher;
		this.giaTriGiam = giaTriGiam;
		this.tongLuocDung = tongLuocDung;
		this.soLuongSanPhamApDung = soLuongSanPhamApDung;
		this.soLuocDungMoiNguoi=soLuocMoiNguoi;
		this.soLuocDaDung=soLuocDaDung;
	}
	
	public VoucherView(int id,String voucherName, String maVoucher, Date ngayBatDau, Date ngayKetThuc, int loaiVoucher,
			float giaTriGiam, int tongLuocDung,int soLuocMoiNguoi,int soLuocDaDung, Long soLuongSanPhamApDung,Integer trangThai) {
		this.id=id;
		this.voucherName = voucherName;
		this.maVoucher = maVoucher;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.loaiVoucher = loaiVoucher;
		this.giaTriGiam = giaTriGiam;
		this.tongLuocDung = tongLuocDung;
		this.soLuongSanPhamApDung = soLuongSanPhamApDung;
		this.soLuocDungMoiNguoi=soLuocMoiNguoi;
		this.soLuocDaDung=soLuocDaDung;
		this.trangThai=trangThai;
	}
	
	public VoucherView(int id,String voucherName, String maVoucher, Date ngayBatDau, Date ngayKetThuc, int loaiVoucher,
			float giaTriGiam, int tongLuocDung, Long soLuongSanPhamApDung) {
		this.id=id;
		this.voucherName = voucherName;
		this.maVoucher = maVoucher;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.loaiVoucher = loaiVoucher;
		this.giaTriGiam = giaTriGiam;
		this.tongLuocDung = tongLuocDung;
		this.soLuongSanPhamApDung = soLuongSanPhamApDung;
//		setSoLuocDungMoiNguoi(soLuocMoiNguoi);
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
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
	public int getLoaiVoucher() {
		return loaiVoucher;
	}
	public void setLoaiVoucher(int loaiVoucher) {
		this.loaiVoucher = loaiVoucher;
	}
	public float getGiaTriGiam() {
		return giaTriGiam;
	}
	public void setGiaTriGiam(float giaTriGiam) {
		this.giaTriGiam = giaTriGiam;
	}
	public int getTongLuocDung() {
		return tongLuocDung;
	}
	public void setTongLuocDung(int tongLuocDung) {
		this.tongLuocDung = tongLuocDung;
	}
	public long getSoLuongSanPhamApDung() {
		return soLuongSanPhamApDung;
	}
	public void setSoLuongSanPhamApDung(int soLuongSanPhamApDung) {
		this.soLuongSanPhamApDung = soLuongSanPhamApDung;
	}
	private int loaiVoucher;
	private float giaTriGiam;
	private int tongLuocDung;
	private long soLuongSanPhamApDung;
}
