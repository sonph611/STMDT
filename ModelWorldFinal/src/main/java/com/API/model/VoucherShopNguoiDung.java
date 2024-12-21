package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "VoucherCuahangNguoiDung")
public class VoucherShopNguoiDung {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "nguoiDungId")
	private Account account;
	@ManyToOne
	@JoinColumn(name = "vouchercuahangid")
	
	
	
	
	private VoucherShop voucher;
	public VoucherShopNguoiDung() {
		
	}
	
	public VoucherShopNguoiDung(Integer voucherShopId,Account a) {
		this.voucher=new VoucherShop(voucherShopId);
		this.account=a;
		this.soLuocDung=0;
	}
	
	public VoucherShopNguoiDung(Integer soLuocDaDung,VoucherShop voucherShop,Account a ) {
		this.account=a;
		this.soLuocDung=soLuocDaDung;
		this.voucher=voucherShop;
	}
	
	public VoucherShopNguoiDung(Integer id,Integer soLuocDaDung,VoucherShop voucherShop ) {
		this.id=id;
		this.soLuocDung=soLuocDaDung;
		this.voucher=voucherShop;
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
	public void setAccount(Account account) {
		this.account = account;
	}
	public VoucherShop getVoucher() {
		return voucher;
	}
	public void setVoucher(VoucherShop voucher) {
		this.voucher = voucher;
	}
	public Integer getSoLuocDung() {
		return soLuocDung;
	}
	public void setSoLuocDung(Integer soLuocDung) {
		this.soLuocDung = soLuocDung;
	}
	private Integer soLuocDung;
}
