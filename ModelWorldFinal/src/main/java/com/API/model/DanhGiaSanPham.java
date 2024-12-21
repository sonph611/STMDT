package com.API.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name="danhGiaSanPham")
public class DanhGiaSanPham {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Min(value = 1,message = "Số sao đánh giá vui lòng lớn hơn 1")
	@Max(value = 5,message = "Số sao đánh giá vui lòng nhỏ hơn 5")
	private Integer soSao;
	private String noiDungDanhGia;
	@ManyToOne
	@JoinColumn(name = "taiKhoan_Id")
	private Account account;
	private Date ngayDanhGia;
	@ManyToOne
	@JoinColumn(name = "chiTietDonHang_id")
	private OrderDetail orderDetail;
	
	@Transient
	private Integer productId;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public DanhGiaSanPham() {
		
	}
	
public DanhGiaSanPham(Integer id ) {
		this.id=id;
	}
	
	public DanhGiaSanPham(Integer id,Integer soSao,String noiDungDanhGia,String taiKhoanId,String tenTaiKhoan,Date ngayDanhGia,String shopPhanHoi,Integer productId) {
		this.id=id;
		this.soSao=soSao;
		this.noiDungDanhGia=noiDungDanhGia;
		this.account=new Account(taiKhoanId,tenTaiKhoan);
		this.ngayDanhGia=ngayDanhGia;
		this.shopPhanHoi=shopPhanHoi;
		this.productId=productId;
	}
	public DanhGiaSanPham(String hinhAnh,Integer id,Integer soSao,String noiDungDanhGia,String taiKhoanId,String tenTaiKhoan,Date ngayDanhGia,String shopPhanHoi,Integer productId) {
		this.id=id;
		this.soSao=soSao;
		this.noiDungDanhGia=noiDungDanhGia;
		this.account=new Account(taiKhoanId,tenTaiKhoan,hinhAnh);
		this.ngayDanhGia=ngayDanhGia;
		this.shopPhanHoi=shopPhanHoi;
		this.productId=productId;
	}
	
	
	

	public List<ChiTietDanhGia> getChiTietDanhGias() {
		return chiTietDanhGias;
	}

	public void setChiTietDanhGias(List<ChiTietDanhGia> chiTietDanhGias) {
		this.chiTietDanhGias = chiTietDanhGias;
	}

	@OneToMany(mappedBy = "danhGiaSanPham")
	
	private List<ChiTietDanhGia>chiTietDanhGias;
	
	public OrderDetail getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}
	private String shopPhanHoi;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSoSao() {
		return soSao;
	}
	public void setSoSao(Integer soSao) {
		this.soSao = soSao;
	}
	public String getNoiDungDanhGia() {
		return noiDungDanhGia;
	}
	public void setNoiDungDanhGia(String noiDungDanhGia) {
		this.noiDungDanhGia = noiDungDanhGia;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Date getNgayDanhGia() {
		return ngayDanhGia;
	}
	public void setNgayDanhGia(Date ngayDanhGia) {
		this.ngayDanhGia = ngayDanhGia;
	}
	public String getShopPhanHoi() {
		return shopPhanHoi;
	}
	public void setShopPhanHoi(String shopPhanHoi) {
		this.shopPhanHoi = shopPhanHoi;
	}
	
}
