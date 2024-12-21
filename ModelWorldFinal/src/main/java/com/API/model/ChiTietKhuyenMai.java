package com.API.model;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chiTietKhuyenMai")
public class ChiTietKhuyenMai {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "khuyenMaiId")
	private KhuyenMai khuyenMai;
	private int limitCount;
	public int getLimit() {
		return limitCount;
	}

	// id , productId,tenSanPham
//	public ChiTietKhuyenMai() {
//		
//	}
	
	
//	public ChiTietKhuyenMai(int id,int id product) {
//		
//	}
	
	public void setLimit(int limit) {
		this.limitCount = limit;
	}
	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;
	public ChiTietKhuyenMai() {
			
	}
	   @Override
	    public boolean equals(Object obj) {
	        ChiTietKhuyenMai that = (ChiTietKhuyenMai) obj;
	        return ( product.getId() == that.product.getId());
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash( product.getId());
	    }
	
    public ChiTietKhuyenMai( Integer id ,String tenKhuyenMai,Date ngayBatDau,Date ngayKetThuc,Integer giatri,Integer productId) {
		this.khuyenMai=new KhuyenMai(id,tenKhuyenMai, ngayBatDau, ngayKetThuc,giatri);
		this.product=new Product(productId);
	}
	    
	public ChiTietKhuyenMai(KhuyenMai k,Product p,int limit) {
		this.khuyenMai=k;
		this.product=p;
		this.limitCount=limit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}
	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
