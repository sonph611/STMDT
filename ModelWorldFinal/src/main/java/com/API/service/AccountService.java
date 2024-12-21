package com.API.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.API.model.Account;
public interface AccountService {
	public List<Account> getAllAccount();
	public Account getAccountByUserName(String userName);
	public Account getAccountById(Integer id);
	public Account getAccountBySoDienThoai(String userName);
	public boolean validationPassword(String passwordInput, String passWord);
	public int validationStatus(String statusInput);
	public String validationRegister(String userName, String passWord, String confirmPassword, String phone);
	public boolean register(String userName, String passWord, String phone);
	public boolean sendOTPSMS(String userName, String phone);
	public String randomOTP();
	public String verifyOtp (String userName, String phone, String otpValue);
	public boolean deleteOTP(String userName);
	public void cleanupExpiredOtps();
	public String createOTP();
}
