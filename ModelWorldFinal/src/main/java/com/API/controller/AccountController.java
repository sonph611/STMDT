package com.API.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.AccountDTO;
import com.API.globalException.CustomeException;
import com.API.model.Account;
import com.API.model.DiaChi;
import com.API.model.Shop;
import com.API.repository.AccountRepository;
import com.API.repository.DiaChiRepository;
import com.API.service.AccountService;
import com.API.service.MailService;
import com.API.service.SerPassword;
import com.API.service.TokenService;
import com.API.service.jwtToken;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
public class AccountController {

	@Autowired
	AccountService accountService;
	@Autowired
	jwtToken jwtToken;

	@Autowired
	UserFilter userFilter;
	
	@Autowired
	SerPassword serPass;
	@Autowired
	AccountRepository accountRePository;
	
	@Autowired
	DiaChiRepository diaChiRepository;
	@Autowired
	TokenService t;
	
	@Autowired
	MailService m;
	
	
	@PostMapping("/user/auth/changemail")
	public Object postMethodNames(@RequestParam("email")String email,
								@RequestParam("token")String token,
								@RequestParam("validtoken")String validToken) {

		if(accountRePository.getUserIdByEmail(email.trim()).or(-1)==-1&&accountRePository.getShopIdByEmail(email.trim()).or(-1)==-1) {
			if(t.validateToken(token.trim())) {
				String aa=t.getOrderInfoFromToken(token);
				if (aa.substring(0, aa.indexOf(" ")).equals(email) && aa.substring(aa.lastIndexOf(" ")).trim().equals(validToken)) {
					if(accountRePository.updateEmail(email,userFilter.getAccount().getId())==1) {
						return new ObjectRespone(200,"Đổi email thành công",null);
					}
					return new ObjectRespone(400,"Cập nhật email thất bại vui lòng thử lại sao",null);
				}else {
					return new ObjectRespone(400,"Mã xác thực email hoặc email không hợp lệ",null);
				}
			}else{
				return new ObjectRespone(400,"Mã xác thực email đã quá hạn vui lòng thử lại",null);
			}
		}
		return new ObjectRespone(400,"Đã có shop đăng ký email này vui lòng thử lại sao...",null);
			
//		}
//		return new ObjectRespone(400,"Tài khoản này đã đăng ký shop",null);
	}
	
