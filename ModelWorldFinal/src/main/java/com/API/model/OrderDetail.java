
package com.API.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ChiTietDonHang")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
//	@JsonManagedReference
	@JoinColumn(name = "donHangId")
//	@JsonIgnore
	private Order order;
	@ManyToOne
	@JoinColumn(name = "productId")
	private ProductDetail product;
	
	@Transient
	private int productId;
	@Transient
	private int shopId;
	
	// THUỘC TÍNH THÊM MỚI
	@Transient
	@JsonIgnore
	private Integer liveId;
	@Transient
	@JsonIgnore
	private Integer soLuongGioHan;
	@Transient
	@JsonIgnore
	private Double giaGiam;
	
	public Integer getLiveId() {
		return liveId;
	}


	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}


	public Integer getSoLuongGioHan() {
		return soLuongGioHan;
	}


	public void setSoLuongGioHan(Integer soLuongGioHan) {
		this.soLuongGioHan = soLuongGioHan;
	}


	public Double getGiaGiam() {
		return giaGiam;
	}


	public void setGiaGiam(Double giaGiam) {
		this.giaGiam = giaGiam;
	}

	private Integer trangThaiDanhGia;
	
//	[
//     1440,
//     "Nồi nướng không dầu",
//     1001.0,
//     1001.0000,
//     "Hammi",
//     1,
//     "Đỏ",
//     "23"
// ],"23"'
//	p.id,p.tenSanPham,pd.giaBan, ch.giaBan,s.shopName,s.shopId,ms.tenMau,k.tenKichThuoc 
	
	public Integer getTrangThaiDanhGia() {
		return trangThaiDanhGia;
	}


	public void setTrangThaiDanhGia(Integer trangThaiDanhGia) {
		this.trangThaiDanhGia = trangThaiDanhGia;
	}


	public OrderDetail(Integer orderId,Integer shopId,String shopName,Integer productOrderId,Integer productId,String productName,
			float giaBan,Integer soLuong,String tenMau,String tenKichThuoc,Double tongDon) {
		this.order=new Order(orderId,tongDon,new Shop(shopId, shopName));
		this.product=new ProductDetail(new Product(productId,productName),productOrderId,tenMau,tenKichThuoc);
		this.giaBan=giaBan;
		this.soLuong=soLuong;
	}

	
	public OrderDetail(Integer a) {
	}
	
//	public ProductDetail(Product p,String hinhAnh,float giaBan,String tenMau,String tenKichThuoc) {
	public OrderDetail(Integer soLuong,float giaBan,ProductDetail p) {
		this.soLuong=soLuong;
		this.product=p;
		this.giaBan=giaBan;
	}
	public OrderDetail(Integer orderId,Integer soLuong,float giaBan,ProductDetail p) {
		this.soLuong=soLuong;
		this.product=p;
		this.giaBan=giaBan;
		this.order=new Order(orderId);
	}
	
	public OrderDetail(Integer id,Integer orderId,Integer soLuong,float giaBan,ProductDetail p) {
		this.soLuong=soLuong;
		this.product=p;
		this.id=id;
		this.giaBan=giaBan;
		
		this.order=new Order(orderId);
	}
	
	public OrderDetail(Integer id,Integer orderId,Integer soLuong,float giaBan,ProductDetail p,Integer trangThaiDanhGia) {
		this.soLuong=soLuong;
		this.product=p;
		this.id=id;
		this.giaBan=giaBan;
		this.trangThaiDanhGia=trangThaiDanhGia;
		
		this.order=new Order(orderId);
	}
	
	public OrderDetail(int bienTheId,int soLuong,float giaGiam, float giaBan,int productId,int shopId) {
		this.product=new ProductDetail(bienTheId);
		this.soLuong = soLuong;
		this.giaDaGiam=giaGiam;
		this.giaBan = giaBan;
		this.productId=productId;
		this.shopId=shopId;
	}
	
	public OrderDetail(int bienTheId,int soLuong,float giaGiam, float giaBan,int productId,int shopId,Integer liveId,Integer soLuongGioHan,Double giaGiams) {
		this.product=new ProductDetail(bienTheId);
		this.soLuong = soLuong;
		this.giaDaGiam=giaGiam;
		this.giaBan = giaBan;
		this.productId=productId;
//		System.out.println(" hello bạn hiền "+productId);
		this.shopId=shopId;
		this.liveId=liveId;
		this.soLuongGioHan=soLuongGioHan;
		this.giaGiam=giaGiams;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

//	public OrderDetail(int productId,int soLuong, Float giaBan,Float giaGiam) {
//		this.product=new ProductDetail(productId);
//		this.soLuong = soLuong;
//		this.giaBan = giaBan;
//		this.giaDaGiam=giaGiam;
//	}
	@Transient
	@JsonIgnore
	private Float giaDaGiam;
	
	public Float getGiaDaGiam() {
		return giaDaGiam;
	}


	public void setGiaDaGiam(Float giaDaGiam) {
		this.giaDaGiam = giaDaGiam;
	}


	public OrderDetail(int productId,int soLuong, float giaBan) {
		this.product=new ProductDetail(productId);
//		System.out.println("Hello bạn hiền"+productId);
//		System.out.println(soLuong);
		this.soLuong = soLuong;
		this.giaBan = giaBan;
	}
	
	public OrderDetail() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public ProductDetail getProduct() {
		return product;
	}
	public void setProduct(ProductDetail product) {
		this.product = product;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public float getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(float giaBan) {
		this.giaBan = giaBan;
	}
	private int soLuong;
	private float giaBan;
}
