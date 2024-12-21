package com.API.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.API.DTO.cart.VoucherAplly;
import com.API.DTO.khuyenMai.KhuyenMaiItem;
import com.API.DTO.voucher.VoucherView;
import com.API.model.Shop;
import com.API.model.VoucherShop;
import com.API.model.VoucherShopNguoiDung;

import jakarta.transaction.Transactional;

public interface VoucherRepository extends JpaRepository<VoucherShop, Integer>{
	
//	Integer voucherId, Integer accountId, Integer loaiVoucher, Float giaTriGiam, Double donToiThieu,
//	String voucherName,Integer voucherSoLuocDung, Integer voucherSoLuocDaDung
	@Query("SELECT new com.API.DTO.cart.VoucherAplly(vc.id,vn.account.id,vc.loaiVoucher,vc.giaTriGiam,vc.donToiThieu,"
			+ "vc.tenVoucher,vc.soLuocDung,vc.soLuocDaDung) FROM VoucherShopDetail vp " +
		       "JOIN VoucherShop vc ON vc.id = vp.voucher.id " +
		       "LEFT JOIN VoucherShopNguoiDung vn ON vn.voucher.id = vc.id and vn.account.id=:accountId " +
		       "WHERE vp.product.id = :productId " +
		       "AND now() BETWEEN vc.ngayBatDau AND vc.ngayKetThuc and vc.trangThai=1")
		List<VoucherAplly> getVoucherByProductId(@Param("productId") Integer id, @Param("accountId") Integer accountId);

	
	@Query("SELECT \r\n"
			+ "    SUM(dh.tienTru) ,\r\n"
			+ "    COUNT(vc.id) ,\r\n"
			+ "    (SUM(vc.soLuocDaDung) * 100 / SUM(vc.soLuocDung)) AS percentageUsage,\r\n"
			+ "    COUNT(DISTINCT dh.accountId.id) AS uniqueTaiKhoanCount\r\n"
			+ "FROM \r\n"
			+ "    VoucherShop vc\r\n"
			+ "LEFT JOIN \r\n"
			+ "    Order dh ON dh.voucherShopId.id = vc.id AND dh.voucherShopId.id IS NOT NULL\r\n"
			+ "WHERE \r\n"
			+ "    vc.shop.shopId = :id \r\n"
			+ "    AND vc.ngayBatDau >=:date")
	Object getReportOrder(@Param("date") LocalDateTime date,@Param("id")Integer shopId);
	
	
	 Optional<VoucherShop> findByMaVoucher(String maVoucher);
	 
	 @Query("SELECT s FROM VoucherShop s where s.id=:id and s.shop.shopId=:shopId ")
	 Optional<VoucherShop> findVoucherIdByIdAndShopIdNha(@Param("id")Integer id,@Param("shopId")Integer shopId);
	 
	 @Query(value = "	 SELECT vp.id,p.* FROM voucher_sanpham vp \r\n"
	 		+ "	 JOIN sanpham p ON p.id=vp.sanPham_Id and vp.Voucher_Id=144\r\n"
	 		+ "	 JOIN chitietsanpham pd WHERE pd.sanPhamId=p.id\r\n"
	 		+ " ", nativeQuery = true)
	 Optional<List<Object[]>> findVoucherDetailByVoucherId();
	 
	 @Query(value = "SELECT id FROM VouchersCuaHang WHERE maVoucher = ?1 ", nativeQuery = true)
	 Optional<Integer> findVoucherIdByIdAndShopIdGetInteger(String id);
	 
	 @Query("SELECT new com.API.DTO.khuyenMai.KhuyenMaiItem(p.id,p.tenSanPham,min(pd.giaBan),max(pd.giaBan),sum(pd.soLuong),p.hinhAnh) "
	 		+ " FROM Product p\r\n"
	 		+ "JOIN ProductDetail pd ON p.id=pd.product.id AND p.shop.shopId=:shopId where p.tenSanPham like :key \r\n"
	 		+ "GROUP BY (p.id)")
	org.springframework.data.domain.Page<KhuyenMaiItem> getProductForchoose(
	        @Param("shopId") Integer shopId,Pageable  p,@Param("key")String key);
	 
