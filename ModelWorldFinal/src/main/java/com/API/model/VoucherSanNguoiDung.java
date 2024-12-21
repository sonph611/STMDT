package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "VoucherSanNguoiDung")
public class VoucherSanNguoiDung {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "accountId")
	private Account account;
	@ManyToOne
	@JoinColumn(name = "voucherSanId")
	private VoucherSan voucherSan;
	private int soLuocDaDung;
	
	public VoucherSanNguoiDung() {
		
	}
	
	public VoucherSanNguoiDung(Integer id ,Integer soLuocDaDung,VoucherSan voucherSan) {
		this.id=id;
		this.soLuocDaDung=soLuocDaDung;
		this.voucherSan=voucherSan;
	}
	
	public VoucherSanNguoiDung(Integer voucherId,Account accountId) {
		this.voucherSan=new VoucherSan(voucherId);
		this.account=accountId;
		this.soLuocDaDung=0;
	}
	
	public VoucherSanNguoiDung(Integer soLuoc,VoucherSan vs, Account a) {
		this.soLuocDaDung=soLuoc;
		this.voucherSan=vs;
		this.account=a;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public VoucherSan getVoucherSan() {
		return voucherSan;
	}
	public void setVoucherSan(VoucherSan voucherSan) {
		this.voucherSan = voucherSan;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public int getSoLuocDaDung() {
		return soLuocDaDung;
	}
	public void setSoLuocDaDung(int soLuocDaDung) {
		this.soLuocDaDung = soLuocDaDung;
	}
}
