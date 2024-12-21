package com.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.API.DTO.cart.VoucherSanAply;
import com.API.model.VoucherSan;
import com.API.model.VoucherSanNguoiDung;

public interface VoucherSanRepository extends JpaRepository<VoucherSan, Integer> {
//	(int id,int loaiVoucher,float giaTriGiam,float giamToiDa,int hinhThucGiam)
	@Query("SELECT new com.API.model.VoucherSan(vs.id,vs.loaiVoucher,vs.giaTriGiam,vs.gioiHanGiam,vs.hinhThucApDung,vs.donToiThieu) FROM VoucherSan vs\r\n"
			+ "JOIN VoucherSanNguoiDung vd ON vd.voucherSan.id=vs.id\r\n"
			+ "WHERE vs.id=:voucherId AND vd.account.id=:accountId\r\n"
			+ "AND vs.soLuocDung > vd.soLuocDaDung and vs.soLuocDungMoiNguoi>vd.soLuocDaDung\r\n"
			+ "AND NOW()  BETWEEN vs.ngayBatDau AND vs.ngayKetThuc")
	Optional<VoucherSan> getVoucherSanByAccountIdAndVoucherId(@Param("accountId")Integer accountId,@Param("voucherId")Integer voucherId);

	// order 
	// update lại số lược dùng cyar voucher
//	@Query(value = "CALL UpdateSoLuotDung(:voucherSanId, :accountId)", nativeQuery = true)
//    void updateSoLuotDung(@Param("voucherSanId") Integer voucherSanId, @Param("accountId") Integer accountId);
	
	@Procedure(procedureName = "UpdateSoLuotDung")
    void updateSoLuotDaDung(
        @Param("voucherSanId") Integer voucherSanId, 
        @Param("accountId") Integer accountId
    );
	
	@Query(value = "SELECT new com.API.DTO.cart.VoucherSanAply(vs,vn.account.id) FROM VoucherSan vs\r\n"
			+ "LEFT join VoucherSanNguoiDung vn ON vn.voucherSan.id=vs.id AND vn.account.id=:accountId \r\n"
			+ "WHERE  vs.soLuocDung>vs.soLuocDaDung  AND NOW() BETWEEN vs.ngayBatDau AND vs.ngayKetThuc"
			+ " and (vn.account.id is  null or vs.soLuocDungMoiNguoi>vn.soLuocDaDung)")
    List<VoucherSanAply> getVoucherSanApply(@Param("accountId") Integer accountId);
	
	@Query("SELECT p.id FROM VoucherSan p\r\n"
			+ "			left JOIN VoucherSanNguoiDung v ON v.voucherSan.id=p.id and v.account.id=:accountId \r\n"
			+ "			WHERE p.id=:voucherId AND v.account.id IS null AND NOW() BETWEEN p.ngayBatDau AND p.ngayKetThuc AND\r\n"
			+ "			 p.soLuocDung> p.soLuocDaDung")
	Optional<Integer> getVoucher(@Param("voucherId")Integer id,@Param("accountId")Integer accun);
	
	@Query("select new com.API.model.VoucherSanNguoiDung(p.id,p.soLuocDaDung,p.voucherSan) "
			+ "from VoucherSanNguoiDung p where p.account.id=:accountId and p.voucherSan.maVoucher like :key")
	Page<VoucherSanNguoiDung> getVoucherHistory(@Param("accountId")Integer accountId,Pageable p,@Param("key")String key);
	
	@Query("select vs from VoucherSan vs where vs.id not in (select v.voucherSan.id from VoucherSanNguoiDung v where v.account.id=:accountId) and "
			+ "  vs.maVoucher like :key and now() <= vs.ngayKetThuc and vs.soLuocDung>vs.soLuocDaDung")
	Page<VoucherSan> getVoucherSanNotInVoucherNguoiDung(@Param("accountId")Integer accountId,Pageable p,@Param("key")String key);
	
    @Query("select p.id from VoucherSan p where p.id =:id")
    Optional<Integer> getVoucherIdById(@Param("id")Integer id );
    
    @Query("select p.id from VoucherSanNguoiDung p where p.account.id=:accountId and p.voucherSan.id=:id")
	Optional<Integer> getIdByVoucherIdAndAccountIsssd(@Param("accountId")Integer aai,@Param("id")Integer id );
    
    Page<VoucherSan> findAll(Pageable pageable);
    
    VoucherSan findByTenVoucher(String tenVoucher);
    
//    Optional<VoucherSan> findById(Integer id);
    
    
}
