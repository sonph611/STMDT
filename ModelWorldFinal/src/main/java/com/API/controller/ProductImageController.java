package com.API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.ProductImage;
import com.API.repository.KichThuocRespository;
import com.API.repository.ProductImageRepository;
import com.API.service.saler.ShopFilter;
import com.API.utils.ObjectRespone;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;



// cần sửa lại shopId của mỗi request

@RestController
public class ProductImageController {
	
	@Autowired
	ShopFilter s;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@GetMapping(value="/productimage/getall")
	public ResponseEntity<ObjectRespone> getAllAccount() {
		return ResponseEntity.ok(new ObjectRespone(200,"success",productImageRepository.findAll()));
	}
	
	
	@PostMapping("/sale/productimage/delete/{id}/{productId}")
	public ObjectRespone deleteProductImage(@PathVariable int id,@Valid @PathVariable int productId) {
		Integer p=(productImageRepository.findShopIdByIdAndProductId(id, productId));
		if(p!=null) {
			if(p==s.getShopId()) {
				productImageRepository.deleteById(id);
				return new ObjectRespone(200,"Đã xóa thành công ảnh",id);
			}
			return new ObjectRespone(403,"Bạn không có quyền xóa ảnh product này",p);
		}
		return new ObjectRespone(400,"Dữ liệu hình ảnh không chính xác",null);
	}
	
	
}