	@PostMapping("/user/auth/validmaailchange")
	public Object getMethodNamse(@RequestParam(name="email",defaultValue = "")String email) {
		String a="";
		if(accountRePository.getUserIdByEmail(email.trim()).or(-1)==-1&&accountRePository.getShopIdByEmail(email.trim()).or(-1)==-1) {
			String u= accountService.randomOTP();
			try {
				 a=t.generateToken(email.trim()+" "+u);
			} catch (Exception e) {
			}
			m.sendSimpleEmail(email.trim(),"Mã xác thực email shop","Mã xác thực của bạn là : "+u);
			return new ObjectRespone(200,"Đã gửi mã thực đến mail của bạn (Thời hạn 5 phút)",a);
		}
		return new ObjectRespone(400,"Email đã có tài khoản đăng ký vui lòng thử lại sao...",null);
		
	}
	
	
	@GetMapping("/user/auth/getalldiachi")
	public Object getMethodName() {
		return new ObjectRespone(200,"", diaChiRepository.getsDiaChiId(userFilter.getAccount().getId()));
	}
	
	
	@Transactional
	@PostMapping("/user/auth/updateaddress")
	public Object postMethodName(@RequestBody @Valid DiaChi dcs, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        String firstError = bindingResult.getAllErrors().get(0).getDefaultMessage();
	        return new ObjectRespone(400, "Lỗi: " + firstError, null);
		}
		if(diaChiRepository.getIdDiaChiByShopIdAccount(dcs.getId(),userFilter.getAccount().getId())!=-1) {
			dcs.setShop(null);
			dcs.setAccount(userFilter.getAccount());
			dcs.setIsShop(0);
			diaChiRepository.save(dcs);
			return new ObjectRespone(200,"Cập nhật địa chỉ shop thành công", null);
		}
		return new ObjectRespone(400,"Địa chỉ cập nhật không hợp lệ", null);
	}
	
	@Transactional
	@PostMapping("/user/auth/addaddress")
	public Object postMethodNames(@RequestBody @Valid DiaChi dcs, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        String firstError = bindingResult.getAllErrors().get(0).getDefaultMessage();
	        return new ObjectRespone(400, "Lỗi: " + firstError, null);
		}
		dcs.setShop(null);
		dcs.setAccount(userFilter.getAccount());
		dcs.setIsShop(0);
		diaChiRepository.save(dcs);
		return new ObjectRespone(200,"Cập nhật địa chỉ shop thành công", null);
	}
	
	@Transactional
	@PostMapping("/user/auth/address/updatedefault")
	public Object postMethodName(@RequestParam Integer id) {
		diaChiRepository.updateDefaultAll(id,userFilter.getAccount().getId());
		if(diaChiRepository.updateDefault(id,userFilter.getAccount().getId())!=1) {
			throw new CustomeException("Dữ liệu cung cấp không hợp lệ...");
		}
		return new ObjectRespone(200,"Cập nhật thành công địa chỉ",null);
	}
	
	
	@PostMapping("/user/auth/changepass")
	public ObjectRespone postMethodName(@RequestParam("password")String password,@RequestParam("newpass")String newPass) {
		if(newPass.length()>=5) {
			if(password.equals(newPass)) {
				accountRePository.updateMatKhau(newPass,userFilter.getAccount().getId());
				return new ObjectRespone(200,"Cập nhật mật khẩu thành công",null);
			}else {
				return new ObjectRespone(400,"Mật khẩu xác nhận không khớp",null);
			}
		}else {
			return new ObjectRespone(400,"Mật khẩu ít nhất 5 ký tự",null);
		}
		
		
	}
	
	
	
	@GetMapping("/account/getAll")
	public ResponseEntity<ObjectRespone> getAllAccount() {
		List<Account> accounts = new ArrayList<Account>();
		try {
			accounts = accountService.getAllAccount();
			return ResponseEntity.ok(new ObjectRespone(200, "success", accounts));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectRespone(500, "Error", null));
		}

	}
	
	@PostMapping("/user/auth/updateprofile")
	public Object postMethodName(@RequestBody @Valid Account a,BindingResult bd) {
		if(bd.hasErrors()) {
			FieldError firstError = bd.getFieldErrors().get(0);
	        String errorMessage = firstError.getDefaultMessage();
	        return new ObjectRespone(400, errorMessage, null);
		}
		Integer id=accountRePository.getUserIdByTenTaiKhoan(a.getTenTaiKhoan());
		if(id==null||id==userFilter.getAccount().getId()) {
			Date inputDate = new Date();
	        LocalDate dateToCheck = a.getSinhNhat().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        LocalDate tenYearsAgo = LocalDate.now().minusYears(10);
			if(!dateToCheck.isBefore(tenYearsAgo)) {
				return new ObjectRespone(400, "Tuổi người sử dụng tài khoản phải lón hơn 10",null);
			}
			accountRePository.updateProfile(a.getTenTaiKhoan(), a.getHoVaTen(),a.getGioiTinh(), a.getSinhNhat(), a.getHinhAnh(),userFilter.getAccount().getId());
			return new ObjectRespone(200, "success", null);

		}
		return new ObjectRespone(400, "Tên đăng nhập này đã được sử dụng",null);
	}
	
	
	
	@GetMapping("/user/auth/getprofile")
	public ObjectRespone getProfile() {
		System.out.println(userFilter.getAccount().getId()+" Hello world");
		Account a=accountRePository.findAccountByPhone(userFilter.getAccount().getId());
		a.setMatKhau(null);
		a.setVaiTro(null);
		return new ObjectRespone(200,"success", a);
	}
	@PostMapping("/account/login")
	public ObjectRespone login(@RequestParam("userName") String userName,
			@RequestParam("password") String password) {
		try {
			Account account = accountService.getAccountBySoDienThoai(userName);
			if (account == null || !serPass.deSerialPassword(account.getMatKhau()).trim().equals(password.trim())) {
				return new ObjectRespone(400, "Thông tin đăng nhập chưa chính xác", null);
			}else {
			}
			if (!account.getTrangThai().equals("HoatDong")) {
				return new ObjectRespone(400, "Tài khoản ngưng hoạt động", null);
			}
			account.setMatKhau(null);
			Map<String,Object>maps=new HashMap<String, Object>();
			maps.put("token", t.generateTokenAccount(account.getId()+""));
			maps.put("userInfo",account);
			maps.put("role", account.getVaiTro());
			return new ObjectRespone(200, "Đăng nhập thành công", maps);

		} catch (Exception e) {
			e.printStackTrace();
			return new ObjectRespone(500, "Lỗi hệ thống", null);
		}
	}
	
