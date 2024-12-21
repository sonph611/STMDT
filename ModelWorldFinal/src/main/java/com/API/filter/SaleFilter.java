package com.API.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.API.model.Account;
import com.API.model.Shop;
import com.API.repository.AccountRepository;
import com.API.repository.ShopRepository;
import com.API.service.TokenService;
import com.API.service.saler.ShopFilter;
import com.API.service.saler.UserFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class SaleFilter implements HandlerInterceptor {

	@Autowired
	ShopFilter shopFilter;
	@Autowired
	ShopRepository s;
//	@Autowired UserFilter u;
	@Autowired 
	TokenService t;
	@Autowired
	AccountRepository accountRepo;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String a=request.getHeader("token");
    	Account account;
    	try {
    		if(a!=null||t.validateToken(a)) {
        		Integer userId=Integer.parseInt(t.getOrderInfoFromToken(a));
        		account=accountRepo.findAccountByIdS(userId);
        		if(account!=null&&account.getTrangThai().equals("HoatDong")) {
//        			System.out.println("Hello world");
        	    	Integer idShop=s.findActiveShopsByAccountId(userId).orElse(0);
        	    	if(idShop>0) {
            	    	shopFilter.setInfo(userId ,idShop);
        	    		return true;
        	    	}else {
        	    		request.setAttribute("type","shop");
        	    		return false;
        	    	}
        		}else {
//        			System.out.println("hello world");
        			request.setAttribute("type","account");
        	    	request.getRequestDispatcher("/error").forward(request, response);
        	    	
        		}
        	}
		} catch (Exception e) {
		}
    	return false;
    }
}
