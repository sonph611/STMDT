package com.API.DTO.order;

import java.util.List;
import java.util.Set;

import com.API.model.Cart;
import com.API.model.VoucherShop;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderInsertItem {
	private Set<Integer> cartItems;
	private String diaChi;
	private VoucherShop voucherShop;
	private String loiNhan;
	private float phiShip;
	@JsonIgnore
	private List<Cart> carts;
	
	public OrderInsertItem(Set<Integer> cartItems, String diaChi, VoucherShop voucherShop, String loiNhan,
			float phiShip) {
		super();
		this.cartItems = cartItems;
		this.diaChi = diaChi;
		this.voucherShop = voucherShop;
		this.loiNhan = loiNhan;
		this.phiShip = phiShip;
	}
	
	public List<Cart> getCarts() {
		return carts;
	}
	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}
	public Set<Integer> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Set<Integer> cartItems) {
		this.cartItems = cartItems;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public VoucherShop getVoucherShop() {
		return voucherShop;
	}
	public void setVoucherShop(VoucherShop voucherShop) {
		this.voucherShop = voucherShop;
	}
	public String getLoiNhan() {
		return loiNhan;
	}
	public void setLoiNhan(String loiNhan) {
		this.loiNhan = loiNhan;
	}
	public float getPhiShip() {
		return phiShip;
	}
	public void setPhiShip(float phiShip) {
		this.phiShip = phiShip;
	}
}
