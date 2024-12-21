package com.API.DTO;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import com.API.model.KhuyenMai;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InsertKhuyenMaiDTO {
	@Valid
	private KhuyenMai khuyenMai;
	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}
	public void setKhuyenMai( KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	@NotNull(message = "Vui lòng thêm sản phẩm vào khuyến mãi")
//	@Size(min =1,message = "Khuyến mãi cần ít nhất một sản phẩm")
  	public Set<Integer> set;

	public Set<Integer> getSet() {
		return set;
	}
	public void setSet(Set<Integer> set) {
		this.set = set;
	}
	
}
