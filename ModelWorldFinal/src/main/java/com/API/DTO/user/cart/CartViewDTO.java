package com.API.DTO.user.cart;

import com.API.model.KhuyenMai;
import com.API.model.KichThuoc;
import com.API.model.Shop;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CartViewDTO {
	
	private Integer id;
	private String shopName;
	@JsonIgnore
	private Integer shopId;
	private String tenSanPham;
	private Float ProductGiaBan=(float) 0;
	private Integer productSoLuong=0;
	private Integer giaTriKhuyenMai;
	private Integer sanPhamSoLuong;
	private String hinhAnh;
	private String tenBienThe;
	private Integer sanPhamId;
	private Integer liveId;
	private Double giaGiam;
	private Integer soLuongGioHan;
	private Integer soLuocDaDung;
	private Shop shop;
//	@JsonIgnore

	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public Integer getSoLuocDaDung() {
		return soLuocDaDung;
	}
	public void setSoLuocDaDung(Integer soLuocDaDung) {
		this.soLuocDaDung = soLuocDaDung;
	}
	public Float getProductGiaBan() {
		return ProductGiaBan;
	}
	public void setProductGiaBan(Float productGiaBan) {
		ProductGiaBan = productGiaBan;
	}
	public Integer getProductSoLuong() {
		return productSoLuong;
	}
	public void setProductSoLuong(Integer productSoLuong) {
		this.productSoLuong = productSoLuong;
	}
	public Integer getLiveId() {
		return liveId;
	}
	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}
	public Double getGiaGiam() {
		return giaGiam;
	}
	public void setGiaGiam(Double giaGiam) {
		this.giaGiam = giaGiam;
	}
	public Integer getSoLuongGioHan() {
		return soLuongGioHan;
	}
	public void setSoLuongGioHan(Integer soLuongGioHan) {
		this.soLuongGioHan = soLuongGioHan;
	}
	public Integer getSanPhamId() {
		return sanPhamId;
	}
	public void setSanPhamId(Integer sanPhamId) {
		this.sanPhamId = sanPhamId;
	}
	public Integer getId() {
		return id;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public int getSoLuong() {
		return productSoLuong;
	}
	public void setSoLuong(int soLuong) {
		this.productSoLuong = soLuong;
	}
	public Integer getGiaTriKhuyenMai() {
		return giaTriKhuyenMai;
	}
	public void setGiaTriKhuyenMai(Integer giaTriKhuyenMai) {
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}
	public Integer getSanPhamSoLuong() {
		return sanPhamSoLuong;
	}
	public void setSanPhamSoLuong(Integer sanPhamSoLuong) {
		this.sanPhamSoLuong = sanPhamSoLuong;
	}
	public String getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	public String getTenBienThe() {
		return tenBienThe;
	}
	public void setTenBienThe(String tenBienThe) {
		this.tenBienThe = tenBienThe;
	}
	public Float getGiaBan() {
		return ProductGiaBan;
	}
	public void setGiaBan(Float giaBan) {
		this.ProductGiaBan = giaBan;
	}
	public CartViewDTO(Integer id,String tenSanPham, Integer soLuong, Integer giaTriKhuyenMai,
			Integer sanPhamSoLuong, String hinhAnh, String tenBienThe, Float giaBan) {
		this.id = id;
		this.tenSanPham = tenSanPham;
		this.productSoLuong = soLuong;
		this.giaTriKhuyenMai = giaTriKhuyenMai;
		this.sanPhamSoLuong = sanPhamSoLuong;
		this.hinhAnh = hinhAnh;
		this.tenBienThe = tenBienThe;
		this.ProductGiaBan = giaBan;
	}
	
	public CartViewDTO(Integer id,Integer soLuong,Float giaBan,String tenSanPham,int soLuongSanPham,String hinhAnh,String tenBienThe,
			Integer shopId,String shopName,Integer giaTriGiam,
			Integer productId,Integer liveId,Double giaGiam,Integer soLuongGioiHan) {
			this.id = id;
			this.productSoLuong= soLuong;
			this.ProductGiaBan=giaBan;
			this.tenSanPham=tenSanPham;
			this.sanPhamSoLuong=soLuongSanPham;
			this.hinhAnh=hinhAnh;
			this.tenBienThe=tenBienThe;
			this.shopId=shopId;
			this.shopName=shopName;
			this.giaTriKhuyenMai=giaTriGiam==null?0:giaTriGiam;
			this.sanPhamId=productId;
			this.liveId=liveId;
			this.giaGiam=giaGiam;
			this.soLuongGioHan=soLuongGioiHan;
	
	}
	
	public CartViewDTO(Integer id,Integer soLuong,Float giaBan,String tenSanPham,int soLuongSanPham,String hinhAnh,String tenBienThe,
			Integer shopId,String shopName,Integer giaTriGiam,
			Integer productId,Integer liveId,Double giaGiam,Integer soLuongGioiHan,Integer soLuoncDaDung) {
			this.id = id;
			this.productSoLuong= soLuong;
			this.ProductGiaBan=giaBan;
			this.tenSanPham=tenSanPham;
			this.sanPhamSoLuong=soLuongSanPham;
			this.hinhAnh=hinhAnh;
			this.tenBienThe=tenBienThe;
			this.shopId=shopId;
			this.shopName=shopName;
			this.giaTriKhuyenMai=giaTriGiam==null?0:giaTriGiam;
			this.sanPhamId=productId;
			this.liveId=liveId;
			this.giaGiam=giaGiam;
			this.soLuongGioHan=soLuongGioiHan;
			this.soLuocDaDung=soLuoncDaDung;
	
	}
	public CartViewDTO(Shop s,Integer id,Integer soLuong,Float giaBan,String tenSanPham,int soLuongSanPham,String hinhAnh,String tenBienThe,
			Integer shopId,String shopName,Integer giaTriGiam,
			Integer productId,Integer liveId,Double giaGiam,Integer soLuongGioiHan,Integer soLuoncDaDung) {
			this.id = id;
			this.shop=s;
			this.productSoLuong= soLuong;
			this.ProductGiaBan=giaBan;
			this.tenSanPham=tenSanPham;
			this.sanPhamSoLuong=soLuongSanPham;
			this.hinhAnh=hinhAnh;
			this.tenBienThe=tenBienThe;
			this.shopId=shopId;
			this.shopName=shopName;
			this.giaTriKhuyenMai=giaTriGiam==null?0:giaTriGiam;
			this.sanPhamId=productId;
			this.liveId=liveId;
			this.giaGiam=giaGiam;
			this.soLuongGioHan=soLuongGioiHan;
			this.soLuocDaDung=soLuoncDaDung;
	
	}
//	(p.id,p.soLuong,pd.giaBan,"
//			+ "p.tenSanPham,pd.soLuong,pd.hinhAnh,CONCAT(ms.tenMau,' - ', k.tenKichThuoc)"
//			+ ",s.shopId,s.shopName,km.giaTriKhuyenMai)	
	public CartViewDTO(Integer id,Integer soLuong,Float giaBan,String tenSanPham,int soLuongSanPham,String hinhAnh,String tenBienThe,
			Integer shopId,String shopName,Integer giaTriGiam,
			Integer productId) {
		this.id = id;
		this.productSoLuong= soLuong;
		this.ProductGiaBan=giaBan;
		this.tenSanPham=tenSanPham;
		this.sanPhamSoLuong=soLuongSanPham;
		this.hinhAnh=hinhAnh;
		this.tenBienThe=tenBienThe;
		this.shopId=shopId;
		this.shopName=shopName;
		this.giaTriKhuyenMai=giaTriGiam==null?0:giaTriGiam;
		this.sanPhamId=productId;
	
	}
	
	public CartViewDTO() {
	}
	
}
