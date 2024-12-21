package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name="gioHang")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne()
	@JoinColumn(name = "sanPhamId")
	private ProductDetail product;
	@ManyToOne()
	@JoinColumn(name = "taiKhoanId")
	private Account account;
	@Min(value = 1,message = "Số lượng sản phẩm đặt ko được nhỏ hơn 1")
	private int soLuong;
	
	

	
	public Cart(int productId,int accountId,int soLuong) {
		this.product = new ProductDetail(productId);
		this.account=new Account(accountId);
		this.soLuong = soLuong;
	}
	
	
	
	public Cart(int id,int soLuong) {
		this.id=id;
		this.soLuong = soLuong;
	}
	
	public Cart() {
		this.id=null;
	}
	public Cart(int id, ProductDetail product, Account account, int soLuong) {
		this.id = id;
		this.product = product;
		this.account = account;
		this.soLuong = soLuong;
	}
	
	
	
//	public Cart(Integer id, Integer product, Integer account, Integer soLuong) {
//		this.id = id;
//		this.product = new ProductDetail(product);
//		this.account = new Account(account);
//		this.soLuong = soLuong;
//	}
	
	public Cart(Integer id, Integer product, Integer account, Integer soLuong) {
		this.id = id;
		this.product = new ProductDetail(product);
		this.account = new Account(account);
		this.soLuong = soLuong;
	}
	
	public void plusSoLuong(int sl) {
		this.soLuong+=sl;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ProductDetail getProduct() {
		return product;
	}
	public void setProduct(ProductDetail product) {
		this.product = product;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
}
