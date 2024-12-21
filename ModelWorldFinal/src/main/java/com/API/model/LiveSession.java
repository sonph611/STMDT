package com.API.model;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "livesession")
public class LiveSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull(message = "Vui lòng nhập tiêu đề live")
	@Length(min = 5,message = "Tối thiểu 5 ký tự")
	private String tieuDe;
	private String moTa;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	@Column(name="likeCount")
	private Integer countLike;
	private Integer cartCount;
	private Integer orderCount;
	
	public LiveSession(String tieuDe,Integer shopId,String shopName,String hinhAnh) {
		this.tieuDe=tieuDe;
		this.shop=new Shop(shopId, shopName, hinhAnh, 1);
	}
	public LiveSession(Integer id,String tieuDe,String hinhAnhLive,Integer shopId,String shopName,String hinhAnh) {
		this.tieuDe=tieuDe;
		this.id=id;
		this.hinhAnh=hinhAnh;
		this.shop=new Shop(shopId, shopName, hinhAnh, 1);
	}
	
	public LiveSession( ) { 
	}
	
	public LiveSession(Integer id  ) {
		this.id=id;
	}
	
//	@NotNull(message = "Vui longgf chọn hình ảnh cho phiên live")
	private String hinhAnh;
	@ManyToOne
	@JoinColumn(name="shopId")
	private Shop shop;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTieuDe() {
		return tieuDe;
	}
	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
//	public Integer getTrangThai() {
//		return trangThai;
//	}
//	public void setTrangThai(Integer trangThai) {
//		this.trangThai = trangThai;
//	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getCountLike() {
		return countLike;
	}
	public void setCountLike(Integer countLike) {
		this.countLike = countLike;
	}
	public Integer getCartCount() {
		return cartCount;
	}
	public void setCartCount(Integer cartCount) {
		this.cartCount = cartCount;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public String getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}
