package com.API.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "donhang")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayTaoDon;
	@ManyToOne
	@JoinColumn(name = "voucherShopId")
//	@JsonView(value = )
	private VoucherShop voucherShopId;
	private Double tongTien=0.0;
	private String ghiChu;
	private String lyDo;
	private float phiShip;
	@Column(name="daNhanHang")
	private Integer daNhanHang;
	public Integer getDaNhanHang() {
		return daNhanHang;
	}


	public void setDaNhanHang(Integer daNhanHang) {
		this.daNhanHang = daNhanHang;
	}


	public void setTienTru(Float tienTru) {
		this.tienTru = tienTru;
	}

//	(p.id,new com.API.model.Shop(p.shop.shopId,p.shop.shopName)"
//			+ ",p.ngayTaoDon,p.tongTien,p.phiShip,p.diaChiId,p.trangThai,p.thanhToanId

	public void setTienTruVoucherSan(Float tienTruVoucherSan) {
		this.tienTruVoucherSan = tienTruVoucherSan;
	}
	//	@Transient
	@Column(name="tienTruVoucherShop")
//	@JsonView(value = )
//	@JsonIgnore
	public Float tienTru=(float)0;
	
	private Float tienTruVoucherSan=(float) 0;
	
	@ManyToOne
	@JoinColumn(name = "shopId")
	Shop shop;
	
	public float getTienTruVoucherSan() {
		return tienTruVoucherSan;
	}
	
	
	public   Order(Integer id ,Float tienTruVoucherShop,Double total,Integer userId,String tenUser,String sdt) {
		this.id=id;
		this.tienTru=tienTruVoucherShop;
		this.tongTien=total;
		this.accountId=new Account(userId, tenUser, sdt);
	}
	
	
	public Order(Integer trangThaiId,Integer voucherSanId,Integer voucherShopId) {
		this.trangThai=new TrangThai(trangThaiId);
		this.voucherSanId=new VoucherSan(voucherSanId);
//		System.out.println(vouche);
		this.voucherShopId=new VoucherShop(voucherShopId);
//		this.voucherShopId=new VoucherShop(0, 1);
	}
	

	public Order(Integer trangThaiId,Integer voucherSanId,Integer voucherShopId,Integer accountId,Integer id) {
		this.trangThai=new TrangThai(trangThaiId);
		this.voucherSanId=new VoucherSan(voucherSanId);
//		System.out.println(vouche);
		this.voucherShopId=new VoucherShop(voucherShopId);
		this.accountId=new Account(accountId);
		this.id=id;
//		this.voucherShopId=new VoucherShop(0, 1);
	}
	

	
	
	public Order(Account a,List<OrderDetail>o) {
		this.accountId=a;
		this.orderDetails=o;
		this.tienTruVoucherSan=(float) 0;
	}


	public void setTienTruVoucherSan(float tienTruVoucherSan) {
		this.tienTruVoucherSan = tienTruVoucherSan;
	}
	@ManyToOne
	@JoinColumn(name = "thanhToanId")
	private ThanhToan thanhToanId;
	@ManyToOne
	@JoinColumn(name = "diaChiId")
	private DiaChi diaChiId;
	@ManyToOne
	@JoinColumn(name = "voucherSanId")
	private VoucherSan voucherSanId;
	@ManyToOne
	@JoinColumn(name = "taiKhoanId")
	private Account accountId;
	@ManyToOne
	@JoinColumn(name = "trangThaiId")
	private TrangThai trangThai;
	
