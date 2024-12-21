package com.API.controller.saler;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.Account;
import com.API.model.DiaChi;
import com.API.model.DiaChiShop;
import com.API.model.Shop;
import com.API.repository.AccountRepository;
import com.API.repository.DiaChiRepository;
import com.API.repository.DiaChiShopRepository;
import com.API.repository.ShopRepository;
import com.API.service.MailService;
import com.API.service.TokenService;
import com.API.service.saler.ShopFilter;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin("*")
public class ShopControllerSale {
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	MailService m;
	
	@Autowired
	private AccountRepository a;
	
	@Autowired
	ShopRepository s;
	
	@Autowired
	DiaChiShopRepository diaChiShopRepository;
	
	@Autowired
	DiaChiRepository dc;
	
	@Autowired
	AccountRepository ac;
	
	@Autowired
	UserFilter u;
	
	@Autowired
	TokenService t;
	
	@Autowired
	ShopFilter shopFilter;
	
	@GetMapping("/sale/shop/shopinfo")
	public Object getShop() {
		Shop s=shopRepository.getShopByIdAndAccountId(shopFilter.getShopId(),shopFilter.getAccountId()).orElse(null);
		if(s!=null) {
			s.setAccount(null);
			return new ObjectRespone(200,"success",s);
		}else {
			return new ObjectRespone(400,"Không tìm thấy shop",null);
		}
	}
	
	@PostMapping("/sale/shop/update")
	public Object postMethodName(@Valid @RequestBody Shop shop) {
		Shop a=shopRepository.getShopByShopIdAndAccountId(shopFilter.getShopId(),shopFilter.getAccountId()).orElse(null);
		if(a!=null) {
			shop.setShopId(shopFilter.getShopId());
			Integer id=s.getShopIdByShopName(shop.getShopName()).orElse(-1);
			if(id==-1||id==shopFilter.getShopId()) {
				shop.setAccount(new Account(shopFilter.getAccountId()));
				shop.setDiaChi(a.getDiaChi());
				shopRepository.save(shop);
				return new ObjectRespone(200,"Cập nhật shop thành công",null);
			}
			
			return new ObjectRespone(400,"Têm shop đã được đăng ký, vui lòng thử tên khác",null);
		}
		return new ObjectRespone(400,"Không tìm thấy shop, vui lòng kiểm tra lại thông tinh và tình trạng của shop",null);
	}
	
	
	@GetMapping("/sale/shop/getaddress")
	public Object getMethodName() {
		
		return  dc.getByShopId(shopFilter.getShopId());
	}
	
	@Transactional
	@PostMapping("/sale/shop/updateaddress")
	public Object postMethodName(@RequestBody @Valid DiaChi dcs, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        String firstError = bindingResult.getAllErrors().get(0).getDefaultMessage();
	        return new ObjectRespone(400, "Lỗi: " + firstError, null);
		}
		Integer id=dc.getIdDiaChiByShopId(shopFilter.getShopId());
		dcs.setId(id);
		dcs.setShop(new Shop(shopFilter.getShopId()));
		dc.save(dcs);
		return new ObjectRespone(200,"Cập nhật địa chỉ shop thành công", null);
	}
	
	
	// ĐĂNG KÝ SHOP
	@PostMapping("/user/auth/createshop")
	public Object postMethodNames(@RequestBody @Valid ShopAdd ss, BindingResult result) {
//		if(s.getShopByAccountId(u.getAccount().getId()).orElse(-1)!=-1) {
			if (result.hasErrors()) {
	            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
	            return new ObjectRespone(400, errorMessage, null);
	        }
			if(shopRepository.getShopByAccountId(u.getAccount().getId()).orElse(-1)!=-1) {
				return new ObjectRespone(400,"Tài khoản đã đăng ký shop",null);
			}
			if(s.getShopIdByEmail(ss.getShop().getEmail()).orElse(null)!=null) {
				return new ObjectRespone(400,"Địa chỉ mail đã có shop đăng ký email",null);
			}
			if(t.validateToken(ss.getToken())) {
				String aa=t.getOrderInfoFromToken(ss.getToken());
				if (aa.substring(0, aa.indexOf(" ")).equals(ss.getShop().getEmail()) && aa.substring(aa.lastIndexOf(" ")).trim().equals(ss.getValidToken())) {
					// kiểm tra xem tên shop có trùng khoong
					if(s.getShopIdByShopName(ss.getShop().getShopName().trim()).orElse(-1)==-1) {
						a.updateSeller(u.getAccount().getId());
						String sdt=a.getPhhoneByAccountId(u.getAccount().getId());
						ss.getDiaChi().setShop(ss.getShop());
						ss.getDiaChi().setIsShop(1);
						ss.getShop().setAccount(u.getAccount());
						ss.getShop().setTrangThai(1);
						shopRepository.save(ss.getShop());
						dc.save(ss.getDiaChi());
						return new ObjectRespone(200,"Đăng ký shop thành công",null);
					}
					return new ObjectRespone(400,"Tên shop đã tồn tại vui lòng nhập tên shop khác",null);
				}else {
					return new ObjectRespone(400,"Mã xác thực email không hợp lệ",null);
				}
			}else{
				return new ObjectRespone(400,"Mã xác thực email đã quá hạn vui lòng thử lại",null);
			}
//		}
//		return new ObjectRespone(400,"Tài khoản này đã đăng ký shop",null);
	}
	
	@PostMapping("/user/auth/validmail")
	public Object getMethodNamse(@RequestParam(name="email",defaultValue = "")String email) {
		String a="";
		String u=UUID.randomUUID().toString();
		try {
			 a=t.generateToken(email+" "+u);
		} catch (Exception e) {
		}
		m.sendSimpleEmail(email,"Mã xác thực email shop","Mã xác thực của bạn là : "+u);
		return new ObjectRespone(200,"Đã gửi maxacs thực bạn có thể nhập lại (Thợi hạn 5 phút)",a);
	}
}

class ShopAdd{
	@Valid
	private Shop shop;
	@Valid
	private DiaChi diaChi;
	@NotNull(message = "Cho nhận được token xác thực")
	private String token;
	@NotNull(message = "Cho nhận được token xác thực")
	private String validToken;
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public DiaChi getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(DiaChi diaChi) {
		this.diaChi = diaChi;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getValidToken() {
		return validToken;
	}
	public void setValidToken(String validToken) {
		this.validToken = validToken;
	}
}




