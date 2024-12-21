
package com.API.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.repository.CategoryRepository;
import com.API.repository.KichThuocRespository;
import com.API.repository.MauSacRepository;
import com.API.repository.ShopRepository;
import com.API.repository.ThuongHieuRepository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
@RequestMapping("/shop")
public class ShopController {
	@Autowired
	private ShopRepository shopRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ThuongHieuRepository thuongHieuRepository;
	@Autowired
	private MauSacRepository mauSacRepository;
	@Autowired
	private KichThuocRespository kichThuocRespository;
	
	
	@GetMapping(value="/getall")
	public ResponseEntity<ObjectRespone> getAllAccount() {
		return ResponseEntity.ok(new ObjectRespone(200,"success",shopRepository.findAll()));
	}
	
	@GetMapping(value = "/{shopId}")
	public ResponseEntity<ObjectRespone> getShopById(@PathVariable int shopId,@RequestParam(defaultValue = "0") int userId) {
		
		
	    // Lấy danh sách shop từ cơ sở dữ liệu
	    List<Object[]> shopDetails = shopRepository.getShopById(shopId);
	    
	    // Lấy danh sách voucher từ cơ sở dữ liệu (giả sử bạn có phương thức tương tự)
	    List<Object[]> shopVouchers = shopRepository.getShopVoucherById(shopId);
	    List<Object[]> shopProduct = null;
	    if(userId == 0) {
	    	shopProduct = shopRepository.getSanPham(shopId);
	    	System.out.println("Không người dùng");
	    }else {
	    	shopProduct = shopRepository.getSanPham(shopId,userId);
	    	System.out.println("Có người dùng");
	    }
	    
	    
	    List<Object[]> shopProductNew = shopRepository.getSanPhamDesc(shopId);
	    // Tạo một object chứa cả 2 danh sách để trả về client
	    Map<String, Object> responseData = new HashMap<>();
	    responseData.put("shopDetails", shopDetails);
	    responseData.put("shopVouchers", shopVouchers);
	    responseData.put("shopProduct", shopProduct);
	    responseData.put("shopProductNew", shopProductNew);
	    // Trả về kết quả dưới dạng ObjectRespone
	    return ResponseEntity.ok(new ObjectRespone(200, "success", responseData));  
	}
	@GetMapping("/{id}/product")
	public ResponseEntity<ObjectRespone> findProductByName(
	        @PathVariable("id") Integer id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(value = "theLoaiIds", required = false) List<Integer> theLoaiIds,
	        @RequestParam(value = "sortBy", required = false) String sortBy,
	        @RequestParam(required = false) List<Integer> thuongHieuIds,
	        @RequestParam(required = false) List<Integer> mauSacIds,
	        @RequestParam(required = false) List<Integer> kichThuocIds,
	        @RequestParam(value = "giaMin",required = false,defaultValue = "0.0") Double giaMin,
	        @RequestParam(value = "giaMax",required = false,defaultValue = "0.0") Double giaMax
	        
	) {
	    // Khởi tạo các tham số nếu chúng là null
		
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
	        productPage = shopRepository.findProductByName(id, pageable);
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
	    	productPage = shopRepository.findSanPhamWithFilters(id, theLoaiIds,kichThuocIds,thuongHieuIds,mauSacIds,giaMin,giaMax,pageable);
	    		
	    	
	    }
	    List<Object[]> shopDetails = shopRepository.getShopById(id);
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
	    response.put("shopDetails", shopDetails);
	    return ResponseEntity.ok(new ObjectRespone(200, "success", response));
	}
	 
}



