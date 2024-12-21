package com.API.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.DTO.khuyenMai.KhuyenMaiItem;
import com.API.DTO.khuyenMai.KhuyenMaiViewSaler;
import com.API.model.ChiTietKhuyenMai;
import com.API.model.KhuyenMai;
import com.API.model.Product;

import jakarta.transaction.Transactional;
@Repository
public interface KhuyenMaiRePository extends JpaRepository<KhuyenMai, Integer>{
	
	
	
	
	// lấy khuyến mãi của san rhamr
	@Query("SELECT km FROM ChiTietKhuyenMai ck \r\n"
			+ "JOIN KhuyenMai km ON ck.khuyenMai.id=km.id\r\n"
			+ "WHERE ck.product.id=:productId AND NOW() BETWEEN km.ngayBatDau AND km.ngayKetThuc")
	KhuyenMai getKhuyenMaiByProductId(@Param("productId")Integer productId);
	
	// cách 1
	 @Query("SELECT km.id FROM KhuyenMai km WHERE  km.shop.id = :shopId and km.id!=:id and " +
	           "(km.ngayBatDau BETWEEN :startDate AND :endDate OR " +
	           "km.ngayKetThuc BETWEEN :startDate AND :endDate)  " +
	           "")
	    Set<Integer> findPromotionsWithinDateRange(
	            @Param("shopId") int shopId,
	            @Param("startDate") java.util.Date startDate,
	            @Param("endDate") Date endDate,
	            @Param("id") int id);
	 // cách 2
	 @Query("SELECT COUNT(ck.id) FROM KhuyenMai km "
		       + "JOIN ChiTietKhuyenMai ck ON ck.khuyenMai.id = km.id "
		       + "WHERE km.shop.id = :shopId AND km.id != :id "
		       + "AND ((:endDate <= km.ngayKetThuc AND :endDate <= km.ngayBatDau) "
		       + "OR (:endDate > km.ngayKetThuc AND :startDate >= km.ngayKetThuc)) "
		       + "AND ck.product.id IN :list")
		Integer findPromotionsWithinDateRangeCustome(
		        @Param("shopId") int shopId,
		        @Param("startDate") java.util.Date startDate,
		        @Param("endDate") java.util.Date endDate,
		        @Param("id") int id,
		        @Param("list") Set<Integer> productIds);
	 
	 // lấy các sản phẩm không nằm trong chương trình khuyến mãi.
	 
//	 @Query("SELECT  ck.product.id  \r\n"
//		 		+ "FROM  ChiTietKhuyenMai ck \r\n"
//		 		+ "LEFT JOIN KhuyenMai km ON km.id = ck.khuyenMai.id AND km.shop.shopId=:shopId\r\n"
//		 		+ "WHERE km.id is null or NOW() not BETWEEN km.ngayBatDau AND km.ngayKetThuc  \r\n"
//		 		+ "")
//			Page<KhuyenMaiItem> getProductNotInPromotion(
//			        @Param("shopId") Integer shopId, Pageable p);
	 