//	@JsonBackReference
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;
	
	@Transient
	public float tienTinhDuoc=0;
	
	public void calTongTien(float a) {
		tienTruVoucherSan=(float)(a>tongTien?tongTien:a);
	}
	
	public float getTienTinhDuoc() {
		return tienTinhDuoc;
	}


	public void setTienTinhDuoc(float tienTinhDuoc) {
		this.tienTinhDuoc = tienTinhDuoc;
	}


	public Integer getNguoiHuy() {
		return nguoiHuy;
	}


	public void setNguoiHuy(Integer nguoiHuy) {
		this.nguoiHuy = nguoiHuy;
	}
	private Integer nguoiHuy;
	
	
	public void calTongTienShip(float a) {
		tienTruVoucherSan=a;
		this.phiShip=phiShip-a<0?0:phiShip-a;
	}
	
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}


	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String ten,String sdt,Integer accountId) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.accountId=new Account(accountId,ten,sdt);
		
	}
	
	public Order(Double tongTien,int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String ten,String sdt,Integer accountId) {
		this.id=id;
		this.tongTien=tongTien;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.accountId=new Account(accountId,ten,sdt);
		
	}
	
	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String ten,String sdt) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.accountId=new Account(ten,sdt);
		
	}
	
	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String ten,String sdt,String lyDo) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.accountId=new Account(ten,sdt);
		this.lyDo=lyDo;
		
	}
//	p.id,p.ngayTaoDon,p.diaChiId,p.thanhToanId,p.ghiChu,p.shop.shopName,p.shop.shopId,p.lyDo
	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String ten,String sdt,String lyDo,Integer daNhanHang) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.accountId=new Account(ten,sdt);
		this.lyDo=lyDo;
		this.daNhanHang=daNhanHang;
	}
	
	public Order(int id,Date ngayTao,ThanhToan thanhToan,String ghiChu,String ten,String sdt,String lyDo,Double tongTien,TrangThai trangThai,Integer nguoiHuy) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.accountId=new Account(ten,sdt);
		this.lyDo=lyDo;
//		this.daNhanHang=daNhanHang;
		this.nguoiHuy=nguoiHuy;
		this.tongTien=tongTien;
//		System.out.println(this.tongTien);
		this.trangThai=trangThai;
	}
	

	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String shopName,Integer shopId,String lyDo,Integer daNhanHang,TrangThai trangThai) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.shop=new Shop(shopId,shopName);
		this.lyDo=lyDo;
		this.daNhanHang=daNhanHang;
		this.trangThai=trangThai;
	}
	
	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String shopName,Integer shopId,String lyDo) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.shop=new Shop(shopId,shopName);
		this.lyDo=lyDo;
		this.daNhanHang=daNhanHang;
		
	}
