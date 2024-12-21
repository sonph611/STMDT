package com.API.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.API.model.OTP;

public interface OTPRepository extends JpaRepository<OTP, Integer> {
	@Query(value = "SELECT * FROM otp WHERE userName LIKE :userName AND phone LIKE :phone", nativeQuery = true)
	public OTP findByUserNameAndPhone(@Param
			("userName") String userName, @Param("phone") String phone);
	
	public void deleteByUserName(String userName);
	
	public List<OTP>  findByUserName(String userName);
	
	void deleteByExpirationTimeBefore(LocalDateTime currentTime);
}
