package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.API.model.ThanhToan;

import jakarta.transaction.Transactional;

public interface VoucherShopRepository extends JpaRepository<ThanhToan, Integer> {
	@Modifying
    @Transactional
    @Query(value = "CALL UpdateCountUseVoucher(:voucherShopId, :accountId)", nativeQuery = true)
    void updateCountUseVoucher(int voucherShopId, int accountId);
	
	
//	@Query(value = "\r\n"
//			+ "SELECT vs.*,GROUP_CONCAT('',vd.productId) FROM vouchershop vs \r\n"
//			+ "JOIN vouchershopdetails vd ON vd.voucherId=vs.id AND vs.shopId=shopId\r\n"
//			+ "LEFT JOIN vouchernguoidung vn ON vn.voucherId=vs.id AND vn.accountId=1 AND vn.soLuocDung< vs.soLuocDaDung\r\n"
//			+ "WHERE vd.productId IN (680) AND NOW() not  BETWEEN vs.ngayBatDau AND vs.ngayKetThuc\r\n"
//			+ "GROUP BY vs.id",nativeQuery = true)
//	public Object[] getAll();
}