//	p.id,p.ngayTaoDon,p.diaChiId,p.thanhToanId,p.ghiChu,p.accountId.hoVaTen,p.accountId.soDienThoai,p.lyDo
	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String shopName,Integer shopId,String lyDo,Integer daNhanHang,Integer idd) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		this.shop=new Shop(shopId,shopName);
		this.lyDo=lyDo;
		this.daNhanHang=daNhanHang;
		
	}
	
	
	public Order(Integer id ,Double tongTien,Float phiShip,Float tienTru,Float tienTruVoucherSan) {
		this.id=id;
		this.phiShip=phiShip;
		this.tongTien=tongTien;
		this.tienTru=tienTru;
		this.tienTruVoucherSan=tienTruVoucherSan;
	}
	
	

	public Order(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu) {
		this.id=id;
		this.ngayTaoDon=ngayTao;
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.ghiChu=ghiChu;
		
	}
	

	public Order(int id,Shop s,Date ngayTao,Double tongTien,float phiShip,DiaChi dc,TrangThai trangThai,ThanhToan thanhToan) {
		this.id=id;
		this.shop=s;
		this.ngayTaoDon=ngayTao;
		this.tongTien=tongTien;
		this.phiShip=phiShip;
		this.diaChiId=dc;
		this.trangThai=trangThai;
		this.thanhToanId=thanhToan;
//		this.tienTru=tienTru;
//		this.tienTruVoucherSan=tienTruVoucherSan;
	}
	
	public Order(int id,Shop s,Date ngayTao,Double tongTien,float phiShip,DiaChi dc,TrangThai trangThai,ThanhToan thanhToan,Account a,Float tienTru,Float tienTruVoucherSan) {
		this.id=id;
		this.shop=s;
		this.ngayTaoDon=ngayTao;
		this.tongTien=tongTien;
		this.phiShip=phiShip;
		this.diaChiId=dc;
		this.trangThai=trangThai;
		this.thanhToanId=thanhToan;
		this.accountId=a;
		this.tienTru=tienTru;
		this.tienTruVoucherSan=tienTruVoucherSan;
		
//		this.tienTru=tienTru;
//		this.tienTruVoucherSan=tienTruVoucherSan;
	}
	
	
	public Order(int id,Shop s,Date ngayTao,Double tongTien,float phiShip,DiaChi dc,TrangThai trangThai,ThanhToan thanhToan,float tienTru,float tienTruVoucherSan) {
		this.id=id;
		this.shop=s;
		this.ngayTaoDon=ngayTao;
		this.tongTien=tongTien;
		this.phiShip=phiShip;
		this.diaChiId=dc;
		this.trangThai=trangThai;
		this.thanhToanId=thanhToan;
		this.tienTru=tienTru;
		this.tienTruVoucherSan=tienTruVoucherSan;
	}
	
	
	
	public Order(int id,Shop s,Date ngayTao,Double tongTien,float phiShip,DiaChi dc,TrangThai trangThai,ThanhToan thanhToan,DiaChi d) {
		this.id=id;
		this.shop=s;
		this.ngayTaoDon=ngayTao;
		this.tongTien=tongTien;
		this.phiShip=phiShip;
		this.diaChiId=dc;
		this.trangThai=trangThai;
		this.thanhToanId=thanhToan;
		this.diaChiId=d;
	}
	
	
	public float getTienTru() {
		return tienTru;
	}

	public void setTienTru(float tienTru) {
		this.tienTru = tienTru;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public TrangThai getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThai trangThai) {
		this.trangThai = trangThai;
	}

	public Order() {
		
	}
	
	
	@Override
	public boolean equals(Object o) {
	    Order order = (Order) o;
	    return this.id==order.id;
	}

	@Override
	public int hashCode() {
	    return  id.hashCode();
	}
	
	public Order(int id) {
		this.id=id;
	}
	

	public Order(int id,Double tongDon,Shop s) {
		this.id=id;
		this.tongTien=tongDon;
		this.shop=s;
	}
	
	
	public void setBasicInfo(DiaChi dc,ThanhToan thanhToan,VoucherSan vs,Account a,Shop shop) {
		this.diaChiId=dc;
		this.thanhToanId=thanhToan;
		this.voucherSanId=vs;
		this.accountId=a;
		this.shop=shop;
		this.trangThai=new TrangThai(1);
	}
	

	public void plusTotalOrder(Float a) {
		this.tongTien=this.tongTien+a;
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getNgayTaoDon() {
		return ngayTaoDon;
	}
	public void setNgayTaoDon(Date ngayTaoDon) {
		this.ngayTaoDon = ngayTaoDon;
	}
	public VoucherShop getVoucherShopId() {
		return voucherShopId;
	}
	public void setVoucherShopId(VoucherShop voucherShopId) {
		this.voucherShopId = voucherShopId;
	}
	public Double getTongTien() {
		return tongTien;
	}
	public void setTongTien(Double tongTien) {
		this.tongTien = tongTien;
	}
	public String getGhiChu() {
		return ghiChu;
	}
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	public String getLyDo() {
		return lyDo;
	}
	public void setLyDo(String lyDo) {
		this.lyDo = lyDo;
	}
	public float getPhiShip() {
		return phiShip;
	}
	public void setPhiShip(float phiShip) {
		this.phiShip = phiShip;
	}
	public ThanhToan getThanhToanId() {
		return thanhToanId;
	}
	public void setThanhToanId(ThanhToan thanhToanId) {
		this.thanhToanId = thanhToanId;
	}
	public DiaChi getDiaChiId() {
		return diaChiId;
	}
	public void setDiaChiId(DiaChi diaChiId) {
		this.diaChiId = diaChiId;
	}
	public VoucherSan getVoucherSanId() {
		return voucherSanId;
	}
	public void setVoucherSanId(VoucherSan voucherSanId) {
		this.voucherSanId = voucherSanId;
	}
	public Account getAccountId() {
		return accountId;
	}
	public void setAccountId(Account accountId) {
		this.accountId = accountId;
	}
}
