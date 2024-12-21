package com.API.model;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "cuaHang")
public class Shop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int shopId; // rồi
	@Column(name = "tenShop")
	@NotNull(message = "Tên shop không được để trống")
	@Length(min = 5,max = 50,message ="Tên shop vui lòng lớn hón 10 ký tự và nhỏ hơn 50" )
	private String shopName;
	@NotNull(message = "Không được bỏ trống trường email của shop")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email không hợp lệ."
    )
	private String email;
	public String getEmail() {
		return email;
	}

    @OneToOne(mappedBy = "shop")
    private DiaChi diaChi;
	public void setEmail(String email) {
		this.email = email;
	}
	public DiaChi getDiaChi() {
		return diaChi;
	}
	
	public Shop(Integer id, String tenShop, String hinhAnh,Integer trangThai) {
		this.shopId=id;
		this.shopName=tenShop;
		this.hinhAnh=hinhAnh;
		this.trangThai=trangThai;
	}

	public void setDiaChi(DiaChi diaChi) {
		this.diaChi = diaChi;
	}

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "taiKhoan_Id")
	private Account account;
//	private String soDienThoai;
	private String moTa;
	@NotNull(message="Vui lòng chọn ảnh")
	@NotBlank(message="Ảnh ko được phép rỗng")
	private String hinhAnh;
//	@NotNull(message = "Vui lòng nhập mã số thuế")
	private String maSoThue;
//	@NotNull(message="Vui lòng tải giấy phép kinh doanh")
//	@NotBlank(message="Giấy phép kinh doanh ko được phép rỗng")
	private String giayPhepKinhDoanh;
	private Integer trangThai;
	private String tenCongTy;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayDangKy;
	@Min(value = 1,message = "Vui lòng chọn khoảng 1-3")
	@Min(value = 3,message = "Vui lòng chọn khoảng 1-3")
	private Integer loaiHinhKinhDoanh;
	@NotNull(message="Vui lòng chọn ảnh CCCD")
	@NotBlank(message="CCCD ko được phép rỗng")
	private String hinhChupThe;
	@NotNull(message="Ảnh chụp cùng CCCD lòng chọn ảnh")
	@NotBlank(message="Ảnh chụp cùng CCCD ko được phép rỗng")
	private String anhDangCam;
	@NotNull(message="Vui lòng nhập tên")
	@Length(min = 5,message = "Vui lòng nhập họ và tên ít nhất 5 ký tự")
	private String hoVaTen;
	public Date getNgayDangKy() {
		return ngayDangKy;
	}

	public void setNgayDangKy(Date ngayDangKy) {
		this.ngayDangKy = ngayDangKy;
	}

	public Integer getLoaiHinhKinhDoanh() {
		return loaiHinhKinhDoanh;
	}

	public void setLoaiHinhKinhDoanh(Integer loaiHinhKinhDoanh) {
		this.loaiHinhKinhDoanh = loaiHinhKinhDoanh;
	}

	public String getHinhChupThe() {
		return hinhChupThe;
	}

	public void setHinhChupThe(String hinhChupThe) {
		this.hinhChupThe = hinhChupThe;
	}

	public String getAnhDangCam() {
		return anhDangCam;
	}

	public void setAnhDangCam(String anhDangCam) {
		this.anhDangCam = anhDangCam;
	}

	public String getHoVaTen() {
		return hoVaTen;
	}

	public void setHoVaTen(String hoVaTen) {
		this.hoVaTen = hoVaTen;
	}

	public String getTenCongTy() {
		return tenCongTy;
	}

	public void setTenCongTy(String tenCongTy) {
		this.tenCongTy = tenCongTy;
	}

	
	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

//	public String getSoDienThoai() {
//		
//		return soDienThoai;
//	}
//
//	public void setSoDienThoai(String soDienThoai) {
//		this.soDienThoai = soDienThoai;
//	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public String getMaSoThue() {
		return maSoThue;
	}

	public void setMaSoThue(String maSoThue) {
		this.maSoThue = maSoThue;
	}

	public String getGiayPhepKinhDoanh() {
		return giayPhepKinhDoanh;
	}

	public void setGiayPhepKinhDoanh(String giayPhepKinhDoanh) {
		this.giayPhepKinhDoanh = giayPhepKinhDoanh;
	}

	public Shop(int id) {
		this.shopId=id;
	}
	
	public Shop(int id,String tenShop) {
		this.shopId=id;
		this.shopName=tenShop;
	}
	
	public Shop(int id,String tenShop,String email) {
		this.shopId=id;
		this.shopName=tenShop;
		this.email=email;
	}
	
	
	public Shop() {
		
	}
	
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
