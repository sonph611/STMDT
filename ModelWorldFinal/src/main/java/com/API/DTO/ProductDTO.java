package com.API.DTO;



public class ProductDTO {
	private int productId;
	private String productName;
	private int soLuong;
	private float productPrice;
	private int CategoryId;
	private String image;
	private int trangThai;
	private String moTa;
	
	public ProductDTO() {
		
	}
	
	public ProductDTO(int productId, String productName,int soLuong, float productPrice, String image, int trangThai) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.image = image;
		this.soLuong=soLuong;
		this.trangThai = trangThai;
	}
	
	

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public float getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}
	public int getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	
}



