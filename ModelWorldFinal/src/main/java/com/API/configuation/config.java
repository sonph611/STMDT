package com.API.configuation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.API.filter.AdminFilter;
import com.API.filter.SaleFilter;
import com.API.filter.UserFilter;
//import com.API.service.AuthFilter;

@Configuration
public class config implements WebMvcConfigurer {

    @Autowired
    private SaleFilter saleInterceptor;

    @Autowired
    private UserFilter userFilter;
    
    @Autowired
    private AdminFilter adminFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//    	registry.addInterceptor(adminFilter).addPathPatterns("/admin/**");
        registry.addInterceptor(saleInterceptor).addPathPatterns("/sale/**");
        registry.addInterceptor(userFilter).addPathPatterns("/user/auth/**");
    }

//    @Bean
//    public FilterRegistrationBean<AuthFilter> loggingFilter() {
//        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new AuthFilter());
//        registrationBean.addUrlPatterns("/user/auth/*");
//        return registrationBean;
//    }
}
