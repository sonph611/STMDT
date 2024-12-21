package com.API.configuation;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.API.model.Account;
import com.API.service.saler.ShopFilter;
import com.API.service.saler.UserFilter;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class BeanConfig {
	@Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)  // Sử dụng proxy cho phạm vi request
    public ShopFilter account() {
        return new com.API.service.saler.ShopFilter();
    }
	
	@Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)  // Sử dụng proxy cho phạm vi request
    public UserFilter userfilter() {
		UserFilter u=new UserFilter(new Account());
        return u;
    }
	

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "doa9sdr6z",
            "api_key", "297173819974639",
            "api_secret", "bd6eyA-p0hHsqiw-5sWl5DNtXOk"));
    }
    
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1); 
        executor.setMaxPoolSize(2); 
        executor.setQueueCapacity(100); 
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}
