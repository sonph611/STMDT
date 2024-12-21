//package com.API.configuation;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.API.model.Account;
//import com.API.repository.AccountRepository;
//import com.API.service.TokenService;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//
//@Component
//public class AuthFilter implements Filter {
//    
//	TokenService t=new TokenService();
//	@Autowired
//	AccountRepository accountRepo;
//	@Autowired
//	com.API.service.saler.UserFilter userFilter;
//    @Override
//    public void destroy() {
//        // Dọn dẹp nếu cần
//    }
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String token = httpRequest.getHeader("token");
//        System.out.println(token);
//        if (token == null || !t.validateToken(token)) {
//            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
//            return; 
//        }
//        Integer userId = Integer.parseInt(t.getOrderInfoFromToken(token));
//        Account account = accountRepo.findAccountById(userId);
//        if (account != null && account.getTrangThai().equals("HoatDong")) {
//        	userFilter.setAccount(account);
//            chain.doFilter(request, response);
//            return;
//        }
//        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		
//	}
//}