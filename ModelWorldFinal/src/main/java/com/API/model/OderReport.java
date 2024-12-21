package com.API.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reportorder")
public class OderReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer orderId;
	private Integer accountId;
	private Integer trangThai;
	private LocalDateTime ngayReport;
	private String noiDung;
	private String hinhAnh;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}
	public LocalDateTime getNgayReport() {
		return ngayReport;
	}
	public void setNgayReport(LocalDateTime ngayReport) {
		this.ngayReport = ngayReport;
	}
	public String getNoiDung() {
		return noiDung;
	}
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	public String getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	public OderReport(Integer id, Integer orderId, Integer accountId, Integer trangThai, LocalDateTime ngayReport,
			String noiDung, String hinhAnh) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.accountId = accountId;
		this.trangThai = trangThai;
		this.ngayReport = ngayReport;
		this.noiDung = noiDung;
		this.hinhAnh = hinhAnh;
	}
	public OderReport() {

	}

	
	

}
