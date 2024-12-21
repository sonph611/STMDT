package com.API.DTO.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.API.DTO.user.cart.CartViewDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class VoucherAplly {
	private Integer voucherId;
	private Integer accountId;
	private Integer loaiVoucher;
	private Float giaTriGiam;
	private Double donToiThieu;
	private String voucherName;
	private Integer voucherSoLuocDung;
	private Integer voucherSoLuocDaDung;
	private List<String> productIds=new ArrayList<String>();
	private Boolean canApply;
	private Double priceDiscount;
	public Double orderTotal=0.0;
	private Float giamToiDa=new Float(0);
	
	
	public Double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Float getGiamToiDa() {
		return giamToiDa;
	}

	public void setGiamToiDa(Float giamToiDa) {
		this.giamToiDa = giamToiDa;
	}

	public VoucherAplly(Integer voucherId, Integer accountId, Integer loaiVoucher, Float giaTriGiam, Double donToiThieu,
			String voucherName,Integer voucherSoLuocDung, Integer voucherSoLuocDaDung,Object a,Float giamToiDa) {
		super();
		this.voucherId = voucherId;
		this.accountId = accountId;
		this.loaiVoucher = loaiVoucher;
		this.giaTriGiam = giaTriGiam;
		this.donToiThieu = donToiThieu;
		this.voucherName = voucherName;
		this.voucherSoLuocDung = voucherSoLuocDung;
		this.voucherSoLuocDaDung = voucherSoLuocDaDung;
		Collections.addAll(this.productIds, ((String)a).split(","));
		this.giamToiDa=giamToiDa;
//		this.canApply = canApply;
//		this.priceDiscount = priceDiscount;
	}
	
	public VoucherAplly(Integer voucherId, Integer accountId, Integer loaiVoucher, Float giaTriGiam, Double donToiThieu,
			String voucherName,Integer voucherSoLuocDung, Integer voucherSoLuocDaDung) {
		super();
		this.voucherId = voucherId;
		this.accountId = accountId;
		this.loaiVoucher = loaiVoucher;
		this.giaTriGiam = giaTriGiam;
		this.donToiThieu = donToiThieu;
		this.voucherName = voucherName;
		this.voucherSoLuocDung = voucherSoLuocDung;
		this.voucherSoLuocDaDung = voucherSoLuocDaDung;
		this.canApply=accountId==null;
	}
	@JsonIgnore
	private Double total=0.0;
	
	public void addTotal(double a) {
		this.total+=a;
	}
	
	public Double getTotal() {
		return total;
	}


	public void setTotal(Double total) {
		this.total = total;
	}


	public boolean isInList(String a) {
		return productIds.contains(a);
	}
	

//	public VoucherAplly(Integer voucherId, Integer accountId, Integer loaiVoucher, Float giaTriGiam, Double donToiThieu,
//			String voucherName, Integer voucherSoLuocDung, Integer voucherSoLuocDaDung) {
//		super();
//		this.voucherId = voucherId;
//		this.accountId = accountId;
//		this.loaiVoucher = loaiVoucher;
//		this.giaTriGiam = giaTriGiam;
//		this.donToiThieu = donToiThieu;
//		this.voucherName = voucherName;
//		this.voucherSoLuocDung = voucherSoLuocDung;
//		this.voucherSoLuocDaDung = voucherSoLuocDaDung;
////		this.productIds = productIds;
////		this.canApply = canApply;
////		this.priceDiscount = priceDiscount;
//	}
	public Integer getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Integer voucherId) {
		this.voucherId = voucherId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getLoaiVoucher() {
		return loaiVoucher;
	}
	public void setLoaiVoucher(Integer loaiVoucher) {
		this.loaiVoucher = loaiVoucher;
	}
	public Float getGiaTriGiam() {
		return giaTriGiam;
	}
	public void setGiaTriGiam(Float giaTriGiam) {
		this.giaTriGiam = giaTriGiam;
	}
	public Double getDonToiThieu() {
		return donToiThieu;
	}
	public void setDonToiThieu(Double donToiThieu) {
		this.donToiThieu = donToiThieu;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public Integer getVoucherSoLuocDung() {
		return voucherSoLuocDung;
	}
	public void setVoucherSoLuocDung(Integer voucherSoLuocDung) {
		this.voucherSoLuocDung = voucherSoLuocDung;
	}
	public Integer getVoucherSoLuocDaDung() {
		return voucherSoLuocDaDung;
	}
	public void setVoucherSoLuocDaDung(Integer voucherSoLuocDaDung) {
		this.voucherSoLuocDaDung = voucherSoLuocDaDung;
	}
	public List<String> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}
	public Boolean getCanApply() {
		return canApply;
	}
	public void setCanApply(Boolean canApply) {
		this.canApply = canApply;
	}
	public Double getPriceDiscount() {
		return priceDiscount;
	}
	public void setPriceDiscount(Double priceDiscount) {
		this.priceDiscount = priceDiscount;
	}
}
//SELECT vc.id,vc.tenVoucher, vc.loaiVoucher, vc.giaTriGiam, vc.donToiThieu,vn.vouchercuahangId,
//vc.soLuocDaDung, vc.soLuocDung,GROUP_CONCAT('', vs.sanPham_Id,' ') AS sanPhams
//FROM voucherscuahang vc
//JOIN (SELECT * FROM voucher_sanpham vs WHERE vs.sanPham_Id IN (1923, 1924)) vs ON vs.voucher_Id = vc.id
//LEFT JOIN vouchercuahangnguoidung vn ON vn.vouchercuahangId = vc.id
//WHERE vc.cuaHangid = 1 
//AND NOW() BETWEEN vc.thoiGianBatDau AND vc.thoiGianKetThuc
//AND vc.soLuocDaDung < vc.soLuocDung
//AND (COALESCE(vn.soLuocDung, 0) + 1 <= vc.soLuocDungMoiNguoi)
//GROUP BY vc.id