	@Query("SELECT new com.API.DTO.khuyenMai.KhuyenMaiItem(p.id,p.tenSanPham,min(pd.giaBan),max(pd.giaBan),sum(pd.soLuong),p.hinhAnh) "
		 		+ " FROM VoucherShopDetail vp join"
		 		+ " Product p on p.id=vp.product.id and vp.voucher.id=:id \r\n"
		 		+ "JOIN ProductDetail pd ON p.id=pd.product.id \r\n"
		 		+ "GROUP BY (p.id)")
		List<KhuyenMaiItem> getVoucherDetailById(@Param("id") Integer id);

	 
	
	@Query(value="SELECT p.id FROM VoucherShop p\r\n"
			+ "left JOIN VoucherShopNguoiDung v ON v.voucher.id=p.id and v.account.id=:accountId \r\n"
			+ "WHERE p.id=:voucherId AND v.account.id IS null and"
			+ " p.soLuocDung> p.soLuocDaDung")
	public Optional<Integer> getVoucherForUser(@Param("voucherId")Integer id,@Param("accountId")Integer accun);
	 
//	 @Query("SELECT new com.API.DTO.khuyenMai.KhuyenMaiItem(p.id,p.tenSanPham,1001.1,1001.0,252535) "
//		 		+ " FROM Product p where p.shop.shopId=:shopId")
////		(Integer id, Integer productId, String productName, Object  minPrice,Object maxPrice, Object soLuong
//		List<KhuyenMaiItem> getProductForchoose(@Param("shopId") Integer shopId);
//	 
//	 @Query("SELECT  p.product.id,min(p.giaBan),max(p.giaBan),sum(p.soLuong)"
//		 		+ " FROM ProductDetail p where p.product.id in :l group by (p.product.id) ")
////		(Integer id, Integer productId, String productName, Object  minPrice,Object maxPrice, Object soLuong
//		List<KhuyenMaiItem> getProductDetail(@Param("l")List<Integer>l);
	 
	 
    @Query(value = "SELECT id FROM VouchersCuaHang WHERE id = ?1 AND shopId = ?2", nativeQuery = true)
    Integer findVoucherIdByIdAndShopId(Integer id, int shopId);
    VoucherShop findByIdAndShopShopId(int id, int shopId);

    @Modifying
    @Query("UPDATE VoucherShop v SET v.trangThai = ?1 WHERE v.id = ?2")
    void updateTrangThai(int trangThai, int id);
    @Modifying
    @Query("UPDATE VoucherShop v SET v.moTa = :moTa, v.tenVoucher = :tenVoucher, v.ngayKetThuc = :ngayKetThuc, v.soLuocDung = :soLuocDung WHERE v.id = :id")
    int updateVoucherDetails(@Param("moTa") String moTa, 
                             @Param("tenVoucher") String tenVoucher,
                             @Param("ngayKetThuc") Date ngayKetThuc,
                             @Param("soLuocDung") int soLuocDung, 
                             @Param("id") int id);
//    List<KhuyenMai> findByIdAndShopShopId(int id,int shopId);
//    (String voucherName, String maVoucher, Date ngayBatDau, Date ngayKetThuc, int loaiVoucher,
//			float giaTriGiam, int tongLuocDung, int soLuongSanPhamApDung)
    
    @Query("SELECT new com.API.DTO.voucher.VoucherView(v.id,v.tenVoucher, v.maVoucher,"
    		+ " v.ngayBatDau, v.ngayKetThuc, v.loaiVoucher, v.giaTriGiam, v.soLuocDung,v.soLuocMoiNguoi,v.soLuocDaDung, COUNT(b.id)) " +
    	       "FROM VoucherShop v JOIN VoucherShopDetail b ON v.id = b.voucher.id " +
    	       "WHERE v.shop.id = :shopId " +
    	       "AND (:status =0 OR (CASE " +
    	       "WHEN :status = 1 THEN v.ngayBatDau > now() " + // trang thái 
    	       "WHEN :status = 2 THEN v.ngayBatDau <= now() AND v.ngayKetThuc >= now() " + // đang diễn ra
    	       "WHEN :status = 3 THEN v.ngayKetThuc < now() " + // đã kết thúc
    	       "END))  and (v.tenVoucher like :key or v.maVoucher like :key) "  +
    	       "GROUP BY v.id")
    org.springframework.data.domain.Page<List<VoucherView>> findVouchersByShopIdWithProductCount(
    		Pageable pageable,@Param("shopId")Integer shopId, @Param("status")Integer status,@Param("key")String key);
    