//	
//	@PostMapping("/account/login")
//	public ObjectRespone login(@RequestParam("userName") String userName,
//			@RequestParam("password") String password) {
//		try {
//			Account account = accountService.getAccountBySoDienThoai(userName);
//			if (account == null || !accountService.validationPassword(account.getMatKhau(), password)) {
//				return new ObjectRespone(400, "Thông tin đăng nhập chưa chính xác", null);
//			}
//			if (accountService.validationStatus(account.getTrangThai()) != 0) {
//				return new ObjectRespone(400, "Tài khoản ngưng hoạt động", null);
//			}
//			account.setMatKhau(null);
//			AccountDTO accountDTO = new AccountDTO(account);
//			Map<String,Object>maps=new HashMap<String, Object>();
//			maps.put("token", t.generateTokenAccount(account.getId()+""));
//			maps.put("userInfo",accountDTO);
//			
//			return new ObjectRespone(200, "Đăng nhập thành công", maps);
//
//		} catch (Exception e) {
//			return new ObjectRespone(500, "Lỗi hệ thống", null);
//		}
//	}
	
	@PostMapping("/user/checkuser")
	public ObjectRespone checkValidUser(@RequestParam("token")String token) {
		System.out.println(token);
		if(t.validateToken(token)) {
			Integer a=Integer.parseInt(t.getOrderInfoFromToken(token));
			Account account = accountService.getAccountById(a);
			if(account!=null&&account.getTrangThai().equals("HoatDong")) {
				account.setMatKhau(null);
				return new ObjectRespone(200,"",account);
			}
			System.out.println("Tài khoản không còn hợp lệ");
			return new ObjectRespone(400,"",null);
		}
		System.out.println("Token không hợp lệ");
		return new ObjectRespone(400,"",null);
	}
	
	@GetMapping("/checkuser")
	public ObjectRespone checkValidUsear(@RequestParam("token")String token) {
		if(t.validateToken(token)) {
			Integer a=Integer.parseInt(t.getOrderInfoFromToken(token));
			Account account = accountService.getAccountById(a);
			if(account!=null&&account.getTrangThai().equals("HoatDong")) {
				account.setMatKhau(null);
				return new ObjectRespone(200,"",account);
			}
			return new ObjectRespone(400,"",null);
		}
		return new ObjectRespone(400,"",null);
	}
	

