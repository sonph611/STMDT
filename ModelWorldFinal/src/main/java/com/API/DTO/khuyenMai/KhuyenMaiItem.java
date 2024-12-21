package com.API.DTO.khuyenMai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.API.model.ProductDetail;
import com.API.model.ProductImage;

public class KhuyenMaiItem {
	private Integer id;
	private Integer productId;
	private String productName;
	private Float  minPrice;
	private Float  maxPrice;
	private String productImage;
	public String getProductImage() {
		return productImage;
	}
	
	private List< ProductDetail> productDetails=new ArrayList<ProductDetail>();
	
	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}
	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	private BigDecimal soLuong;
//	private Long soLuong
//	private Long soLuongTonKho;
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	
//	public KhuyenMaiItem() {
//		
//	}
	
	public KhuyenMaiItem(Integer productId, String productName,String ProductImage) {
		this.productId=productId;
		this.productName = productName;
		this.productImage=ProductImage;
	}
	public KhuyenMaiItem(Integer id,Integer productId, String productName,String ProductImage) {
		this.productId=productId;
		this.productName = productName;
		this.productImage=ProductImage;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public KhuyenMaiItem(Integer productId, String productName) {
		this.productId=productId;
		this.productName = productName;
	}
	
	public KhuyenMaiItem(Integer productId, String productName, Object  minPrice,Object maxPrice, Object soLuong,String image) {
//		super();
//		this.id = id;
		this.productId=productId;
		this.productName = productName;
		this.minPrice=(Float) minPrice;
		this.maxPrice=(Float) maxPrice;
		this.soLuong = new BigDecimal(soLuong+"");
		this.productImage=image;
	}
	
	public KhuyenMaiItem(Integer id, Integer productId, String productName, Object  minPrice,Object maxPrice, Object soLuong) {
//super();
//		this.id = id;
		this.productId=productId;
		this.productName = productName;
		this.minPrice=(Float) minPrice;
		this.maxPrice=(Float) maxPrice;
		this.soLuong = new BigDecimal(soLuong+"");
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Float getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(Float minPrice) {
		this.minPrice = minPrice;
	}
	public Float getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(Float maxPrice) {
		this.maxPrice = maxPrice;
	}
	public BigDecimal getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(BigDecimal soLuong) {
		this.soLuong = soLuong;
	}
	
}
