package com.API.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.API.DTO.ProductDTO;
import com.API.model.ChiTietKhuyenMai;
import com.API.model.Product;

import jakarta.transaction.Transactional;

public interface ChiTietKhuyenMaiRepository extends JpaRepository<ChiTietKhuyenMai,Integer> {
	@Query("SELECT c.product.id FROM ChiTietKhuyenMai c WHERE c.khuyenMai.id IN :khuyenMaiIds")
    Set<Integer> findByKhuyenMaiIds(@Param("khuyenMaiIds") Set<Integer> khuyenMaiIds);
	
	
	@Modifying
    @Transactional
    @Query("DELETE FROM ChiTietKhuyenMai km WHERE km.product.id IN :ids and km.khuyenMai.id=:id")
    Integer deleteAllByIds(@Param("ids")List<Integer> ids,@Param("id")Integer id);
	
//	 @Query("SELECT c FROM ChiTietKhuyenMai c WHERE c.id = :id AND c.khuyenMai.id = :khuyenMaiId")
//	    Optional<ChiTietKhuyenMai> findCountItem(@Param("id") int id, @Param("khuyenMaiId") int khuyenMaiId);
	
	//*** bản cũ >
	
//	@Query("SELECT DISTINCT b.product.id FROM ChiTietKhuyenMai b " +
//		       "JOIN KhuyenMai c ON b.khuyenMai.id = c.id " +
//		       "WHERE (c.ngayBatDau BETWEEN :startDate AND :endDate " +
//		       "OR c.ngayKetThuc BETWEEN :startDate AND :endDate) " +
//		       "AND b.product.id IN :productIds")
//		Set<Integer> findDistinctProductIds(
//		       @Param("startDate") java.util.Date startDate,
//		       @Param("endDate") java.util.Date endDate,
//		       @Param("productIds") Set<Integer> productIds);
	// bảng mới >>
	@Query("SELECT  b.product.id FROM ChiTietKhuyenMai b " +
		       "JOIN KhuyenMai c ON b.khuyenMai.id = c.id " +
		       "WHERE (c.ngayBatDau BETWEEN :startDate AND :endDate " +
		       "OR c.ngayKetThuc BETWEEN :startDate AND :endDate) " +
		       "AND b.product.id IN :productIds")
		Set<Integer> findDistinctProductIds(
		       @Param("startDate") java.util.Date startDate,
		       @Param("endDate") java.util.Date endDate,
		       @Param("productIds") Set<Integer> productIds);
	@Query("SELECT new com.API.model.Product(c.product.id,c.product.tenSanPham,c.product.soLuong) FROM ChiTietKhuyenMai c WHERE c.product.shop.shopId=:shopId"
			+ " and  c.khuyenMai.ngayBatDau NOT BETWEEN  :startDate and :endDate"
			+ " AND c.khuyenMai.ngayKetThuc NOT BETWEEN  :startDate and :endDate")
    List<Product> getIdProductNotInPromotion(@Param("shopId") int ShopId, @Param("startDate") Date startDate
    		,@Param("endDate") Date endDate,Pageable p);
	
//	@Query("SELECT new com.API.model.Product(c.product.id,c.product.tenSanPham,c.product.soLuong) FROM ChiTietKhuyenMai c WHERE c.product.shop.shopId=:shopId"
//			+ " and  c.khuyenMai.ngayBatDau NOT BETWEEN  :startDate and :endDate"
//			+ " AND c.khuyenMai.ngayKetThuc NOT BETWEEN  :startDate and :endDate")
//    List<Product> getIdProductNotInPromotionVersionTrue(@Param("shopId") int ShopId, @Param("startDate") Date startDate
//    		,@Param("endDate") Date endDate);
//	
	
	//(int productId, String productName,int soLuong, float productPrice, String image, int trangThai)
	
    @Query("SELECT c FROM ChiTietKhuyenMai c WHERE c.id = :id AND c.khuyenMai.id = :khuyenMaiId")
    Optional<ChiTietKhuyenMai> findByCustomQuery(@Param("id") int id, @Param("khuyenMaiId") int khuyenMaiId);
    
    @Query("SELECT c.id FROM ChiTietKhuyenMai c WHERE c.khuyenMai.id=:khuyenMaiId and c.product.id in  :id  ")
    List<Integer> findByCustomQuerys(@Param("id") List<Integer> id,@Param("khuyenMaiId") int khuyenMaiId);
	
}
