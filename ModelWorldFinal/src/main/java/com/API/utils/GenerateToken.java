package com.API.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class GenerateToken {
    private static final String secretKey ="8B2zYp5WpfJ2oLq1mSkhY9c4PpzN0mR8B2zYp5WpfJ2oLq1mSkhY9c4PpzN0mR8X8X"; // Đúng

    private static final long EXPIRATION_TIME = 7200000; 

    // Hàm tạo token từ chuỗi truyền vào
    public String generateToken(String orderInfo) {
        return Jwts.builder()
                .setSubject(orderInfo) 
                .setIssuedAt(new Date(System.currentTimeMillis())) 
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) 
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

    // Hàm lấy thông tin từ token
    public String getOrderInfoFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody(); 
        return claims.getSubject(); 
    }

    public static void main(String[] args) {
        GenerateToken g = new GenerateToken();
        String orderInfo = "36363-3838-yeyyeye"; 

        String token = g.generateToken(orderInfo);
        System.out.println("Generated Token: " + token);

        boolean isValid = g.validateToken(token);
        System.out.println("Is the token valid? " + isValid);

        String extractedOrderInfo = g.getOrderInfoFromToken(token);
        System.out.println("Extracted Order Info: " + extractedOrderInfo);
    }
}
