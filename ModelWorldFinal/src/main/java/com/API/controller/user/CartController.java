package com.API.controller.user;

import java.io.Console;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.cart.CartAdd;
import com.API.DTO.cart.VoucherAplly;
import com.API.model.Cart;
import com.API.model.ProductDetail;
import com.API.repository.CartRepository;
import com.API.repository.ProductDetailRepository;
import com.API.repository.VoucherSanRepository;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
public class CartController {
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	UserFilter userFilter;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	ProductDetailRepository productDetailRepository;
	
	@Autowired
	VoucherSanRepository voucherSanRepository;
	
	
	@GetMapping("/user/auth/voucher/getvouchersanapplys")
	public Object getMethodName() {
		return voucherSanRepository.getVoucherSanApply(userFilter.getAccount().getId());
	}
	
	
//	@PostMapping("/user/auth/cart/addtocart")
//	@Transactional
//	public Object addToCart(@RequestBody @Valid CartAdd cart) {
//		System.out.println(cart.getProductDetailId());
//		ProductDetail p=productDetailRepository.getProductDetailForCart(cart.getProductDetailId()).orElse(null);
////		System.out.println(p!=null);
//		if(p!=null&&p.getIsActive()==1) {
//			Cart c= cartRepository.getCartByProductIdAndAccountId(userFilter.getAccount().getId(),cart.getProductDetailId()).orElse(new Cart(cart.getProductDetailId(), userFilter.getAccount().getId(),0));
////			System.out.println(c.getId());
//			c.plusSoLuong(cart.getSoLuong());
//			if(c.getSoLuong()<p.getSoLuong()+1) {
//				cartRepository.save(c);
//				return new ObjectRespone(200,"Đã thêm sản phẩm vào giỏ hàng",c.getId());
//			}
//			return new ObjectRespone(400,"Số lượng sản phẩm trong cửa hàng hiện tại không đủ !!!",null);
//		}
//		return new ObjectRespone(400,"Sản phẩm tạm thời không mở bán !!!",null);
//	}
	
	@PostMapping("/user/auth/cart/addtocart")
	@Transactional
	public Object addToCart(@RequestBody @Valid CartAdd cart) {
//		System.out.println(cart.getProductDetailId());
		ProductDetail p=productDetailRepository.getProductDetailForCart(cart.getProductDetailId()).orElse(null);
//		System.out.println(p!=null);
		if(p!=null&&p.getIsActive()==1) {
			Cart c= cartRepository.getCartByProductIdAndAccountId(userFilter.getAccount().getId(),cart.getProductDetailId()).orElse(new Cart(cart.getProductDetailId(), userFilter.getAccount().getId(),0));
//			System.out.println(c.getId());
			c.plusSoLuong(cart.getSoLuong());
			if(c.getSoLuong()<p.getSoLuong()+1) {
				cartRepository.save(c);
				return new ObjectRespone(200,"Đã thêm sản phẩm vào giỏ hàng",c.getId());
			}
			return new ObjectRespone(400,"Số lượng sản phẩm trong cửa hàng hiện tại không đủ !!!",null);
		}
		return new ObjectRespone(400,"Sản phẩm tạm thời không mở bán !!!",null);
	}
	
	
	@GetMapping("/user/auth/getcountcart")
	public Object getCountCart() {
		return new ObjectRespone(200,"sucess", cartRepository.getCartWithLive(userFilter.getAccount().getId()));
	}
	
	
	
	@PostMapping("/user/auth/cart/deletecart/{id}")
	@Transactional
	public ObjectRespone deleteCart(@PathVariable(name="id") Integer id) {
		if(cartRepository.deleteCartById(id, userFilter.getAccount().getId())!=1) {
			return new ObjectRespone(400,"Sản phẩm trong giỏ hàng không hợp lệ.",null);
		}
		return new ObjectRespone(200,"success",null);
	}
	
	@PostMapping("/user/auth/cart/updatecart")
	public ObjectRespone updateCart (@Valid @RequestBody CartAdd cart) {
		ProductDetail p=cartRepository.getCartUpdate(cart.getId(),userFilter.getAccount().getId()).orElse(null);
		if(p!=null) {
			if(p.getIsActive()==1) {
				if(cart.getSoLuong()<p.getSoLuong()+1) {
					int a=cartRepository.updateCart(cart.getSoLuong(),cart.getId());
					return new ObjectRespone(200, "Cập nhật giỏ hàng thành công",null);
				}
				return new ObjectRespone(400, "Kho hàng hiện tại không đủ sản phẩm",null);
			}
			return new ObjectRespone(400, "Sản phẩm tạm thời ngừng hoạt động",null);
		}
		return new ObjectRespone(400, "Cart item is not valid",null);
	}
	
//	public int getIdAccount() {
//		return 1;
//	}
	@GetMapping("/user/auth/cart/getcart")
	public ObjectRespone fetCart() {
		return new ObjectRespone(200,"sucess", cartRepository.getCartWithLive(userFilter.getAccount().getId()).stream()
                .collect(Collectors.groupingBy(cart ->cart.getShopId())));
	}
	
	@PostMapping("/user/auth/cart/getcartinlist")
	public ObjectRespone fetCartInList(@RequestBody List<Integer>id) {
		return new ObjectRespone(200,"sucess", cartRepository.getCartInlist(userFilter.getAccount().getId(),id).stream()
                .collect(Collectors.groupingBy(cart ->cart.getShopId())));
	}
	
//	lấy các voucher hiện tại có thể áp dụng 
	@PostMapping("/user/auth/cart/getvoucherapply/{shopId}")
	public Object tess(@RequestBody List<getVoucherInlistProduct>ids,@PathVariable("shopId")Integer shopId) {
		
		List<VoucherAplly> l= cartRepository.getVoucherCanAplyProductList(ids.stream()
	            .map(getVoucherInlistProduct::getProductId)
	            .collect(Collectors.toList()),shopId,userFilter.getAccount().getId());
		l.forEach(v->{
			ids.forEach(i->{
				if(v.isInList(i.productId+"")) {
					v.addTotal(i.getGiaBan()*i.getSoLuong());
					v.orderTotal+=i.getGiaBan()*i.getSoLuong()-i.getGiaDaGiam();
				}
			});
			if(v.getDonToiThieu()<=v.getTotal()) {
				v.setCanApply(true);
				Double a;
				if(v.getLoaiVoucher()==1) {
					a=(v.getGiaTriGiam()/100)*v.orderTotal;
//					v.setPriceDiscount(v.getGiamToiDa()<1?a:v.getGiamToiDa()<a?v.getGiamToiDa():a);
					v.setPriceDiscount(v.getGiamToiDa()<1||a<=v.getGiamToiDa()?a:v.getGiamToiDa());
				}else {
					 a=v.orderTotal-v.getGiaTriGiam();
					v.setPriceDiscount(a>0?a:v.orderTotal);
				}
			}else {
				v.setCanApply(false);
			}
		});
		return new ObjectRespone(200, null, l);
	}
}
class getVoucherInlistProduct{
	 public Integer getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}
	public Float getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(Float giaBan) {
		this.giaBan = giaBan;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	Integer productId;
	private Double giaDaGiam;
	 public Double getGiaDaGiam() {
		return giaDaGiam;
	}
	public void setGiaDaGiam(Double giaDaGiam) {
		this.giaDaGiam = giaDaGiam;
	}
	Integer soLuong;
	 Float giaBan;
	
}
