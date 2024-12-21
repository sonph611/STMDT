package com.API.model;

import java.util.Objects;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "SanPham")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Product {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int id;
	@NotNull(message = "Tên sản phẩm không được trống !!")
	@Length(min = 10,max = 150,message = "Tên sản phẩm phải lớn hơn 10 ký tự và nhỏ hơn 150")
	private String tenSanPham;
	@Min(value = 0,message = "Số lượng sản phẩm không được phép nhỏ hơn 0 !!")
	private int soLuong;
	private String moTa;
	@NotBlank(message = "Vui lòng chọn 1 ảnh đại diện cho sản phẩm")
	private String hinhAnh;
	private int trangThai;
	private String video;
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	private Set<VoucherShopDetail> voucherDetails;
	@Valid
//	@JsonManagedReference
	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private Set<ProductDetail> productDetails;
	
	private Integer duocMuaKhiHetHang;
	
	public Integer getDuocMuaKhiHetHang() {
		return duocMuaKhiHetHang;
	}
	public void setDuocMuaKhiHetHang(Integer duocMuaKhiHetHang) {
		this.duocMuaKhiHetHang = duocMuaKhiHetHang;
	}
	private Long soLuongDaBan;

	public Long getSoLuongDaban() {
		return soLuongDaBan;
	}
	public void setSoLuongDaban(Long soLuongDaban) {
		this.soLuongDaBan = soLuongDaban;
	}
	@ManyToOne
	@JoinColumn(name = "cuaHangId")
	private Shop shop;
	
	@NotNull(message = "Vui lòng chọn thương hiệu cho sản phẩm !!!")
	@ManyToOne
	@JoinColumn(name = "thuongHieuId")
	private ThuongHieu thuongHieu;
	@ManyToOne
	@NotNull(message = "Vui lòng chọn mặt hàng cho sản phẩm !!!")
	@JoinColumn(name = "theLoaiId")
	private Category category;
	
	
	// THÊM MỚI:
	public Product(Integer id,String tenSanPham,String hinhAnh,Long soLuongDaBan,Integer trangThai) {
		this.id=id;
		this.tenSanPham=tenSanPham;
		this.hinhAnh=hinhAnh;
		this.soLuongDaBan=soLuongDaBan;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Transient
//	private String spacePrice;
//	
//	
//	public Product (Integer id,String tenSanPham,String spacePrice,Integer soLuong) {
//		this.id=id;
//		this.tenSanPham=tenSanPham;
//		this.spacePrice=spacePrice;
//	}
//	
	public Product(Integer a) {
		this.id=a;
	}
	@JsonIgnore
	public Set<VoucherShopDetail> getVoucherDetails() {
		return voucherDetails;
	}

	
	public Product(Integer id ,String tenSanPham) {
		this.id=id;
		this.tenSanPham=tenSanPham;
	}
	
	
	public void setVoucherDetails(Set<VoucherShopDetail> voucherDetails) {
		this.voucherDetails = voucherDetails;
	}
	
	public Product (Integer id ,String tenSanPham,String hinhAnh,Integer trangThai) {
		this.id=id;
		this.tenSanPham=tenSanPham;
		this.hinhAnh=hinhAnh;
		this.trangThai=trangThai;
	}
	
	public Product (Integer id ,String tenSanPham,String hinhAnh) {
		this.id=id;
		this.tenSanPham=tenSanPham;
		this.hinhAnh=hinhAnh;
	}

	public Product(int id,String productName,int soLuong) {
		this.id=id;
		this.tenSanPham=productName;
		this.soLuong=soLuong;
	}
	
	public Product(int id,String productName,int soLuong,Set<ProductDetail>l) {
		this.id=id;
		this.tenSanPham=productName;
		this.soLuong=soLuong;
		this.productDetails=l;
	}
    public interface AgeView {}
	
	public Product(int id ) {
		this.id=id;
	}
	public Product() {
		
	}
	
	public java.util.List<ProductImage> getProductImages() {
		return productImages;
	}
	public void setProductImages(java.util.List<ProductImage> productImages) {
		this.productImages = productImages;
	}
	public Set<ProductDetail> getProductDetails() {
		return productDetails;
	}
	public void setProductDetails(Set<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}
	
	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
	private java.util.List<ProductImage> productImages;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public String getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	public int getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public ThuongHieu getThuongHieu() {
		return thuongHieu;
	}
	public void setThuongHieu(ThuongHieu thuongHieu) {
		this.thuongHieu = thuongHieu;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

}
