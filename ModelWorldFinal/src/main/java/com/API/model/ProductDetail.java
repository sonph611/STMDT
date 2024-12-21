package com.API.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ChiTietSanPham")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class ProductDetail {
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	
	public ProductDetail(Product p,String hinhAnh,String tenMau,String tenKichThuoc) {
		this.product=p;
		this.hinhAnh=hinhAnh;
		this.kichThuoc=new KichThuoc(tenKichThuoc);
		this.mauSac=new MauSac(tenMau);
	}
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	@Min(value = 0,message = "Số lượng biến thể không được bé hơn 0")
	private int soLuong;
	@Min(value = 1000,message = "Số lượng biến thể không được bé hơn 1000")
	private float giaBan;
	@Column(name = "hinhAnhBienThe")
	private String hinhAnh;
	@NotNull(message="Vui lòng chọn màu sắc cho biến thể")
	@ManyToOne()
	@JoinColumn(name="mauSacId")
	private MauSac mauSac;
	@NotNull(message="Vui lòng chọn kích thước cho biến thể")
	@ManyToOne()
	@JoinColumn(name="kichThuocId")
	private KichThuoc kichThuoc;
	@Transient
	private Integer isActive;
	
	private Integer trangThai;
	
	
	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}



	@ManyToOne()
	@JoinColumn(name = "sanPhamId")
	private Product product;
	
	public ProductDetail(int soLuong,int isActive) {
		this.soLuong=soLuong;
		this.isActive=isActive;
	}
	
	public ProductDetail(Integer id,Integer soLuong,Float giaBan,String tenMau,String tenKichThuoc,Integer productId) {
		this.id=id;
		this.soLuong=soLuong;
		this.giaBan=giaBan;
		this.mauSac=new MauSac(tenMau);
		this.kichThuoc=new KichThuoc(tenKichThuoc);
		this.product=new Product(productId);
	}
	
	
	public ProductDetail(Product p,Integer id ,String tenMau,String tenKichThuoc) {
		this.id=id;
		this.mauSac=new MauSac(tenMau);
		this.kichThuoc=new KichThuoc(tenKichThuoc);
		this.product=p;
	}
	
	
	public ProductDetail(int id,int soLuong,Object isActive) {
		this.id=id;
		this.soLuong=soLuong;
		this.isActive=(Integer)(isActive);
	}
	
	public ProductDetail() {
	}
	
	public ProductDetail(Integer id,String tenMau,String tenKichThuoc,String hinhAnh,Integer soLuong,Float giaBan) {
		this.id=id;
		this.mauSac=new MauSac(tenMau);
		this.kichThuoc=new KichThuoc(tenKichThuoc);
		this.hinhAnh= hinhAnh;
		this.soLuong=soLuong;
		this.giaBan=giaBan;
	}
	
	
	public ProductDetail(int id) {
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getId()==((ProductDetail)obj).getId();
	}
	
	public boolean equalsSetNew(Object obj) {
		ProductDetail p=((ProductDetail) obj);
		return this.getKichThuoc().getId()==p.getKichThuoc().getId()
				&&this.getMauSac().getId()==p.getMauSac().getId();
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getKichThuoc().getId(),getMauSac().getId());
	}


	public void setId(int id) {
		this.id = id;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public float getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(float giaBan) {
		this.giaBan = giaBan;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public MauSac getMauSac() {
		return mauSac;
	}

	public void setMauSac(MauSac mauSac) {
		this.mauSac = mauSac;
	}

	public KichThuoc getKichThuoc() {
		return kichThuoc;
	}

	public void setKichThuoc(KichThuoc kichThuoc) {
		this.kichThuoc = kichThuoc;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
}
