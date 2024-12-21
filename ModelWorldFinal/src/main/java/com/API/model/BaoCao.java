package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "BaoCao")
public class BaoCao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull(message = "Vui lòng cung cấp ít nhất một hình ảnh")
	private String hinhAnhDanChung;
	@NotNull
	@NotNull(message = "Vui lòng ghi rõ nội dung báo cáo vi phạm")
	@NotBlank(message="Không được để trống nội dung vi phậm")
	private String noiDungViPham;
	@ManyToOne()
	@NotNull( message = "Chưa chọn lỗi vi phạm")
	@JoinColumn(name = "viPham_id")
	private ViPham viPham;
	@ManyToOne()
	@JoinColumn(name = "taiKhoan_id")
	private Account account;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHinhAnhDanChung() {
		return hinhAnhDanChung;
	}
	public void setHinhAnhDanChung(String hinhAnhDanChung) {
		this.hinhAnhDanChung = hinhAnhDanChung;
	}
	public String getNoiDungViPham() {
		return noiDungViPham;
	}
	public void setNoiDungViPham(String noiDungViPham) {
		this.noiDungViPham = noiDungViPham;
	}
	public ViPham getViPham() {
		return viPham;
	}
	public void setViPham(ViPham viPham) {
		this.viPham = viPham;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
}