//	@PostMapping("account/register")
//	public ResponseEntity<ObjectRespone> register(@RequestParam("userName") String userName,
//			@RequestParam("passWord") String passWord, @RequestParam("confirmPassWord") String confirmPassWord,
//			@RequestParam("phone") String phone) {
//
//		String result = accountService.validationRegister(userName, passWord, confirmPassWord, phone);
//		if (!result.equals("pass")) {
//			return ResponseEntity.status(HttpStatus.OK).body(new ObjectRespone(400, result, null));
//		}
//
//		if (accountService.sendOTPSMS(userName, phone)) {
//
//			String token = jwtToken.generateTokenOTP(userName, phone, passWord);
////			System.out.println(token);
//
//			return ResponseEntity.status(HttpStatus.OK).body(new ObjectRespone(200, "Gửi OTP thành công", token));
//		}
//
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(new ObjectRespone(500, "Gửi OTP thất bại", null));
//	}
	
	@PostMapping("account/sendotp")
	public ObjectRespone register(
			@RequestParam("phone") String phone) {
			if(accountRePository.getSoDienThoai(phone).or(-1)==-1) {
				String otp="258277 "+phone;
				String token = t.generateToken(otp);
				return new ObjectRespone(200, "Gửi OTP thành công", token);
			}
			return new ObjectRespone(400, "Số điện thoại đã có tài khoản đăng ký...", null);
		
	}
	
	@PostMapping("account/register")
	@Transactional
	public ObjectRespone registerAccount(@RequestParam(name="soDienThoai",defaultValue = "") String phone,
										@RequestParam(name="password",defaultValue = "") String password,
										@RequestParam(name="tenTaiKhoan",defaultValue = "") String tenTaiKhoan,
										@RequestParam(name="token",defaultValue = "") String token,
										@RequestParam(name="validToken",defaultValue = "") String validToken) {
		if(t.validateToken(token.trim())) {
			if(!serPass.validSoDienThoai(phone)) {
				return new ObjectRespone(400,"Số điện thoại không hợp lệ",null);			}
			if(serPass.validPassword(password.trim())==false) {
				return new ObjectRespone(400,"Mật khẩu không đúng định dạng",null);		
			}
			if(tenTaiKhoan.trim().length()<5) {
				return new ObjectRespone(400,"Tên tài khoản ít nhất 5 ký tự",null);		
			}
			if(accountRePository.getUserIdByTenTaiKhoan(tenTaiKhoan)!=null) {
				return new ObjectRespone(400,"Tên tài khoản đã được đăng ký",null);		
			}
			String aa=t.getOrderInfoFromToken(token);
			System.out.println(validToken);
			System.out.println(aa.substring(0, aa.indexOf(" ")).equals(validToken.trim()));
			if (aa.substring(0, aa.indexOf(" ")).equals(validToken) && aa.substring(aa.lastIndexOf(" ")).trim().equals(phone)) {
				if(accountRePository.getSoDienThoai(phone).or(-1)==-1) {
					accountRePository.addAccount(serPass.serialPassword(password.trim()), phone,tenTaiKhoan);
					return new ObjectRespone(200,"Đăng ký tài khoản thành công",null);
				}
				return new ObjectRespone(400,"Số điện thoại đã đăng ký tài khoản",null);
			}else {
				return new ObjectRespone(400,"Mã xác thực không hợp lệ, vui lòng thử lại",null);
			}
		}else{
			return new ObjectRespone(400,"Mã xác thực đã hết hạn sử dụng",null);
		}
		 
		
	}

	@PostMapping("account/register/OTP")
	public ResponseEntity<ObjectRespone> registerOTP(@RequestParam("otp") String otp,
			@RequestHeader("Authorization") String token) {

		String userName = jwtToken.getUsername(token.trim());
		String phone = jwtToken.getPhone(token);
		String passWord = jwtToken.getPassWord(token);
		String result = "";

		if (userName == null || phone == null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ObjectRespone(400, "Token không hợp lệ", null));
		}
		
//		accountService.deleteOTP(userName);
		result = accountService.verifyOtp(userName, phone, otp);

		if (!result.equals("pass")) {
			return ResponseEntity.status(HttpStatus.OK).body(new ObjectRespone(400, result, null));
		}

		if (!accountService.register(userName, passWord, phone)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ObjectRespone(500, "Tạo tài khoản thất bại vui lòng thử lại", null));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ObjectRespone(200, "Tạo tài khoản thành công", null));

	}

	@PostMapping("account/register/reSendOTP")
	public ResponseEntity<ObjectRespone> reSendOTP(@RequestParam String token) {
		

		String userName = jwtToken.getUsername(token.trim());
		String phone = jwtToken.getPhone(token);
		
		accountService.deleteOTP(userName);
		
		if (accountService.sendOTPSMS(userName, phone)) {

			return ResponseEntity.status(HttpStatus.OK).body(new ObjectRespone(200, "Gửi OTP thành công", token));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ObjectRespone(500, "Gửi OTP thất bại", null));
	}
	
	

}
