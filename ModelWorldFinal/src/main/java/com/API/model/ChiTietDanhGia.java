package com.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mediaDanhGia")
public class ChiTietDanhGia {
	private static final String IMAGE_TYPE="IMAGE";
	private static final String VIDEO_TYPE="VIDEO";
	private static final String LINK_TYPE="LINK";
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;
	@ManyToOne
	@JoinColumn(name="danhGia_Id")
	private DanhGiaSanPham danhGiaSanPham;
	private String link;
	
	
	public DanhGiaSanPham getDanhGiaSanPham() {
		return danhGiaSanPham;
	}

	public void setDanhGiaSanPham(DanhGiaSanPham danhGiaSanPham) {
		this.danhGiaSanPham = danhGiaSanPham;
	}

	public ChiTietDanhGia() {
		
	}
	
	public ChiTietDanhGia(Integer id,Integer danhGiaId,String link,String type) {
		this.id=id;
		this.danhGiaSanPham=new DanhGiaSanPham(danhGiaId);
		this.link=link;
		this.type=type;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private String type;
}
