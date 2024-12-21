package com.API.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.API.model.Account;
import com.API.repository.AccountRepository;
import com.API.service.TokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserFilter implements HandlerInterceptor{
	@Autowired
	com.API.service.saler.UserFilter userFilter;
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	TokenService t;
	
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
    	String a=request.getHeader("token");
    	try {
    		if(a!=null||t.validateToken(a)) {
        		Integer userId=Integer.parseInt(t.getOrderInfoFromToken(a));
        		Account account=accountRepo.findAccountById(userId);
        		if(account!=null&&account.getTrangThai().equals("HoatDong")) {
        	    	userFilter.setAccount(account);
        			return true;
        		}
        	}
		} catch (Exception e) {
		}
    	request.getRequestDispatcher("/error").forward(request, response);
    	return false;
    }
}
