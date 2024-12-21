package com.API.controller.saler;

import java.util.ArrayList;
import java.util.HashMap;
//import java.awt.print.Pageable;
//import java.io.Console;
//import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
//import org.springframework.data.repository.query.Param;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.API.DTO.ProductRequestDTO;
import com.API.globalException.CustomeException;
import com.API.model.ChiTietKhuyenMai;
import com.API.model.Product;
import com.API.model.ProductDetail;
import com.API.model.ProductImage;
import com.API.model.Shop;
import com.API.model.VoucherShop;
import com.API.repository.CategoryRepository;
import com.API.repository.KichThuocRespository;
import com.API.repository.MauSacRepository;
import com.API.repository.ProductDetailRepository;
import com.API.repository.ProductImageRepository;
import com.API.repository.ProductRepository;
import com.API.repository.ThuongHieuRepository;
import com.API.service.saler.ShopFilter;
import com.API.service.voucherService.VoucherSendAnnounceService;
import com.API.utils.ObjectRespone;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
public class ProductController {
	
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductDetailRepository productDetailRepository;
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    ShopFilter shopFilter;
    
	@Autowired
    private Cloudinary cloudinary;
	
	
	
//	@Autowired
//	CategoryRepository categoryRepository;
	@Autowired
	ThuongHieuRepository thuongHieuRepository;
	@Autowired
	MauSacRepository mauSacRepository;
	@Autowired
	KichThuocRespository kichThuocRespository;
	
	
	
	
	@GetMapping("/PromotionalProducts")
	public ResponseEntity<ObjectRespone> PromotionalProducts(
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
		        productPage = productRepository.getPromotionalProducts(pageable);
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
		    	productPage = productRepository.getAndFindPromotionalProducts(theLoaiIds,kichThuocIds,thuongHieuIds,mauSacIds,giaMin,giaMax,pageable);
		    		
		    	
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
	
	
	
	
	
	
	
	
	
	
    
//    @Autowired
//    private VoucherSendAnnounceService announceService;
    
    // THÊM SẢN PHẨM
    
    
    // đoạn mới 
    
    @PostMapping("/sale/product/deleteproductimage")
    public ObjectRespone deleteProductImage(@RequestParam("imageId")Integer imageId,@Param("productId")Integer productId) {
    	Integer id=productRepository.findByIdInteger(shopFilter.getShopId(),productId).orElse(-1);
    	if(id!=-1&&productRepository.deleteProductImageByIdAndProductId(imageId, productId)==1) {
        	return new ObjectRespone(200,"success", null);
    	}
    	return new ObjectRespone(400,"Yêu cầu xóa ảnh không thành công vui lòng kiểm tra lại thông tin", null);
    }
    
    
    @PostMapping("/sale/product/uploadmultiimage")
    public ObjectRespone addImages(@RequestParam("files") MultipartFile[] files,@RequestParam("productId")Integer productId) throws java.io.IOException {
        List<ProductImage>productImages=new ArrayList<ProductImage>();
        Integer id=productRepository.findByIdInteger(shopFilter.getShopId(),productId).orElse(-1);
    	if(id!=-1) {
    		Product product=new Product(productId);
            for (MultipartFile file : files) {
                try {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    productImages.add(new ProductImage(uploadResult.get("url").toString(),product));
                } catch (IOException e) {
                    return new ObjectRespone(500,"failes",null);
                }
            }
            productImageRepository.saveAllAndFlush(productImages);
            return new ObjectRespone(200,"success",productImages);
    	}
        return new ObjectRespone(400,"Sản phẩm không hợp lệ",null);
    }
    
    
    
//    public updateBienThe
    
    
    
    ////////////////////
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @PostMapping("/sale/product/addproduct")
    @Transactional
    public ObjectRespone addProduct(@Valid @RequestBody Product product) {
    	Integer c=categoryRepository.findByIdCategoryId(product.getCategory().getId()).orElse(0);
    	product.setShop(new Shop(shopFilter.getShopId()));
    	if(c==1) {
    		if(product.getProductDetails().size()>0) {
    			try {
                    productRepository.save(product);
                } catch (Exception ex) {
                    return new ObjectRespone(400, "Thuong hiệu không hợp lệ !!", null);
                }
                try {
                	product.getProductDetails().forEach(v->v.setProduct(product));
                	productDetailRepository.saveAll(product.getProductDetails());
        		} catch (Exception e2) {
        			throw new CustomeException("Vui lòng kiểm tra kích thước và màu sắc của biến thể");
        		}
                product.getProductImages().forEach(v->v.setProduct(product));
                productImageRepository.saveAll(product.getProductImages());
                return new ObjectRespone(200,"Thêm thành công sản phẩm !!!",null);
    		}
    		return new ObjectRespone(400, "Chọn ít nhất một biến thể", null);   
    	} 
    	return new ObjectRespone(400, "Mặt hàng không hợp lệ", null);  
    }
    
    
    
 // CẬP NHẬT SẢN PHẨM 
    
    @PostMapping("/sale/product/updateproduct")
    @Transactional
    public ObjectRespone uppdateProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
    	Product product= productRepository.findById(productRequest.getProduct().getId()).orElse(null);
        if(product!=null) {
        	Set<ProductDetail> productDetails= productRequest.getProductDetail(); 
            if(!product.getProductDetails().containsAll(productRequest.getProduct().getProductDetails())) {
            	return new ObjectRespone(400,"Danh sách biến thể cập nhật không hợp lệ !!!",null);
            }
            boolean hasCommonElements = product.getProductDetails().stream().anyMatch(b -> productDetails.stream() .anyMatch(v ->v.equalsSetNew(b)));
            if(hasCommonElements) {
            	return new ObjectRespone(400,"Danh sách biến thể thêm mới có trùng lắp !!!",null);
            }
            productDetails.forEach(v->v.setProduct(product));
            try {
                productDetailRepository.saveAll(productDetails);
    		} catch (Exception e) {
    			throw new CustomeException("Danh sách sản phẩm biến thể không hợp lệ");
    		}
            productRequest.getProduct().getProductDetails().forEach(v->{
            	productDetailRepository.updateProductDetail(v.getSoLuong(), v.getGiaBan(),v.getHinhAnh(),v.getId());
            });
            productRequest.getProduct().setShop(new Shop(shopFilter.getShopId()));
            productRepository.save(productRequest.getProduct());
            return new ObjectRespone(200,"Cập nhật thành công sản phẩm !!!",null);
        }
    	return new ObjectRespone(403,"Bạn không có quyền này !!!",null);
    }

