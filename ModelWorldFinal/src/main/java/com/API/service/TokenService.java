package com.API.service;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class TokenService {

	    
	private static final String secretKey ="8B2zYp5WpfJ2oLq1mSkhY9c4PpzN0mR8B2zYp5WpfJ2oLq1mSkhY9c4PpzN0mR8X8X"; 

    private static final long EXPIRATION_TIME = 30000000; 

    // Hàm tạo token từ chuỗi truyền vào
    public String generateToken(String orderInfo) {
        return Jwts.builder()
                .setSubject(orderInfo) 
                .setIssuedAt(new Date(System.currentTimeMillis())) 
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) 
                .signWith(SignatureAlgorithm.HS256, secretKey) 
                .compact(); 
    }
    
    public String generateTokenAccount(String orderInfo) {
        return Jwts.builder()
                .setSubject(orderInfo)
                .setIssuedAt(new Date(System.currentTimeMillis())) 
                .setExpiration(new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000))) 
                .signWith(SignatureAlgorithm.HS256, secretKey) 
                .compact();
    }

    // Hàm kiểm tra tính hợp lệ của token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // Giải mã token
            return true; 
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getOrderInfoFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()  // sử dụng parserBuilder nếu phiên bản mới của jjwt
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // trả về subject (thông thường là thông tin người dùng)
        } catch (ExpiredJwtException e) {
            // Nếu token hết hạn
            System.out.println("Token expired");
        } catch (MalformedJwtException e) {
            // Nếu token không hợp lệ
        	e.printStackTrace();
        } catch (Exception e) {
            // Bắt lỗi chung
            System.out.println("Error parsing token: " + e.getMessage());
        }
        return null; // Trả về null nếu có lỗi
    }

    // Hàm lấy thông tin từ token
//    public String getOrderInfoFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody(); 
//        return claims.getSubject(); 
//    }

}
