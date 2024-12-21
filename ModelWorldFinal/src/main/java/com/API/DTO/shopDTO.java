package com.API.DTO;

import java.time.LocalDateTime;

public class shopDTO {
    private Integer shopId;
    private String tenShop;
    private String hoVaTen;
    private String anhDaiDien;
    private LocalDateTime ngayDangKy;
    private String trangThai;
    private String soDienThoai;
    private Integer totalOrders;
    private Double totalRevenue;

    public shopDTO(Integer shopId, String tenShop, String hoVaTen, String anhDaiDien, LocalDateTime ngayDangKy,
                   String trangThai, String soDienThoai, Integer totalOrders, Double totalRevenue) {
        this.shopId = shopId;
        this.tenShop = tenShop;
        this.hoVaTen = hoVaTen;
        this.anhDaiDien = anhDaiDien;
        this.ngayDangKy = ngayDangKy;
        this.trangThai = trangThai;
        this.soDienThoai = soDienThoai;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getTenShop() {
		return tenShop;
	}

	public void setTenShop(String tenShop) {
		this.tenShop = tenShop;
	}

	public String getHoVaTen() {
		return hoVaTen;
	}

	public void setHoVaTen(String hoVaTen) {
		this.hoVaTen = hoVaTen;
	}

	public String getAnhDaiDien() {
		return anhDaiDien;
	}

	public void setAnhDaiDien(String anhDaiDien) {
		this.anhDaiDien = anhDaiDien;
	}

	public LocalDateTime getNgayDangKy() {
		return ngayDangKy;
	}

	public void setNgayDangKy(LocalDateTime ngayDangKy) {
		this.ngayDangKy = ngayDangKy;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public Integer getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(Integer totalOrders) {
		this.totalOrders = totalOrders;
	}

	public Double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public shopDTO() {

	}

    
}