    @PostMapping("/sale/product/deleteallvariant")
    public Object deleteAllProductVariant(@RequestBody List<Integer> idProductDetails,@RequestParam(name = "id",defaultValue = "-1")Integer id) {
    	if( productRepository.getStateProductById(id,shopFilter.getShopId())!=2) {
    		
    	}
    	return new ObjectRespone(400,"Product is not delete because locked by admin",null);
    }
    
    // * LẤY DANH SÁCH SẢN PHẨM.
    
    // lấy tất cả sản phẩm
    
    @PostMapping("/sale/product/getall")
    public Object getAllSanPhamInShop(@RequestParam(name="page",defaultValue = "0")Integer page,
    		@RequestParam(name="pagesize",defaultValue = "5")Integer pageSize,
    		@RequestParam(name="key",defaultValue = "")String key,
    		@RequestParam(name="state",defaultValue = "1")Integer state,
    		@RequestBody List<Integer> ids,
    		@RequestParam(name="sortType",defaultValue = "0")Integer sortType) {
    	Sort sort=null;
    	switch (sortType) {
		case 0: {
			sort = Sort.by(Sort.Order.desc("id"));
			 break;
		}
		case 1:{
			sort = Sort.by(Sort.Order.asc("tenSanPham"));
			 break;
		}
		default:
			sort = Sort.by(Sort.Order.desc("soLuongDaBan"));
		}
    	Pageable pageable=PageRequest.of(page, pageSize,sort);
    	Page<Product>products=null;
    	if(ids.size()==0) {
        	products= productRepository.findAllProductsWithDetails(shopFilter.getShopId(),pageable,state,"%"+key+"%");
    	}else {
    		products= productRepository.findAllProductsWithDetailsAndIds(shopFilter.getShopId(),pageable,state,"%"+key+"%",ids);
    	}
    	return new Content(products, productRepository.getCountKhuyenMaiInProductList(products.getContent()),productRepository.getIbject(products.getContent()));
    }
    
