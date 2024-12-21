package com.API.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "VouchersCuaHang")
public class VoucherShop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "Chưa nhập tên voucher.")
	private String tenVoucher;
	@Length(max =5,message="Không được dài hơn 5 ký tự")
	@NotNull(message = "Không được null")
	private String maVoucher;
	@NotNull(message = "Vui lòng nhập giá trị giảm !!!")
	@Min(value = 1,message = "Giá trị giảm vui lòng lơn hơn 0 !!!")
	private float giaTriGiam;
	@Min(value = 0,message = "Chọn 0 nếu đơn hàng giảm theo %.")
	@Max(value = 1,message = "Chọn 1 nếu đơn hàng giam theo tiền.")
	private int loaiVoucher;
	@NotNull(message = "Chưa nhập ngày bắt đầu.")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "thoiGianBatDau")
	private Date ngayBatDau;
	@NotNull(message = "Chưa nhập ngày kết thúc.")
	@Column(name = "thoiGianKetThuc")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayKetThuc;
	@NotNull(message="Chưa nhập đơn tối thiểu.")
	@Min(value = 0,message = "Đơn tối thiểu = 0 nếu không giới hạn tiền.")
	private double donToiThieu;
	@Min(value = 1,message = "Số lược dùng phải lớn hơn 1.")
	private int soLuocDung;
	@Min(value = 1,message = "Số lược dùng phải lớn hơn bằng 1")
	@Column(name = "soLuocDungMoiNguoi")
	private int soLuocMoiNguoi;
	@NotNull(message="Chưa nhập số tiền giảm tối đa")
	@Column(name = "giamToiDa")
	private float tienGiamToiDa;
	private String moTa;
	private int trangThai;
	private int soLuocDaDung;
	public int getSoLuocDaDung() {
		return soLuocDaDung;
	}
	public void setSoLuocDaDung(int soLuocDaDung) {
		this.soLuocDaDung = soLuocDaDung;
	}
	@ManyToOne
//	@JsonIgnore
	@JoinColumn(name = "cuaHangId")
	private Shop shop;
	@Transient
	@JsonIgnore
    private List<String> a = new ArrayList<>();

    public List<String> getA() {
		return a;
	}
	public void setA(List<String> a) {
		this.a = a;
	}
	
	public VoucherShop(Integer id ,Integer soLuocDungMoiNguoi,Integer soLuocDaDung,String tenVoucher,float giaTriGiam,
			Integer loaiVoucher,float donToiThieu,float giamToiDa,Shop s) {
		this.id=id;
		this.soLuocMoiNguoi=soLuocDungMoiNguoi;
		this.soLuocDaDung=soLuocDaDung;
		this.tenVoucher=tenVoucher;
		this.giaTriGiam=giaTriGiam;
		this.loaiVoucher=loaiVoucher;
		this.donToiThieu=donToiThieu;
		this.tienGiamToiDa=giamToiDa;
		this.shop=s;
	}
	
	
	public VoucherShop(int id, float giaTriGiam, int loaiVoucher, float giamToiDa,double donToiThieu, Object items) {
        this.id = id;
        this.giaTriGiam = giaTriGiam;
        this.tienGiamToiDa = giamToiDa;
        this.donToiThieu=donToiThieu;  
        this.loaiVoucher = loaiVoucher;
        Collections.addAll(a, ((String)items).split(","));
        
    }
//    public VoucherShop(Integer  id) {
//        this.id = id;
//    }
    
	public boolean inList(String b) {
//		System.out.println(b);
		return a.contains(b);
	}
	
	
	public int getTrangThai() {
		return trangThai;
	}
	
	public VoucherShop() {
		
	}
	
	public VoucherShop(Integer id ) {
		this.id =id;
	}
	
	public VoucherShop(Integer id,Integer d ) {
		this.id =id;
	}
//	public vo
	
	public VoucherShop(int trangThai) {
		this.trangThai=trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}
	@OneToMany(mappedBy = "voucher")
	private Set<VoucherShopDetail> voucherShopDetails;
	
	public boolean startDateIsAfterDateNow() {
		return ngayBatDau.after(new Date());
	}
	
	
	public Set<VoucherShopDetail> getVoucherShopDetails() {
		return voucherShopDetails;
	}
	
	public boolean validDate() {
		LocalDateTime startDateTime = ngayBatDau.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = ngayKetThuc.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Duration duration = Duration.between(startDateTime,endDateTime);
        double durationInHours = duration.toHours();
        return durationInHours>=1;
//		return true;
	}
	
	
	
	public void setVoucherShopDetails(Set<VoucherShopDetail> voucherShopDetails) {
		this.voucherShopDetails = voucherShopDetails;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTenVoucher() {
		return tenVoucher;
	}
	public void setTenVoucher(String tenVoucher) {
		this.tenVoucher = tenVoucher;
	}
	public String getMaVoucher() {
		return maVoucher;
	}
	public void setMaVoucher(String maVoucher) {
		this.maVoucher = maVoucher;
	}
	public float getGiaTriGiam() {
		return giaTriGiam;
	}
	public void setGiaTriGiam(float giaTriGiam) {
		this.giaTriGiam = giaTriGiam;
	}
	public int getLoaiVoucher() {
		return loaiVoucher;
	}
	public void setLoaiVoucher(int loaiVoucher) {
		this.loaiVoucher = loaiVoucher;
	}
	public Date getNgayBatDau() {
		return ngayBatDau;
	}
	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}
	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}
	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}
	public double getDonToiThieu() {
		return donToiThieu;
	}
	public void setDonToiThieu(double donToiThieu) {
		this.donToiThieu = donToiThieu;
	}
	public int getSoLuocDung() {
		return soLuocDung;
	}
	public void setSoLuocDung(int soLuocDung) {
		this.soLuocDung = soLuocDung;
	}
	public int getSoLuocMoiNguoi() {
		return soLuocMoiNguoi;
	}
	public void setSoLuocMoiNguoi(int soLuocMoiNguoi) {
		this.soLuocMoiNguoi = soLuocMoiNguoi;
	}
	public float getTienGiamToiDa() {
		return tienGiamToiDa;
	}
	public void setTienGiamToiDa(float tienGiamToiDa) {
		this.tienGiamToiDa = tienGiamToiDa;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}
