package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.ProductImage;
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>{
	ProductImage findByIdAndProductId(int id, int productId);
	
    @Query("SELECT p.product.shop.shopId FROM ProductImage p WHERE p.id = :id AND p.product.id = :productId")
    Integer findShopIdByIdAndProductId(@Param("id") int id, @Param("productId") int productId);
}	