	 @Query("SELECT DISTINCT new com.API.DTO.khuyenMai.KhuyenMaiItem(p.id, p.tenSanPham, p.hinhAnh) \r\n"
				+ "FROM Product p \r\n"
				+ "LEFT JOIN ChiTietKhuyenMai ck ON ck.product.id = p.id  \r\n"
				+ "LEFT JOIN KhuyenMai km ON km.id = ck.khuyenMai.id AND km.shop.shopId = :shopId \r\n"
				+ "WHERE p.shop.id=:shopId and (km.id IS NULL  \r\n"
				+ "OR (:endDate <= km.ngayKetThuc AND :endDate <= km.ngayBatDau \r\n"
				+ "OR :endDate > km.ngayKetThuc AND :startDate >= km.ngayKetThuc) )")
	Page<KhuyenMaiItem> getProductNotInPromotion(
		        @Param("shopId") Integer shopId, Pageable p,@Param(value ="startDate")Date startDate,
				@Param(value ="endDate")Date endDate);
	 
	 
	 @Query("SELECT new com.API.DTO.khuyenMai.KhuyenMaiItem(p.id, p.tenSanPham, p.hinhAnh) \r\n"
				+ "FROM Product p \r\n"
				+ " where p.id in:l and p.shop.id=:shopId")
	List<KhuyenMaiItem> getKhuyenMaiItemInList(
		        @Param("shopId") Integer shopId,@Param("l")List<Integer>l);
	 
//	 @Query("SELECT DISTINCT p.id  \r\n"
//		 		+ "FROM Product p \r\n"
//		 		+ "LEFT JOIN ChiTietKhuyenMai ck ON ck.product.id = p.id \r\n"
//		 		+ "LEFT JOIN KhuyenMai km ON km.id = ck.khuyenMai.id AND km.shop.shopId=:shopId\r\n"
//		 		+ "WHERE ( km.id is null or NOW()  BETWEEN km.ngayBatDau AND km.ngayKetThuc) and p.id in :l \r\n"
//		 		+ "")
//	List<Integer> getValidProductNotPromotion(@Param(value ="l")List<Integer>l,@Param("shopId")Integer id);

	 
	 @Query("SELECT ck.product.id  \r\n"
				+ "FROM ChiTietKhuyenMai ck  \r\n"
				+ "JOIN KhuyenMai km ON km.id = ck.khuyenMai.id AND km.shop.shopId = :shopId \r\n"
				+ "WHERE ck.product.id IN :l \r\n"
				+ "AND ( \r\n"
				+ "     (:endDate <= km.ngayKetThuc AND :endDate >= km.ngayBatDau) \r\n"
				+ "  OR (:endDate >= km.ngayKetThuc AND :startDate <= km.ngayKetThuc) \r\n"
				+ ")")
	List<Integer> getValidProductNotPromotionVesion(@Param(value ="l")List<Integer>l,@Param("shopId")Integer id,
													@Param(value ="startDate")Date startDate,
													@Param(value ="endDate")Date endDate);	
	 
	 // THÊM MỚI 
	 
	 
	 @Query("SELECT ck.product.id  \r\n"
				+ "FROM ChiTietKhuyenMai ck  \r\n"
				+ "JOIN KhuyenMai km ON km.id = ck.khuyenMai.id AND km.shop.shopId = :shopId \r\n"
				+ "WHERE km.id !=:id and ck.product.id IN :l \r\n"
				+ "AND ( \r\n"
				+ "     (:endDate <= km.ngayKetThuc AND :endDate >= km.ngayBatDau) \r\n"
				+ "  OR (:endDate >= km.ngayKetThuc AND :startDate <= km.ngayKetThuc) \r\n"
				+ ")")
	List<Integer> getValidProductNotPromotionVesion2(@Param(value ="l")List<Integer>l,@Param("shopId")Integer id,
													@Param(value ="startDate")Date startDate,
													@Param(value ="endDate")Date endDate,@Param("id")Integer idKm );	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 // endđ
	 
	 @Query("SELECT km FROM KhuyenMai km WHERE km.id = :id AND km.shop.id = :shopId")
	 Optional<KhuyenMai> findKhuyenMaiByIdAndShopId(@Param("id") int id, @Param("shopId") int shopId);
//	 int id, String tenKhuyenMai,
//		java.util.Date ngayBatDau, java.util.Date ngayKetThuc,
//		 int giaTriKhuyenMai
	 @Query("SELECT new com.API.model.KhuyenMai(km.id,km.tenKhuyenMai,km.ngayBatDau,km.ngayKetThuc,km.giaTriKhuyenMai) FROM KhuyenMai km WHERE km.id = :id AND km.shop.id = :shopId")
	 Optional<KhuyenMai> findKhuyenMaiByIdAndShopIdVersion(@Param("id") int id, @Param("shopId") int shopId);
	 
	 @Query("SELECT km.product.id FROM ChiTietKhuyenMai km WHERE km.khuyenMai.id = :id ")
	 List<Integer> getProductIdInKhuyenMaiById(@Param("id") int id);
	
	 
	 
	 
	 @Query("SELECT km.id FROM KhuyenMai km WHERE km.id = :id AND km.shop.id = :shopId")
	 Optional<Integer> getIdByShopId(@Param("id") int id, @Param("shopId") int shopId);
	 
