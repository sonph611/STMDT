package com.API.model;

import java.util.Date;

import org.hibernate.annotations.ManyToAny;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sanPhamYeuThich")

public class SanPhamYeuThich {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id ;
	@ManyToOne()
	@JoinColumn(name = "taiKhoan_Id")
	private Account account;
	@ManyToOne()
	@JoinColumn(name = "sanPham_Id")
	private Product product;
	private Date ngayThemYeuThich;
	public SanPhamYeuThich( Account account, Product product) {
		
		this.account = account;
		this.product = product;
		this.ngayThemYeuThich = new Date();
	}
	public SanPhamYeuThich() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account acccount) {
		this.account = acccount;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Date getNgayLuuYeuThich() {
		return ngayThemYeuThich;
	}
	public void setNgayLuuYeuThich(Date ngayLuuYeuThich) {
		this.ngayThemYeuThich = ngayLuuYeuThich;
	}
}