//package com.API.controller;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.API.repository.CategoryRepository;
//import com.API.repository.KichThuocRespository;
//import com.API.repository.MauSacRepository;
//import com.API.repository.ShopRepository;
//import com.API.repository.ThuongHieuRepository;
//import com.API.utils.ObjectRespone;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/shop")
//public class ShopController {
//	@Autowired
//	private ShopRepository shopRepository;
//	@Autowired
//	private CategoryRepository categoryRepository;
//	@Autowired
//	private ThuongHieuRepository thuongHieuRepository;
//	@Autowired
//	private MauSacRepository mauSacRepository;
//	@Autowired
//	private KichThuocRespository kichThuocRespository;
//	
//	
//	@GetMapping(value="/getall")
//	public ResponseEntity<ObjectRespone> getAllAccount() {
//		return ResponseEntity.ok(new ObjectRespone(200,"success",shopRepository.findAll()));
//	}
//	
//	@GetMapping(value = "/{shopId}")
//	public ResponseEntity<ObjectRespone> getShopById(@PathVariable int shopId) {
//		
//		
//	    // Lấy danh sách shop từ cơ sở dữ liệu
//	    List<Object[]> shopDetails = shopRepository.getShopById(shopId);
//	    
//	    // Lấy danh sách voucher từ cơ sở dữ liệu (giả sử bạn có phương thức tương tự)
//	    List<Object[]> shopVouchers = shopRepository.getShopVoucherById(shopId);
//	    
//	    List<Object[]> shopProduct = shopRepository.getSanPham(shopId);
//	    
//	    List<Object[]> shopProductNew = shopRepository.getSanPhamDesc(shopId);
//	    // Tạo một object chứa cả 2 danh sách để trả về client
//	    Map<String, Object> responseData = new HashMap<>();
//	    responseData.put("shopDetails", shopDetails);
//	    responseData.put("shopVouchers", shopVouchers);
//	    responseData.put("shopProduct", shopProduct);
//	    responseData.put("shopProductNew", shopProductNew);
//	    // Trả về kết quả dưới dạng ObjectRespone
//	    return ResponseEntity.ok(new ObjectRespone(200, "success", responseData));  
//	}
//	@GetMapping("/{id}/product")
//	public ResponseEntity<ObjectRespone> findProductByName(
//	        @PathVariable("id") Integer id,
//	        @RequestParam(defaultValue = "0") int page,
//@RequestParam(defaultValue = "10") int size,
//	        @RequestParam(value = "theLoaiIds", required = false) List<Integer> theLoaiIds,
//	        @RequestParam(required = false) List<Integer> thuongHieuIds,
//	        @RequestParam(required = false) List<Integer> mauSacIds,
//	        @RequestParam(required = false) List<Integer> kichThuocIds,
//	        @RequestParam(value = "giaMin",required = false,defaultValue = "0.0") Double giaMin,
//	        @RequestParam(value = "giaMax",required = false,defaultValue = "0.0") Double giaMax
//	        
//	) {
//	    // Khởi tạo các tham số nếu chúng là null
//		
//	    theLoaiIds = theLoaiIds == null ? new ArrayList<>() : theLoaiIds;
//	    thuongHieuIds = thuongHieuIds == null ? new ArrayList<>() : thuongHieuIds;
//	    mauSacIds = mauSacIds == null ? new ArrayList<>() : mauSacIds;
//	    kichThuocIds = kichThuocIds == null ? new ArrayList<>() : kichThuocIds;
//	    Pageable pageable = PageRequest.of(page, size);
//	    Page<Object[]> productPage = null;
//	    
//	    // Nếu chỉ tìm theo tên mà không có các bộ lọc khác
//	    if (theLoaiIds.isEmpty() && thuongHieuIds.isEmpty() && mauSacIds.isEmpty() && kichThuocIds.isEmpty()&& giaMin == 0 && giaMax == 0)  {
//	        productPage = shopRepository.findProductByName(id, pageable);
//	        System.out.println("Hello 1");
//	        
//	    } else {
//	    	System.out.println("Hello 2");
//	    	if (giaMin == 0 && giaMax == 0) {
//	    		
//				giaMin = (double) 0;
//				giaMax = (double) 999999999;
//				
//				
//			}else if(giaMin == 0 && giaMax != 0) {
//				
//				giaMin = (double) 0;
//			
//			}else if(giaMin != 0 && giaMax == 0){
//				
//				giaMax = giaMin;
//				giaMin = (double) 0;
//				
//			}
//	    	productPage = shopRepository.findSanPhamWithFilters(id, theLoaiIds,kichThuocIds,thuongHieuIds,mauSacIds,giaMin,giaMax,pageable);
//	    		
//	    	
//	    }
//	    List<Object[]> shopDetails = shopRepository.getShopById(id);
//	    // Lấy các bộ lọc dữ liệu để trả về cho frontend
//	    List<Object[]> fillterTheLoai = categoryRepository.getFillterLoaiSanPham();
//	    List<Object[]> fillterThuongHieu = thuongHieuRepository.getFillterThuongHieu();
//	    List<Object[]> fillterMauSac = mauSacRepository.getFillterMauSac();
//	    List<Object[]> fillterKichThuoc = kichThuocRespository.getFillterKichThuoc();
//
//	    // Đưa các bộ lọc vào Map để trả về cho frontend
//	    Map<String, List<Object[]>> filters = new HashMap<>();
//	    filters.put("theLoai", fillterTheLoai);
//	    filters.put("thuongHieu", fillterThuongHieu);
//	    filters.put("mauSac", fillterMauSac);
//	    filters.put("kichThuoc", fillterKichThuoc);
//
//	    // Chuẩn bị dữ liệu trả về, bao gồm cả danh sách sản phẩm và bộ lọc
//	    Map<String, Object> response = new HashMap<>();
//	    response.put("products", productPage);
//	    response.put("filters", filters);
//	    response.put("shopDetails", shopDetails);
//	    return ResponseEntity.ok(new ObjectRespone(200, "success", response));
//	}
//	 
//}



//package com.API.controller;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.API.repository.MauSacRepository;
//import com.API.repository.ShopRepository;
//import com.API.utils.ObjectRespone;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/shop")
//public class ShopController {
//	@Autowired
//	private ShopRepository shopRepository;
//	@GetMapping(value="/getall")
//	public ResponseEntity<ObjectRespone> getAllAccount() {
//		return ResponseEntity.ok(new ObjectRespone(200,"success",shopRepository.findAll()));
//	}
//	
//	@GetMapping(value = "/{shopId}")
//	public ResponseEntity<ObjectRespone> getShopById(@PathVariable int shopId) {
//		
//		
//	    // Lấy danh sách shop từ cơ sở dữ liệu
//	    List<Object[]> shopDetails = shopRepository.getShopById(shopId);
//	    
//	    // Lấy danh sách voucher từ cơ sở dữ liệu (giả sử bạn có phương thức tương tự)
//	    List<Object[]> shopVouchers = shopRepository.getShopVoucherById(shopId);
//	    
//	    List<Object[]> shopProduct = shopRepository.getSanPham(shopId);
//	    
//	    List<Object[]> shopProductNew = shopRepository.getSanPhamDesc(shopId);
//	    // Tạo một object chứa cả 2 danh sách để trả về client
//	    Map<String, Object> responseData = new HashMap<>();
//	    responseData.put("shopDetails", shopDetails);
//	    responseData.put("shopVouchers", shopVouchers);
//	    responseData.put("shopProduct", shopProduct);
//	    responseData.put("shopProductNew", shopProductNew);
//	    // Trả về kết quả dưới dạng ObjectRespone
//	    return ResponseEntity.ok(new ObjectRespone(200, "success", responseData));  
//	}
//	
//	 
//}