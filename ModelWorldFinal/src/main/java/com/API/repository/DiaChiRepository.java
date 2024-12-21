package com.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.API.model.DiaChi;
import com.API.model.DiaChiShop;

public interface DiaChiRepository extends JpaRepository<DiaChi, Integer>{
	@Query("select p.id from DiaChi p where p.id=:diaChiId and p.account.id=:accountId ")
	Integer getsDiaChiId(@Param("accountId")int accountId,@Param("diaChiId")Integer diaChiId);
	
	@Query("select p from DiaChi p where  p.account.id=:accountId ")
	List<DiaChi> getsDiaChiId(@Param("accountId")int accountId);
	
	@Query("select p from DiaChi p where p.shop.shopId=:shopId and p.isShop=1")
	DiaChi getByShopId(@Param("shopId")Integer shopId);
	@Query("select p.id from DiaChi p where p.shop.shopId=:shopId and p.isShop=1")
	Integer getIdDiaChiByShopId(@Param("shopId")Integer shopId);
	
	
	@Query("select p.id from DiaChi p where p.id=:id and p.account is not null and p.account.id=:accountId")
	Integer getIdDiaChiByShopIdAccount(@Param("id")Integer shopId,@Param("accountId")Integer accountId);
	
	@Modifying
	@Query("update DiaChi d set d.isDefault=0 where d.id !=:id and d.account is not null and d.account.id=:accountId")
	void updateDefaultAll(@Param("id") Integer id, @Param("accountId") Integer accountId);

	@Modifying
	@Query("update DiaChi d set d.isDefault=1 where d.id=:id and d.account is not null and d.account.id=:accountId")
	Integer updateDefault(@Param("id") Integer id, @Param("accountId") Integer accountId);

}
