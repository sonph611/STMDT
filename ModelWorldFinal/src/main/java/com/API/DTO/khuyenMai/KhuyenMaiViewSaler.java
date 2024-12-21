package com.API.DTO.khuyenMai;

import java.util.Date;
import java.util.List;


public class KhuyenMaiViewSaler {
	private int id;
	private String tenKhuyenMai;
	private Date ngayBatDau;
	private Date ngayKetThuc;
	private float giaTriGiam;
//	private Ineteger loaiKhuyenMai;
	private Integer trangThai;
	public Integer getTrangThai() {
		return trangThai;
	}





	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}
	private List<KhuyenMaiItem>items;
	public List<KhuyenMaiItem> getItems() {
		return items;
	}





	public void setItems(List<KhuyenMaiItem> items) {
		this.items = items;
	}
	private String[] images;
	
	
	
	public KhuyenMaiViewSaler(String s) {
		
		super();
	}
	
	
	
	
	
	public KhuyenMaiViewSaler() {
		super();
	}
	public KhuyenMaiViewSaler(int id, String tenKhuyenMai, Date ngayBatDau, Date ngayKetThuc,
			float giaTriGiam, Object images) {
		super();
		this.id = id;
		this.tenKhuyenMai = tenKhuyenMai;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.giaTriGiam = giaTriGiam;
		this.images =((String) images).split("-");
	}
	
	public KhuyenMaiViewSaler(Integer trangThai,int id, String tenKhuyenMai, Date ngayBatDau, Date ngayKetThuc,
			float giaTriGiam, Object images) {
		super();
		this.id = id;
		this.trangThai=trangThai;
		this.tenKhuyenMai = tenKhuyenMai;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.giaTriGiam = giaTriGiam;
		this.images =((String) images).split("-");
	}
	
	public KhuyenMaiViewSaler(int id, String tenKhuyenMai, Date ngayBatDau, Date ngayKetThuc,
			float giaTriGiam) {
		super();
		this.id = id;
		this.tenKhuyenMai = tenKhuyenMai;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.giaTriGiam = giaTriGiam;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTenKhuyenMai() {
		return tenKhuyenMai;
	}
	public void setTenKhuyenMai(String tenKhuyenMai) {
		this.tenKhuyenMai = tenKhuyenMai;
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
	public float getGiaTriGiam() {
		return giaTriGiam;
	}
	public void setGiaTriGiam(float giaTriGiam) {
		this.giaTriGiam = giaTriGiam;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	
}

