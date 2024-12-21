package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.API.model.VoucherShopNguoiDung;
import com.google.common.base.Optional;

public interface VoucherCuaHangguoiDungRepository extends JpaRepository<VoucherShopNguoiDung,Integer> {
	@Query("select p.id from VoucherShopNguoiDung p where p.account.id=:accountId and p.voucher.id=:id")
	Optional<Integer> getIdByVoucherIdAndAccountId(@Param("accountId")Integer aai,@Param("id")Integer id );
}
