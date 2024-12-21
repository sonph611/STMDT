package com.API.service;

	import java.io.IOException;
	import java.security.SecureRandom;
	import java.sql.Timestamp;
	import java.time.LocalDateTime;
	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Service;
	import org.springframework.web.context.annotation.SessionScope;

	import com.API.model.Account;
import com.API.model.OTP;
//	import com.API.model.OTP;
	import com.API.repository.AccountRepository;
//	import com.API.repository.OTPRepository;
import com.API.repository.OTPRepository;

	@SessionScope
	@Service
	public class AccountServicelmpl implements AccountService {
		
		@Autowired
		AccountRepository accountRepository;
		@Autowired
		OTPRepository otpRepository;
		
		@Override
		public List<Account> getAllAccount() {
			return accountRepository.findAll();
		}

		@Override
		public Account getAccountByUserName(String userName) {
			Account account = accountRepository.findAccountByUserName(userName);
			if (account != null) {
				return account;
			}
			return null;
		}

		@Override
		public Account getAccountBySoDienThoai(String userName) {
			Account account = accountRepository.getSoDienThoais(userName).orNull();
			if (account != null) {
				return account;
			}
			return null;
		}
		
		@Override
		public boolean validationPassword(String passwordInput, String passWord) {
			if (passWord.equals(passwordInput)) {
				return true;
			}
			return false;
		}

		@Override
		public int validationStatus(String statusInput) {
			if (statusInput.equals("HoatDong")) {
				return 0;
			}
			return 1;
		}

		@Override
		public String validationRegister(String userName, String passWord, String confirmPassword, String phone) {
		    if (userName.isEmpty() || passWord.isEmpty() || phone.isEmpty()) 
		        return "Vui lòng điền đầy đủ thông tin";
		    
		    if (userName.length() < 7 || passWord.length() < 7) 
		        return "Tên tài khoản và mật khẩu có ít nhất 7 kí tự";
		    
		    System.out.println(phone.length());
		    if (phone.length() < 10 || phone.length() > 15) 
		        return "Số điện thoại có độ dài từ 10 đến 15 kí tự";
		    
		    if (!passWord.equals(confirmPassword)) 
		        return "Mật khẩu xác nhận chưa đúng";
		    
		    if (accountRepository.findAccountByPhone(phone) != null) 
		    	return "Số điện thoại đã được sử dụng";
		    
		    if (accountRepository.findAccountByUserName(userName) != null) 
		        return "Tài khoản này đã tồn tại";
		    
		    return "pass";
		}

		@Override
		public boolean register(String userName, String passWord, String phone) {
			
			Account account = new Account();
			account.setTenTaiKhoan(userName);
			account.setMatKhau(passWord);
			account.setSoDienThoai(phone);
			account.setVaiTro("NguoiDung");
			account.setTrangThai("HoatDong");
			
			Account savedAccount = accountRepository.saveAndFlush(account);
		    if (savedAccount != null) {
		        return true;
		    } 
			return false;
		}

		@Override
		public boolean sendOTPSMS(String userName, String phone) {
			SendOTP api  = new SendOTP("xhzIs__0Vu5h03-ZkWJJoymyc8hZM_eT");
			String OTP = randomOTP();
			String content = "Mã OTP của bạn là: " + OTP + ", hiệu lực của mã này là 2 phút, vui lòng không chia sẻ cho bất kì ai";
	OTP otp = new OTP();
			otp.setUserName(userName);
			otp.setPhone(phone);
			otp.setOtp(OTP);
			otp.setCreatedTime(LocalDateTime.now());
			otp.setExpirationTime(LocalDateTime.now().plusMinutes(2));
			otp.setIsUsed(false);
			try {
				otpRepository.save(otp);
				api.sendSMS(phone, content, 2, "3e131306b05f893c");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public String createOTP() {
			SendOTP api  = new SendOTP("xhzIs__0Vu5h03-ZkWJJoymyc8hZM_eT");
			String OTP = randomOTP();
		    return OTP;
		}
		
		
		@Override
		public String randomOTP() {
			 SecureRandom random = new SecureRandom();
		        int otp = random.nextInt(900000) + 100000; // Random từ 100000 đến 999999
		        return String.valueOf(otp);
		}

		@Override
		public String verifyOtp(String userName, String phone, String otpValue) {
			OTP otp = otpRepository.findByUserNameAndPhone(userName, phone);
			if (otp == null) {
				return "Không tìm thấy OTP";
			}
			
			if (!otp.getOtp().equals(otpValue)) {
				return "Mã OTP chưa chính xác";
			}
			
			if (otp.getIsUsed()) {
				return "Mã OTP đã hết hiệu lực";
			}
			
			otpRepository.delete(otp);
			
			return "pass";
		}

		@Override
		public boolean deleteOTP(String userName) {
			List<OTP> otps = otpRepository.findByUserName(userName);
			if (otps != null) {
				otpRepository.deleteAll(otps);
				return true;
			}
			return false;
		}

		@Override
		public void cleanupExpiredOtps() {
			
		}

		@Override
		public Account getAccountById(Integer id) {
			return accountRepository.findAccountById(id);
		}


		





	
}