	 // delete chi tiết khuyễn mãi.
	 @Modifying
	 @Query("delete ChiTietKhuyenMai k where k.khuyenMai.id=:id")
	 Optional<Integer> deleteKhuyenMaiItem(@Param("id") int id);
	 
	  @Query("SELECT k FROM KhuyenMai k WHERE k.shop.id = :shopId ")
	   List<KhuyenMai> findKhuyenMaiByShopIdAndKhuyenMaiId(@Param("shopId") int shopId);
	  
	  
	  
	 
	  
	  
	  // lấy thông tin khuyến mãi ,... kèm product images;

	  @Query("SELECT new com.API.DTO.khuyenMai.KhuyenMaiViewSaler(km.trangThai,km.id, km.tenKhuyenMai, km.ngayBatDau, km.ngayKetThuc, km.giaTriKhuyenMai, GROUP_CONCAT(p.hinhAnh, '-')) " +
		       "FROM KhuyenMai km " +
		       "JOIN ChiTietKhuyenMai k ON km.id = k.khuyenMai.id " +
		       "JOIN Product p ON p.id = k.product.id " +
		       "WHERE km.shop.shopId = :shopId " +
		       "AND (:status = 0 OR " +
		       "  (CASE " +
		       "     WHEN :status = 1 THEN km.ngayBatDau > CURRENT_TIMESTAMP " +
		       "     WHEN :status = 2 THEN km.ngayBatDau <= CURRENT_TIMESTAMP AND km.ngayKetThuc >= CURRENT_TIMESTAMP " +
		       "     WHEN :status = 3 THEN km.ngayKetThuc < CURRENT_TIMESTAMP " +
		       "     ELSE false " + // Trường hợp không khớp với bất kỳ status nào
		       "   END)) and (km.tenKhuyenMai like :key or p.tenSanPham like :key) " +
		       "GROUP BY km.id")

		Page<List<KhuyenMaiViewSaler>> findKhuyenMaiByShopId(Pageable pageable,@Param("shopId")Integer shopId,@Param("status")Integer status,@Param("key") String key);
	  
	  @Query("SELECT new com.API.DTO.khuyenMai.KhuyenMaiViewSaler(km.id, km.tenKhuyenMai, km.ngayBatDau, km.ngayKetThuc, km.giaTriKhuyenMai, GROUP_CONCAT(p.hinhAnh, '-')) " +
		       "FROM KhuyenMai km " +
		       "JOIN ChiTietKhuyenMai k ON km.id = k.khuyenMai.id " +
		       "JOIN Product p ON p.id = k.product.id " +
		       "WHERE km.shop.shopId = :shopId AND  km.ngayBatDau between :startDate and :endDate   " +
		       "AND (:status = 0 OR " +
		       "  (CASE " +
		       "     WHEN :status = 1 THEN km.ngayBatDau > CURRENT_TIMESTAMP " +
		       "     WHEN :status = 2 THEN km.ngayBatDau <= CURRENT_TIMESTAMP AND km.ngayKetThuc >= CURRENT_TIMESTAMP " +
		       "     WHEN :status = 3 THEN km.ngayKetThuc < CURRENT_TIMESTAMP " +
		       "     ELSE false " + // Trường hợp không khớp với bất kỳ status nào
		       "   END)) and (km.tenKhuyenMai like :key or p.tenSanPham like :key) " +
		       "GROUP BY km.id")

