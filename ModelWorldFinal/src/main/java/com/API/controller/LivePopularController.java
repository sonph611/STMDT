package com.API.controller;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.ProductLiveDTO;
import com.API.model.LiveSession;
import com.API.model.ProductDetail;
import com.API.repository.LiveDetail;
import com.API.repository.LiveSessionRepository;
import com.API.repository.ProductDetailRepository;
import com.API.repository.ProductRepository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class LivePopularController {
	@Autowired
	LiveDetail ld;
	@Autowired
	ProductDetailRepository p;
	
	@Autowired
	LiveSessionRepository lp;
	
	@GetMapping("/user/auth/productinlive")
	public ObjectRespone getProductInLive(@RequestParam("id")Integer id) {
		return new ObjectRespone(200,"",ld.getListByIdLive(id));
	}
	
	@GetMapping("/user/auth/productinlives")
	public ObjectRespone getProductInLives(@RequestParam("id")Integer id) {
		List<ProductLiveDTO> l=ld.getProductInlive(id);
		List<Integer> productIds = l.stream()
	            .map(dto -> dto.getP().getId())
	            .collect(Collectors.toList());
		List<ProductDetail> productDetails = p.findByProductIdsProductLive(productIds); 
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
	
	@GetMapping("/getinfolive")
	public Object getInfoLive(@RequestParam("id")Integer id) {
		LiveSession l=lp.getInfoLive(id).orNull();
		if(l!=null) {
			return new ObjectRespone(200,"", l);
		}else {
			return new ObjectRespone(404,"Không tìm thấy phiên live", l);
		}
	}
	
	
}
