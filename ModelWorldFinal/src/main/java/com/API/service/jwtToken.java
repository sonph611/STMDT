package com.API.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.API.model.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class jwtToken {

    private String secretKey = "tanbvpc05190dgwcbxmcbxmbcjgcdjdjdhdu2382jscsjcvsvcv"; 

    public String generateTokenOTP(String userName, String phone, String passWord) {
        Map<String, Object> claims = new HashMap();
        claims.put("username", userName);
        claims.put("phone", phone);
        claims.put("password", passWord);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getClaims(String token) {
    	if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
    
    public  String createTokendecodeTokenJSON(String json) {
    	Map<String, Object> claims = new HashMap();
        claims.put("data",json);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    public  String decodeTokenJSON(String token) {
    	return (String) getClaims(token).get("data");
    }

    
    

    public String getUsername(String token) {
        return (String) getClaims(token).get("username");
    }
    
    public String getPhone(String token) {
        return (String) getClaims(token).get("phone");
    }
    
    public String getPassWord(String token) {
        return (String) getClaims(token).get("password");
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

            return !isTokenExpired(claims);
        } catch (SignatureException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