    @PostMapping("/sale/product/updatestatus")
    @Transactional
    public Object postMethodName(@RequestBody List<Integer> ids,@RequestParam(name = "state",defaultValue = "0")Integer state) {
       if(state >2||state<0) {
    	   return new ObjectRespone(400,"Mã trạng thái không hợp lệ",null);
       }else {
    	  if( productRepository.updateStatusByIdsAndShopId(state, ids,shopFilter.getShopId())==ids.size()) {
    		  return new ObjectRespone(200,"Cập nhật thành công",null);
    	  }
    	  throw new CustomeException("Danh sách sản phẩm cập nhật không hợp lệ");
       }
    }
    

    
    @GetMapping("/sale/product/getproductdetailbyproductid")
    public ObjectRespone getProductDetailByProductId(@RequestParam(name = "id", defaultValue = "-1")Integer id) {
    	return new ObjectRespone(200,"success",productRepository.getProductDetailByProductId(id));
    }
    
    
    @GetMapping("/sale/product/getallbystatus")
    public Object getAllSanPhamInShop(@RequestParam(name = "idState",defaultValue ="1")Integer status,@RequestParam(name="page",defaultValue = "0")Integer page,@RequestParam(name="pagesize",defaultValue = "5")Integer pageSize) {
    	org.springframework.data.domain.Pageable pageable =  PageRequest.of(page,pageSize);
    	Page<Product>products= productRepository.findAllProductsWithDetails(1,status,pageable);
    	List<ProductDetail> productDetails = productDetailRepository.findByProductIds(products.getContent()); 
    	Map<Integer, Set<ProductDetail>> productDetailMap = productDetails.stream()
    	        .collect(Collectors.groupingBy(
    	                pd -> pd.getProduct().getId(), 
    	                Collectors.toSet()
    	        ));
    	 products.getContent().forEach(v->{
    		 v.setProductDetails(productDetailMap.get(v.getId()));
    	 });
    	return products;
    }
    
    
    // lay san pham chi tiet.
    @GetMapping("/sale/product/getproductdetail")
    public Object getProductDetail(@RequestParam(name = "id",defaultValue = "-1")Integer id ) {
    	Product p=productRepository.findByIdAndShopId(id,shopFilter.getShopId()).orElse(null);
    	if(p!=null) {
    		return new ObjectRespone(200,"success",p);
    	}
    	return new ObjectRespone(400,"failed",null);
    }
    
    
    
    
    
    
    
//    @GetMapping(value = "/product/getall")
//    public ResponseEntity<ObjectRespone> getAllProducts() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Product p= productRepository.findProductWithDetailsById(680);
//
//        try {
//            String jsonString = objectMapper.writeValueAsString(p);
//            System.out.println(jsonString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok(new ObjectRespone(200, "success", productRepository.findById(680).get().getProductDetails()));
//    }
}

class Content{
	private Page<Product> page;
	List<Object[]> listReport;
	public Content(Page<Product> page, List<ChiTietKhuyenMai> chiTietKhuyenMail,List<Object[]>l) {
		this.page = page;
		this.listReport=l;
//		System.out.println(listReport.su);
		this.chiTietKhuyenMail = chiTietKhuyenMail;
	}
	public List<Object[]> getListReport() {
		return listReport;
	}
	public void setListReport(List<Object[]> listReport) {
		this.listReport = listReport;
	}
	private List<ChiTietKhuyenMai> chiTietKhuyenMail;
	public Page<Product> getPage() {
		return page;
	}
	public void setPage(Page<Product> page) {
		this.page = page;
	}
	public List<ChiTietKhuyenMai> getChiTietKhuyenMail() {
		return chiTietKhuyenMail;
	}
	public void setChiTietKhuyenMail(List<ChiTietKhuyenMai> chiTietKhuyenMail) {
		this.chiTietKhuyenMail = chiTietKhuyenMail;
	}
}
