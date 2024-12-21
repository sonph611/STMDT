package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chiTietThongBao")
public class ChiTietThongBao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne()
	@JoinColumn(name="thongBao_id")
	private ThongBao thongBao;
	@ManyToOne()
	@JoinColumn(name="nguoinhan_id")
	private Account account;
	private Integer TrangThaiDoc;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ThongBao getThongBao() {
		return thongBao;
	}
	public void setThongBao(ThongBao thongBao) {
		this.thongBao = thongBao;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public ChiTietThongBao() {
		
	}
	
	public ChiTietThongBao(Integer id ,ThongBao thongBao,  Integer trangThaiDoc) {
		super();
		this.thongBao = thongBao;
		this.id=id;
		TrangThaiDoc = trangThaiDoc;
	}
	
	public ChiTietThongBao(ThongBao thongBao, Account account, Integer trangThaiDoc) {
		super();
		this.thongBao = thongBao;
		this.account = account;
		TrangThaiDoc = trangThaiDoc;
	}
	public Integer getTrangThaiDoc() {
		return TrangThaiDoc;
	}
	public void setTrangThaiDoc(Integer trangThaiDoc) {
		TrangThaiDoc = trangThaiDoc;
	}
}
