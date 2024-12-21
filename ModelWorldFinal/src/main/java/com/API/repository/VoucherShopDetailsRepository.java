package com.API.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.API.model.VoucherShopDetail;

public interface VoucherShopDetailsRepository extends JpaRepository<VoucherShopDetail,Integer>{
	@Query(value = "SELECT p.sanpham_id FROM voucher_sanpham p  WHERE p.voucher_id = :shopId AND p.sanpham_id IN :productIds", nativeQuery = true)
    List<Integer> findProductIdsByShopIdAndProductIds(@Param("shopId") Integer shopId, @Param("productIds") Set<Integer> productIds);
	// count
	@Query(value = "SELECT count(p.sanpham_id) FROM voucher_sanpham p  WHERE p.voucher_id = :shopId AND p.sanpham_id IN :productIds", nativeQuery = true)
    int CountfindProductIdsByShopIdAndProductIds(@Param("shopId") Integer shopId, @Param("productIds") Set<Integer> productIds);
	
	@Query(value = "SELECT p.sanpham_id FROM voucher_sanpham p  WHERE p.voucher_Id = :shopId ", nativeQuery = true)
    List<Integer> SetfindProductIdsByShopIdAndProductIds(@Param("shopId") Integer shopId);
	
	@Query(value = "SELECT p.id FROM voucher_sanpham p  WHERE  p.sanpham_id IN :productIds", nativeQuery = true)
    List<Integer> HehefindProductIdsByShopIdAndProductIds(@Param("productIds") Set<Integer> productIds);
	
	@Modifying
	@Query(value = "DELETE FROM voucher_sanpham v WHERE v.sanpham_id IN :productIds", nativeQuery = true)
	void deleteByProductIds(@Param("productIds") List<Integer> productIds);
	
	@Modifying
	@Query(value = "DELETE FROM voucher_sanpham v WHERE v.sanpham_id IN :productIds", nativeQuery = true)
	void deleteByProductIds(@Param("productIds") Set<Integer> productIds);
}