    @Query("SELECT new com.API.DTO.voucher.VoucherView(v.id,v.tenVoucher, v.maVoucher,"
    		+ " v.ngayBatDau, v.ngayKetThuc, v.loaiVoucher, v.giaTriGiam, v.soLuocDung,v.soLuocMoiNguoi,v.soLuocDaDung, COUNT(b.id),v.trangThai) " +
    	       "FROM VoucherShop v left JOIN VoucherShopDetail b ON v.id = b.voucher.id " +
    	       "WHERE v.shop.id = :shopId and v.loaiVoucher not in:ids " +
    	       "AND (:status =0 OR (CASE " +
    	       "WHEN :status = 1 THEN v.ngayBatDau > now() " + // trang thái 
    	       "WHEN :status = 2 THEN v.ngayBatDau <= now() AND v.ngayKetThuc >= now() " + // đang diễn ra
    	       "WHEN :status = 3 THEN v.ngayKetThuc < now() " + // đã kết thúc
    	       "END))  and (v.tenVoucher like :key or v.maVoucher like :key) "  +
    	       "GROUP BY v.id")
    org.springframework.data.domain.Page<List<VoucherView>> findVouchersByShopIdWithProductCount(
    		Pageable pageable,@Param("shopId")Integer shopId, @Param("status")Integer status,@Param("key")String key,List<Integer>ids);

    @Query("SELECT new com.API.DTO.voucher.VoucherView(v.id,v.tenVoucher, v.maVoucher, v.ngayBatDau, v.ngayKetThuc, v.loaiVoucher, v.giaTriGiam, v.soLuocDung, COUNT(b.id)) " +
 	       "FROM VoucherShop v JOIN VoucherShopDetail b ON v.id = b.voucher.id " +
 	       "WHERE v.shop.id = :shopId " +
 	       "AND (:status =0 OR (CASE " +
 	       "WHEN :status = 1 THEN v.ngayBatDau > CURRENT_DATE " +
 	       "WHEN :status = 2 THEN v.ngayBatDau <= CURRENT_DATE AND v.ngayKetThuc >= CURRENT_DATE " +
 	       "WHEN :status = 3 THEN v.ngayKetThuc < CURRENT_DATE " +
 	       "END)) " +
 	       "GROUP BY v.id")
 	List<VoucherView> findVouchersByShopIdWithProductCount(@Param("shopId") int shopId, @Param("status") int status,Pageable page);
    
    @Query("select v.id from VoucherShop v where v.id=:id and v.shop.shopId=:shopId")
    Optional<Integer> getVoucherIdByIdAndShopId(@Param("id") Integer id,@Param("shopId") Integer shopId);
    
    
//    Integer id ,Integer soLuocDungMoiNguoi,Integer soLuocDaDung,String tenVoucher,float giaTriGiam,
//	Integer loaiVoucher,float donToiThieu,float giamToiDa,Shop s

    @Query("select new com.API.model.VoucherShopNguoiDung(vn.id,vn.soLuocDung,"
    		+ "vn.voucher) from VoucherShopNguoiDung vn where vn.account.id=:accountId and vn.voucher.maVoucher like:key")
    Page<VoucherShopNguoiDung> getVoucherHistory(Pageable p,@Param("accountId")Integer accountId,@Param("key")String key);
    
    @Query("select p from VoucherShop p where p.id not in (select v.voucher.id from VoucherShopNguoiDung v "
    		+ " where v.account.id =:accountId) and p.maVoucher like:key and now()<=p.ngayKetThuc and p.soLuocDung>p.soLuocDaDung")
    Page<VoucherShop> getVoucherNotInMyVoucher(Pageable p,@Param("accountId")Integer accountId,@Param("key")String key);
//    new com.API.model.VoucherShop(vn.voucher.id,vn.voucher.soLuocMoiNguoi,vn.voucher.soLuocDaDung,vn.voucher.tenVoucher,"
//    		+ "vn.voucher.giaTriGiam,vn.voucher.loaiVoucher,vn.voucher.donToiThieu,vn.voucher.tienGiamToiDa,"
//    		+ "new com.API.model.Shop(vn.voucher.shop.shopId,vn.voucher.shop.shopName))
    
    @Query("select p.id from VoucherShop p where p.id =:id")
    Optional<Integer> getVoucherIdById(@Param("id")Integer id );
    @Modifying
    @Transactional
    @Query("Update VoucherShop s set s.trangThai=:trangThai where s.id=:id and s.shop.shopId=:shopId")
    Integer updateStateVoucher(@Param("trangThai")Integer trangThai,@Param("id")Integer id,@Param("shopId")Integer shopId);
}
