package com.API.controller.user;

import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.cart.VoucherAplly;
import com.API.filter.UserFilter;
import com.API.model.KhuyenMai;
import com.API.model.Product;
import com.API.model.SanPhamYeuThich;
import com.API.model.VoucherShop;
import com.API.repository.KhuyenMaiRePository;
import com.API.repository.ProductDetailRepository;
import com.API.repository.ProductRepository;
import com.API.repository.SanPhamYeuThichRepository;
import com.API.repository.VoucherRepository;
import com.API.utils.ObjectRespone;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "*")
public class ProductUserController {
	@Autowired 
	ProductRepository p;
	
	@Autowired
	com.API.service.saler.UserFilter userFilter;
	
	@Autowired
	SanPhamYeuThichRepository sanPhamYeuThichRepo;
	
	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired	
	KhuyenMaiRePository khuyenMaiRepository;
	@Autowired
	ProductDetailRepository pr;
	ObjectMapper objectMapper = new ObjectMapper();
	@GetMapping("/user/product/getAll")
	public Object getMethodName() {
		Product p1=p.findById(680).get();
        try {
			return objectMapper.writeValueAsString(p1);
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}
		return pr.getObject();
	}
	
	
	
	@PostMapping("/user/auth/product/release/{productid}")
	@Transactional
	public ObjectRespone release(@PathVariable("productid")Integer productId) {
		if(p.getProuctIdById(productId).orElse(null)!=null) {
			
			SanPhamYeuThich sanPhamYeuThich=sanPhamYeuThichRepo.getProductFaByProductIdAndAccountId(productId,userFilter.getAccount().getId()).orNull();
			if(sanPhamYeuThich==null) {
				sanPhamYeuThichRepo.save(new SanPhamYeuThich(userFilter.getAccount(),new Product(productId)));
				return new ObjectRespone(200,"",true);
			}
			sanPhamYeuThichRepo.delete(sanPhamYeuThich);
			return new ObjectRespone(200,"Không tìm thấy sản phẩm",false);
		}
		return new ObjectRespone(400,"Không tìm thấy sản phẩm",null);
	}
	
	@GetMapping("/user/auth/product/isfavorite/{id}")
	public ObjectRespone getIsFavorite(@PathVariable("id")Integer id) {
		return new ObjectRespone(200, null,sanPhamYeuThichRepo.getProductFaByProductIdAndAccountId(id,userFilter.getAccount().getId()).orNull()!=null);
	}
	
	@PostMapping("/user/auth/favorite/delete")
	public Object postMethodName(@RequestParam(name="id",defaultValue = "-1")Integer id ) {
		Integer ids=sanPhamYeuThichRepo.getIdByIdAndAccountId(id,userFilter.getAccount().getId()).orNull();
		if(ids!=null) {
			sanPhamYeuThichRepo.deleteById(ids);
			return new ObjectRespone(200,"Xóa thành công...", null);
		}
		return new ObjectRespone(400,"Không tìm thấy sản phẩm yêu thích", null);
	}
	
	
	@GetMapping("/user/auth/myfavorite")
	public ObjectRespone getMyFavorite(@RequestParam(name="page",defaultValue = "0")Integer page,@RequestParam(name="key",value ="")String key) {
		key="%"+key.trim()+"%";
		return new ObjectRespone(200, null, sanPhamYeuThichRepo.getAllSanPhamYeuThich(PageRequest.of(page, 6),userFilter.getAccount().getId(),key));
	}
	
	
	@PostMapping("/user/auth/product/delete/{id}")
	public ObjectRespone deleteProductFavorite(@PathVariable("id")Integer productId) {
//		if(p.getProuctIdById(productId).orElse(null)!=null) {
			SanPhamYeuThich sanPhamYeuThich=sanPhamYeuThichRepo.getProductFaByProductIdAndAccountId(productId,userFilter.getAccount().getId()).orNull();
			if(sanPhamYeuThich==null) {
				sanPhamYeuThichRepo.save(new SanPhamYeuThich(userFilter.getAccount(),new Product(productId)));
			}
//			return new ObjectRespone(200,"Không tìm thấy sản phẩm",null);
//		}
		return new ObjectRespone(400,"Không tìm thấy sản phẩm yêu thích trong danh sách.",null);
	}
	
	
	@GetMapping("/product/productdetail/{id}")
	public Object getProductdetail(@PathVariable("id")Integer id) {
		Product p1=p.findById(id).orElse(null);
		
		if(p1!=null) {
			Map<String,Object>m=new HashMap<String, Object>();
			m.put("product",p1);
			KhuyenMai k=khuyenMaiRepository.getKhuyenMaiByProductId(id);
			if(k!=null) {
				k.setChitietKhuyenMai(null);
				m.put("khuyenMai",k);
			}
//			m.put("countProduct", 11);
//			System.out.println("Thêm vào cáu gì vậy bạn ơi ");
//			System.out.println(m.get("countProduct"));
//			countProductInShopByShopid
			return new ObjectRespone(200,"success",m);
		}
		return new ObjectRespone(400,"Rất tiết, không tìm thấy sản phẩm !!!",p1);
	}
	
//	@GetMapping("/user/product/productdetail/{id}")
//	public Object getProductdetail(@PathVariable("id")Integer id) {
//		Product p1=p.findById(id).orElse(null);
//		if(p1!=null) {
//			Map<String,Object>m=new HashMap<String, Object>();
//			m.put("product",p1);
//			KhuyenMai k=khuyenMaiRepository.getKhuyenMaiByProductId(id);
//			if(k!=null) {
//				k.setChitietKhuyenMai(null);
//				m.put("khuyenMai",k);
//			}
//			return new ObjectRespone(200,"success",m);
//		}
//		return new ObjectRespone(400,"Rất tiết, không tìm thấy sản phẩm !!!",p1);
//	}
	
	
	@GetMapping("/voucher/gevoucherbyproductid/{productId}")
	public Object getMethodName(@PathVariable("productId") Integer productId) {
		List<VoucherAplly>l=voucherRepository.getVoucherByProductId(productId,userFilter.getAccount().getId());
		return new ObjectRespone(200, null, l);
	}
	
	
}

class ProductDetailRespone{
	private Product product;
	private KhuyenMai khuyenMai;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}
	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	
}