		Page<List<KhuyenMaiViewSaler>> findKhuyenMaiByShopIdAndDate(Pageable pageable,@Param("shopId")Integer shopId,@Param("status")Integer status,@Param("key") String key,
								@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	  
	  
//		public KhuyenMai(int id, String tenKhuyenMai,
//				java.util.Date ngayBatDau, java.util.Date ngayKetThuc,
//				 int giaTriKhuyenMai) {
//		public KhuyenMaiViewSaler(int id, String tenKhuyenMai, Date ngayBatDau, Date ngayKetThuc,
//				float giaTriGiam, Object images) {
	
	  @Query("select new com.API.DTO.khuyenMai.KhuyenMaiViewSaler(p.id,p.tenKhuyenMai,p.ngayBatDau,p.ngayKetThuc,p.giaTriKhuyenMai)"
	  		+ " from KhuyenMai p where p.id=:id and p.shop.shopId=:shopId")
	  public Optional<KhuyenMaiViewSaler> getKhuyenMaiById(@Param("id")Integer id,@Param("shopId")Integer shopId);
	  
	  
	  
	  
	  @Query("	  SELECT new com.API.DTO.khuyenMai.KhuyenMaiItem(p.id,p.id,p.tenSanPham,"
		  		+ "      MIN(pd.giaBan),MAX(pd.giaBan),sum(pd.soLuong))  FROM Product p\r\n"
		  		+ "	 JOIN ChiTietKhuyenMai ck ON ck.product.id=p.id JOIN ProductDetail pd ON p.id=pd.product.id \r\n"
		  		+ "	   where ck.khuyenMai.id=:id\r\n"
		  		+ "	  GROUP BY (p.id) ")
			  public Optional<List<ChiTietKhuyenMai>> getChiTietKhuyenMaiByIdKhuyenMai(@Param("id")Integer id);

//	  public void man() {
//		  new KhuyenMaiItem(null, null, null, 0, null)
//	  }
	  // get item khuyen mai
//	  (Integer id, String productId, String productName, float  minPrice,float maxPrice, Integer soLuong)
	  @Query("	  SELECT new com.API.DTO.khuyenMai.KhuyenMaiItem(p.id,p.id,p.tenSanPham,"
	  		+ "      MIN(pd.giaBan),MAX(pd.giaBan),sum(pd.soLuong))  FROM Product p\r\n"
	  		+ "	 JOIN ChiTietKhuyenMai ck ON ck.product.id=p.id JOIN ProductDetail pd ON p.id=pd.product.id \r\n"
	  		+ "	   where ck.khuyenMai.id=:id\r\n"
	  		+ "	  GROUP BY (p.id) ")
		  public Optional<List<KhuyenMaiItem>> getItemKhuyenMaiById(@Param("id")Integer id);
//	  
	  // Mới nek
	  @Query(value = "SELECT p.* FROM khuyenmai km \r\n"
	  		+ "JOIN chitietkhuyenmai ck ON km.id=147 AND ck.khuyenMaiId=km.id\r\n"
	  		+ "JOIN sanpham p ON p.id=ck.productId",nativeQuery = true)
	  public Object[] getALl();
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  // THÊM MỚI NHẤT
	  
	  @Modifying
	  @Transactional
	  @Query("update KhuyenMai s set s.trangThai=:trangThai where s.id=:id and s.shop.shopId=:shopId")
	  Integer updateStateKhuyenMai(@Param("trangThai")Integer trangThai,@Param("id")Integer id ,@Param("shopId")Integer shopId );

//	  @Query("SELECT new com.API.DTO.khuyenMai.KhuyenMaiViewSaler(GROUP_CONCAT(' ')) " +
//		       "FROM KhuyenMai km "
//			  +
//		       "GROUP BY km.id")
//		List<KhuyenMaiViewSaler> findKhuyenMaiByShopId();

//	    (int id, String tenKhuyenMai, Date ngayBatDau, Date ngayKetThuc, int loaiGiaGia,
//				float giaTriGiam, String images)
//	   @Query("SELECT km FROM KhuyenMai km WHERE km.shop.id = :shopId AND (:ngayBatDau >= km.ngayBatDau and :ngayBatDau >= km.ngayBatDau) AND"
//	   		+ "")
//	    List<KhuyenMai> findByShopIdAndNgayBatDau(@Param("shopId") int shopId, @Param("ngayBatDau") java.util.Date ngayBatDau);
}

class Name {
	private String[] s;
	
	public Name() {
		
	}
	
	public Name(Object s) {
		this.s=((String) s).split("-");
	}

	public String[] getS() {
		return s;
	}

	public void setS(String[] s) {
		this.s = s;
	}
	
}
