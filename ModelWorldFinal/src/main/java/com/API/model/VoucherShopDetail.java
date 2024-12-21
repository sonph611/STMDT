package com.API.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Voucher_SanPham")
public class VoucherShopDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="voucher_id")
	private VoucherShop voucher;
	@ManyToOne
	@JoinColumn(name="sanPham_id")
	private Product product;
	
	
	public VoucherShopDetail() {
		
	}
	

	
	public VoucherShopDetail(int id,VoucherShop v) {
		product=new Product();
		this.voucher=v;
		this.getProduct().setId(id);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@JsonIgnore
	public VoucherShop getVoucher() {
		return voucher;
	}
	public void setVoucher(VoucherShop voucher) {
		this.voucher = voucher;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
}
