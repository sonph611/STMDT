package com.API.service.saler;

import java.beans.ConstructorProperties;

import org.springframework.stereotype.Component;
//@Component
public class ShopFilter {
	private Integer accountId;
    private Integer shopId;
    @ConstructorProperties({"accountId", "shopId"})
    public ShopFilter(Integer accountId, Integer shopId) {
        this.accountId = accountId;
        this.shopId = shopId;
    }
    
    public ShopFilter() {
    }

    // Getters and Setters
    public Integer getAccountId() {
        return accountId;
    }
    
    public void setInfo(Integer accountId,Integer shopId) {
        this.accountId = accountId;
        this.shopId=shopId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
