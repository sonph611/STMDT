package com.API.controller.saler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.ProductLiveDTO;
import com.API.model.LiveSession;
import com.API.model.Live_Product;
import com.API.model.Product;
import com.API.model.ProductDetail;
import com.API.model.Shop;
import com.API.repository.LiveDetail;
import com.API.repository.LiveSessionRepository;
import com.API.repository.ProductDetailRepository;
import com.API.repository.ProductRepository;
import com.API.service.saler.ShopFilter;
import com.API.utils.ObjectRespone;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.val;

@RestController
@CrossOrigin("*")
public class LiveController {
	
	@Autowired
	ProductRepository p;
	@Autowired
	LiveSessionRepository ls;
	@Autowired
	LiveDetail ld;
	@Autowired
	ShopFilter s;
	
	@Autowired
	ProductDetailRepository pp;
	
//	@GetMapping("/user/auth/productinlive")
//	public ObjectRespone getProductInLive(@RequestParam("id")Integer id) {
//		return new ObjectRespone(200,"",ld.getListByIdLive(id));
//	}
	
//	@Transactional
	@GetMapping("/clearallpromotion")
	public ObjectRespone getProductInLive(@RequestParam("id")Integer id) {
		Integer a=ls.clearAllPromotion(id);
//		System.out.println(a);
		return new ObjectRespone(200,"Rồi á bạn ơi",ld.getListByIdLive(id));
	}
	
	
	@PostMapping("/live/endlive")
	public Object postMethodName(@RequestParam("id")Integer id,@RequestParam("countCart")Integer countCart,@RequestParam("countLike")Integer countLike) {
		Integer a=ls.updateEndLive(id,countLike,countCart);
		if(a==1) {
			return new ObjectRespone(200,null,null);
		}else {
			return new ObjectRespone(400, null, null);
		}
	}
	
	
	@GetMapping("/sale/productnotinlive")
	public ObjectRespone getProductInLive(@RequestParam("key")String key,@RequestParam(name="page",defaultValue = "0")Integer page) {
		key="%"+key+"%";
		Page<Product>products= p.getProductNotInlive(s.getShopId(),PageRequest.of(page,10),key);
		return new ObjectRespone(200,"",products);
	}
	@Transactional
	@PostMapping("/sale/live/addproduct")
	public Object postMethodName(@RequestParam("productId")Integer productId,@RequestParam("liveId")Integer lived) {
		Integer a=p.getStateProductById(productId, s.getShopId());
		if(a!=null) {
			if(a==1) {
				if(ls.getLiveIdByIdAndShopId(lived, s.getShopId())!=-1) {
					try {
						Live_Product l=new Live_Product();
						l.setProduct(new Product(productId));
						l.setLive(new LiveSession(lived));
						l.setGiaGiam(0.0);
						l.setSoLuongGioiHan(0);
						l.setSoLuocDaDung(0);
						ld.save(l);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new ObjectRespone(200, null, null);
				}
				return new ObjectRespone(400,"Phiên live không hợp lệ",null);
			}
			return new ObjectRespone(400,"Sản phẩm tạm thời ngưng bán",null);
		}
		return new ObjectRespone(400,"Không tìm thấy sản phẩm",null);
	}
	
//	public ObjectRespone updateEndTimeOut() {
//		return new ObjectRespone(200, null, ld)
//	}
//	
	
	@GetMapping("/sale/productinlives")
	public ObjectRespone getProductInLives(@RequestParam("id")Integer id) {
		List<ProductLiveDTO> l=ld.getProductInlive(id);
		List<Integer> productIds = l.stream()
	            .map(dto -> dto.getP().getId())
	            .collect(Collectors.toList());
		List<ProductDetail> productDetails = pp.findByProductIdsProductLive(productIds); 
    	Map<Integer, Set<ProductDetail>> productDetailMap = productDetails.stream()
    	        .collect(Collectors.groupingBy(
    	                pd -> pd.getProduct().getId(), 
    	                Collectors.toSet()
    	        ));
    	 l.forEach(v->{
    		 v.getP().setProductDetails(productDetailMap.get(v.getP().getId()));
    	 });
		
		return new ObjectRespone(200,"",l);
	}

	
	@PostMapping("/sale/live/createlive")
	public Object postMethodName(@RequestBody @Valid LiveAdd live) {
		ArrayList<Live_Product>l=new ArrayList<Live_Product>();
		if(live.getProductIds().size()<1) {
			
		}else {
			System.out.println(p.countByIdInAndShopIdAndActive(live.getProductIds(),s.getShopId()));
			if(p.countByIdInAndShopIdAndActive(live.getProductIds(),s.getShopId())==live.getProductIds().size()) {
				live.getProductIds().forEach(v->{
					l.add(new Live_Product(v,live.getLive()));
				});
			}else {
				return new ObjectRespone(400,"Danh sách sản phẩm của shop không hợp lệ",null);
			}
		}
		live.getLive().setShop(new Shop(s.getShopId()));
		ls.save(live.getLive());
		if(l.size()>0) {
			ld.saveAll(l);
		}
		return new ObjectRespone(200,"Khởi tạo thành công live",null);
	}
	
	
	@GetMapping("/sale/live/getall")
	public ObjectRespone getMethodName(@RequestParam(name="page",defaultValue = "0")Integer page) {
		return new ObjectRespone(200,"success",ls.getAllLiveByShopId(s.getShopId(),PageRequest.of(page,10)));
	}
	
	@GetMapping("/sale/checklive")
	public Object checkShopIsLive(@RequestParam("liveId")Integer id) {
		Integer a=ls.checkShopLive(s.getShopId(),id).or(-1);
		if(a==-1) {
			return new ObjectRespone(200, null, null);
		}else {
			return new ObjectRespone(400, null,a);
		}
	}
	
	@PostMapping("/sale/live/thietlapgia")
	public Object thietLapGia(@RequestBody @Valid List<LiveSetPrice>ids,@RequestParam("liveId")Integer liveId) {
		LiveSession l=ls.getLiveByIdAndIsStart(liveId,s.getShopId()).orNull();
//		System.out.println(liveId);
//		System.out.println(s.getShopId());
//		System.out.println(l!=null);
		if(l!=null) {
			ids.stream().map(LiveSetPrice::getId) .collect(Collectors.toList()).forEach(v->System.out.println(v));
			if(ls.getLiveItemInIds(ids.stream().map(LiveSetPrice::getId) .collect(Collectors.toList()), liveId)==ids.size()) {
				ids.forEach(v->{
					ls.upDateSoLuongAndGiaBan(v.getId(),v.getSoLuongGioiHan(),v.getGiaGiam());
				});
				return new ObjectRespone(200, "Thiết lập giá thành công", null);
			}else {
				return new ObjectRespone(400, "Danh sách item không hợp lệ", null);
			}
			
		}
		return new ObjectRespone(400, "Phiên live không hợp lệ, vui lòng kiểm tra lại phiên live của bạn", null);
	}
	
}

class LiveAdd{
	private LiveSession live;
	private Set< Integer> productIds;
	public LiveSession getLive() {
		return live;
	}
	public void setLive(@Valid LiveSession live) {
		this.live = live;
	}
	public Set<Integer> getProductIds() {
		return productIds;
	}
	public void setProductIds(Set<Integer> productIds) {
		this.productIds = productIds;
	}
	
}

class LiveSetPrice{
	@NotNull(message="Chưa có item của sản phaamr set giá")
	private Integer id;
	@NotNull(message="Chưa có số lượng")
	@Min(value =0, message = "số lượng là 0 nếu không giới hạn")
	private Integer soLuongGioiHan;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSoLuongGioiHan() {
		return soLuongGioiHan;
	}
	public void setSoLuongGioiHan(Integer soLuongGioiHan) {
		this.soLuongGioiHan = soLuongGioiHan;
	}
	public Integer getGiaGiam() {
		return giaGiam;
	}
	public void setGiaGiam(Integer giaGiam) {
		this.giaGiam = giaGiam;
	}
	@NotNull(message="Chưa có giá giảm")
	@Min(value =0, message = "Giá giảm vui lòng lớn hơn 0")
	@Max(value =99, message = "Giá giảm vui lòng nhỏ hơn 100")
	private Integer giaGiam;
	
}




