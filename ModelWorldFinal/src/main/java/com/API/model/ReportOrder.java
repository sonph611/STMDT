package com.API.model;

import java.util.Date;

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
@Table(name = "reportorder")
public class ReportOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id ;
	@ManyToOne
	@JoinColumn(name = "accountId")
	private Account account;
	@NotNull(message = "Vui lòng chọn order để report")
	@ManyToOne
	@JoinColumn(name = "orderId")
	private Order order;
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
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
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
	public Date getNgayReport() {
		return ngayReport;
	}
	public void setNgayReport(Date ngayReport) {
		this.ngayReport = ngayReport;
	}
	@NotNull(message = "Chưa nhập nội dung report")
	@NotBlank(message = "Nội dung report không được phép rỗng")
	private String noiDung;
	private String hinhAnh;
	private Date ngayReport;
}
