package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.DiaChiShop;

@Repository
public interface DiaChiShopRepository extends JpaRepository<DiaChiShop,Integer> {
	
	@Query("select p from DiaChiShop p where p.shop.shopId=:shopId")
	DiaChiShop getByShopId(@Param("shopId")Integer shopId);
	
	@Query("select p.id from DiaChiShop p where p.shop.shopId=:shopId")
	Integer getIdDiaChiByShopId(@Param("shopId")Integer shopId);
}
