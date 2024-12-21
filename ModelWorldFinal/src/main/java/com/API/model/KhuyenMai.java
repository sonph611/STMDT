package com.API.model;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "Vui lòng nhập tên khuyến mãi")
	private String tenKhuyenMai;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "thoiGianBD")
	private java.util.Date ngayBatDau;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "thoiGianKT")
	private java.util.Date ngayKetThuc;
	@OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL)
	@Size(min = 0, message = "Số lượng sản phẩm áp dụng phải lớn hơn 0 !!!")
	private Set<ChiTietKhuyenMai> chitietKhuyenMai;
	private Integer trangThai;
	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	@ManyToOne
	@JoinColumn(name = "cuaHangId")
	private Shop shop;
	@Max(value = 99, message = "Giá trị phải nhỏ hơn 100 !!!")
	@Min(value = 1, message = "Giá trị phải lớn hơn 0 !!!")
	private Integer giaTriKhuyenMai;
	
	@Min(value = 0, message = "Số lượng tối thiểu phải lớn hơn bằng 0")
	private Integer soLuongToiThieu;

	public Integer getSoLuongToiThieu() {
		return soLuongToiThieu;
	}

	public void setSoLuongToiThieu(Integer soLuongToiThieu) {
		this.soLuongToiThieu = soLuongToiThieu;
	}

	public void setGiaTriKhuyenMai(Integer giaTriKhuyenMai) {
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}

	public KhuyenMai() {

	}

	public KhuyenMai(int id, String tenKhuyenMai, java.util.Date ngayBatDau, java.util.Date ngayKetThuc,
			int giaTriKhuyenMai) {
		this.id = id;
		this.tenKhuyenMai = tenKhuyenMai;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}

	public KhuyenMai(String tenKhuyenMai, java.util.Date ngayBatDau, java.util.Date ngayKetThuc, int giaTriKhuyenMai) {
		this.tenKhuyenMai = tenKhuyenMai;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}

	public Set<ChiTietKhuyenMai> getChitietKhuyenMai() {
		return chitietKhuyenMai;
	}

	public void setChitietKhuyenMai(Set<ChiTietKhuyenMai> chitietKhuyenMai) {
		this.chitietKhuyenMai = chitietKhuyenMai;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTenKhuyenMai() {
		return tenKhuyenMai;
	}

	public void setTenKhuyenMai(String tenKhuyenMai) {
		this.tenKhuyenMai = tenKhuyenMai;
	}

	public java.util.Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(java.util.Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public java.util.Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(java.util.Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public boolean validDate() {
		LocalDateTime startDateTime = ngayBatDau.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime endDateTime = ngayKetThuc.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		Duration duration = Duration.between(startDateTime, endDateTime);
		double durationInHours = duration.toHours();
		return durationInHours >= 1;
//		return true;
	}

//	public boolean lonHonHienTai() {
//		return ngayBatDau.after(new java.util.Date());
//        LocalDateTime dateToCompareLocalDateTime = ngayBatDau.toInstant()
//                .atZone(ZoneId.systemDefault()).toLocalDateTime();
//        return  dateToCompareLocalDateTime.is
//	}

	public int getGiaTriKhuyenMai() {
		return giaTriKhuyenMai;
	}

	public void setGiaTriKhuyenMai(int giaTriKhuyenMai) {
		this.giaTriKhuyenMai = giaTriKhuyenMai;
	}

}
