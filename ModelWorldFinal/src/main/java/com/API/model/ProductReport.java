package com.API.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productreport")
public class ProductReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer productId;
	private Integer reportId;
	private Integer reportedBy;
	private Integer status;
	private LocalDateTime createdAt;
	private String content;
	private String image;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public Integer getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(Integer reportedBy) {
		this.reportedBy = reportedBy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDateTime getCreateAt() {
		return createdAt;
	}

	public void setCreateAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}




	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	



	public ProductReport() {
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public ProductReport(Integer id, Integer productId, Integer reportId, Integer reportedBy, Integer status,
			LocalDateTime createdAt, String content, String image) {
		super();
		this.id = id;
		this.productId = productId;
		this.reportId = reportId;
		this.reportedBy = reportedBy;
		this.status = status;
		this.createdAt = createdAt;
		this.content = content;
		this.image = image;
	}
	
	

}
