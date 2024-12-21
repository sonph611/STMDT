package com.API.model;

import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "voucherSan")
public class VoucherSan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String tenVoucher;
	private String maVoucher;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayBatDau;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayKetThuc;
	private Integer loaiVoucher;
	private Integer soLuocDung;
	private float giaTriGiam;
	private Integer hinhThucApDung;
	private float gioiHanGiam;
	private double donToiThieu;
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
	private Integer soLuocDungMoiNguoi;
	
	public Integer getSoLuocDungMoiNguoi() {
		return soLuocDungMoiNguoi;
	}

	public void setSoLuocDungMoiNguoi(Integer soLuocDungMoiNguoi) {
		this.soLuocDungMoiNguoi = soLuocDungMoiNguoi;
	}

	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public double getDonToiThieu() {
		return donToiThieu;
	}

	public void setDonToiThieu(double donToiThieu) {
		this.donToiThieu = donToiThieu;
	}

	public VoucherSan(int id,int loaiVoucher,float giaTriGiam,float giamToiDa,int hinhThucGiam,double donToiThieu) {
		this.id=id;
		this.loaiVoucher=loaiVoucher;
		this.giaTriGiam=  giaTriGiam;
		this.gioiHanGiam=giamToiDa;
		this.hinhThucApDung=hinhThucGiam;
		this.donToiThieu=donToiThieu;
	}
	
	
	public void calFee(List<Order> o) {
		 float discountFactor = (loaiVoucher == 0) ? giaTriGiam : giaTriGiam / 100;
		if(hinhThucApDung==1) {
			if(loaiVoucher==0) {
				o.forEach(v->{
					v.calTongTien(discountFactor);
				});
			}else {
				o.forEach(v->{
					float a=(float) (discountFactor*(v.getTongTien()-v.tienTru));
					v.calTongTien((float) (a>this.gioiHanGiam&&this.gioiHanGiam!=0? this.gioiHanGiam:a));
				});
			}
		}else { 
			if(loaiVoucher==0) {
				o.forEach(v->{
					v.calTongTienShip(discountFactor);
				});
			}else {
				o.forEach(v->{
					v.calTongTienShip(discountFactor*v.getPhiShip());
				});
			}
		}
	}
	
	
	public VoucherSan() {
		
	}
	public VoucherSan(Integer id ) {
		this.id=id;
		
	}
	
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
	public Date getNngayKetThuc() {
		return ngayKetThuc;
	}
	public void setNngayKetThuc(Date nngayKetThuc) {
		this.ngayKetThuc = nngayKetThuc;
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
}
