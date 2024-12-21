package com.API.DTO.saleproductDTo;

import java.util.Set;

import com.API.model.ProductDetail;

public class ProductDTOWithProductDetail {
	    private Integer id;
	    private String tenSanPham;
	    private Set<ProductDetail> productDetails;

	    // Constructor
	    public ProductDTOWithProductDetail(Integer id, String tenSanPham,String hinhAnh, Set<ProductDetail> productDetails) {
	        this.id = id;
	        this.tenSanPham = tenSanPham;
	        this.productDetails = productDetails;
	    }

	    // Getters and setters
	    public Integer getId() {
	        return id;
	    }

	    public String getTenSanPham() {
	        return tenSanPham;
	    }

	    public Set<ProductDetail> getProductDetails() {
	        return productDetails;
	    }
	}