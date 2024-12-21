package com.API.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "otp")
public class OTP {
	@Id
	private int id;
	private String userName;
	private String phone;
	private String otp;
	private LocalDateTime createdTime;
	private LocalDateTime expirationTime;
	private Boolean isUsed;

	public OTP(int id, String userName, String phone, String otp, LocalDateTime createdTime,
			LocalDateTime expirationTime, Boolean isUsed) {

		this.id = id;
		this.userName = userName;
		this.phone = phone;
		this.otp = otp;
		this.createdTime = createdTime;
		this.expirationTime = expirationTime;
		this.isUsed = isUsed;
	}

	public OTP() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

}
