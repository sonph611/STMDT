package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ThongBao")
public class ThongBao {
	
	
	private static String thongThuong="THONGTHUONG";
	private static String uuDai="UUDAI";
	public static String ORDER="ORDER";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String tieuDe;
	private String noiDung;
	private String link;
	private String hinhAnh;
	private String loaiThongBao;
	@ManyToOne()
	@JoinColumn(name="nguoiGui_Id")
	private Account account;
	
	public ThongBao() {
		
	}
	
	public ThongBao(String tieuDe, String noiDung, String link, String hinhAnh, String loaiThongBao, Account account) {
		super();
		this.tieuDe = tieuDe;
		this.noiDung = noiDung;
		this.link = link;
		this.hinhAnh = hinhAnh;
		this.loaiThongBao = loaiThongBao;
		this.account = account;
	}
	
	
	public static String getThongThuong() {
		return thongThuong;
	}
	public static void setThongThuong(String thongThuong) {
		ThongBao.thongThuong = thongThuong;
	}
	public static String getUuDai() {
		return uuDai;
	}
	public static void setUuDai(String uuDai) {
		ThongBao.uuDai = uuDai;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTieuDe() {
		return tieuDe;
	}
	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}
	public String getNoiDung() {
		return noiDung;
	}
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	public String getLoaiThongBao() {
		return loaiThongBao;
	}
	public void setLoaiThongBao(String loaiThongBao) {
		this.loaiThongBao = loaiThongBao;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	
}
