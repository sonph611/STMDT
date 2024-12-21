package com.API.service;

import java.util.Base64;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class SerPassword {
	String re="^(?=.*[A-Z])(?=(.*\\d){2})(?=(.*[a-zA-Z]){3})[a-zA-Z0-9]+$";
	static String reSdt="^(?:\\+84|0)(?:2|3|5|7|8|9)\\d{8}$";
	
	public boolean validPassword(String password) {
		return Pattern.matches(re, password);
	}
	public static void main(String[] args) {
		System.out.println(new SerPassword().validPassword("12GhsdS"));
	}
	public boolean validSoDienThoai(String password) {
		return Pattern.matches(reSdt, password);
	}
	
	public String serialPassword(String password) {
		return  Base64.getEncoder().encodeToString(password.getBytes());
	}
	
	public String deSerialPassword(String encoded) {
		return  new String(Base64.getDecoder().decode(encoded));
	}
}
