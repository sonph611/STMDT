package com.API.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.LichSuSanPham;
import com.API.model.Product;
import com.API.repository.CategoryRepository;
import com.API.repository.KichThuocRespository;
import com.API.repository.MauSacRepository;
import com.API.repository.ProductRepository;
import com.API.repository.ThuongHieuRepository;
import com.API.service.HomeService;
import com.API.utils.ObjectRespone;

import io.jsonwebtoken.lang.Arrays;

@RestController
@CrossOrigin("*")
public class HomeController {

	@Autowired
	HomeService homeService;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ThuongHieuRepository thuongHieuRepository;
	@Autowired
	MauSacRepository mauSacRepository;
	@Autowired
	KichThuocRespository kichThuocRespository;
	
	@GetMapping("/home/getProductOnHome")
	public ResponseEntity<ObjectRespone> getLatestProducts(@RequestParam(defaultValue = "0") int page,
										   @RequestParam(defaultValue = "48") int size) {
		
			List<Object[]> listProduct = productRepository.getProductHome();
			List<Object[]> listCategory = categoryRepository.getLoaiSanPhamHome();
			 Map<String, Object> responseData = new HashMap<>();
			    responseData.put("listProduct", listProduct);
			    responseData.put("listCategory", listCategory);	
		return ResponseEntity.ok(new ObjectRespone(200, "success", responseData));
	}
	 
	@GetMapping("/home/getProducLoginIn")
	public ResponseEntity<ObjectRespone> getLatestProductsLogin(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "48") int size,
	    @RequestParam int userId) {

	    if (userId <= 0) {
	        return ResponseEntity.badRequest().body(new ObjectRespone(400, "Invalid userId", null));
	    }

	    List<Object[]> listProduct = productRepository.getProductHomeLogin(userId);
	    List<Object[]> listCategory = categoryRepository.getLoaiSanPhamHome();
	    Map<String, Object> responseData = new HashMap<>();
	    responseData.put("listProduct", listProduct);
	    responseData.put("listCategory", listCategory);  
	    return ResponseEntity.ok(new ObjectRespone(200, "success", responseData));
	}
	
	@GetMapping("/home/find/{name}")
	public ResponseEntity<ObjectRespone> findProductByName(
	        @PathVariable("name") String name,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(value = "theLoaiIds", required = false) List<Integer> theLoaiIds,
	        @RequestParam(value = "sortBy", required = false) String sortBy,
	        @RequestParam(value = "thuongHieuIds",required = false) List<Integer> thuongHieuIds,
	        @RequestParam(required = false) List<Integer> mauSacIds,
	        @RequestParam(required = false) List<Integer> kichThuocIds,
	        @RequestParam(value = "giaMin",required = false,defaultValue = "0.0") Double giaMin,
	        @RequestParam(value = "giaMax",required = false,defaultValue = "0.0") Double giaMax
	        
	) {
	
	    theLoaiIds = theLoaiIds == null ? new ArrayList<>() : theLoaiIds;
	    thuongHieuIds = thuongHieuIds == null ? new ArrayList<>() : thuongHieuIds;
	    mauSacIds = mauSacIds == null ? new ArrayList<>() : mauSacIds;
	    kichThuocIds = kichThuocIds == null ? new ArrayList<>() : kichThuocIds;
	   
	    Pageable pageable;
	    pageable = PageRequest.of(page, size);
	    if (sortBy.equals("best_selling")) {
	        pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("soLuongDaBan")));
	        System.out.println("Sắp theo lượt bán");
	    } else if (sortBy.equals("low_to_high")) {
	        pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("giaSanPham")));
	        System.out.println("Sắp theo giá thấp đến cao");
	    } else if (sortBy.equals("high_to_low")) {
	        pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("giaSanPham")));
	        System.out.println("Sắp theo giá cao về thấp");
	    } else if (sortBy.equals("az")) {
	        pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("tenSanPham")));
	        System.out.println("Sắp theo A-Z");
	    } else if (sortBy.equals("za")) {
	        pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("giaSanPham")));
	        System.out.println("Sắp theo ");
	    } else if (sortBy.equals("newest_first")) {
	        pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));
	    } else if (sortBy.equals("oldest_firs")) {
	        pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
	    } 
	    Page<Object[]> productPage = null;
	    
	    // Nếu chỉ tìm theo tên mà không có các bộ lọc khác
	    if (theLoaiIds.isEmpty() && thuongHieuIds.isEmpty() && mauSacIds.isEmpty() && kichThuocIds.isEmpty()&& giaMin == 0 && giaMax == 0)  {
	        productPage = productRepository.findProductByName(name, pageable);
	        System.out.println("Hello 1");
	        
	    } else {
	    	System.out.println("Hello 2");
	    	
	    	if (giaMin == 0 && giaMax == 0) {
	    		
				giaMin = (double) 0;
				giaMax = (double) 999999999;
}else if(giaMin == 0 && giaMax != 0) {
				
				giaMin = (double) 0;
			
			}else if(giaMin != 0 && giaMax == 0){
				
				giaMax = giaMin;
				giaMin = (double) 0;
				
			}
	    	productPage = productRepository.findSanPhamWithFilters(name,theLoaiIds,kichThuocIds,thuongHieuIds,mauSacIds,giaMin,giaMax,pageable);
	    		
	    	 
	    }
	    
	    // Lấy các bộ lọc dữ liệu để trả về cho frontend
	    List<Object[]> fillterTheLoai = categoryRepository.getFillterLoaiSanPham();
	    List<Object[]> fillterThuongHieu = thuongHieuRepository.getFillterThuongHieu();
	    List<Object[]> fillterMauSac = mauSacRepository.getFillterMauSac();
	    List<Object[]> fillterKichThuoc = kichThuocRespository.getFillterKichThuoc();


	    // Đưa các bộ lọc vào Map để trả về cho frontend
	    Map<String, List<Object[]>> filters = new HashMap<>();
	    filters.put("theLoai", fillterTheLoai);
	    filters.put("thuongHieu", fillterThuongHieu);
	    filters.put("mauSac", fillterMauSac);
	    filters.put("kichThuoc", fillterKichThuoc);

	    // Chuẩn bị dữ liệu trả về, bao gồm cả danh sách sản phẩm và bộ lọc
	    Map<String, Object> response = new HashMap<>();
	    response.put("products", productPage);
	    response.put("filters", filters);

	    return ResponseEntity.ok(new ObjectRespone(200, "success", response));
	}
	
	
 
}