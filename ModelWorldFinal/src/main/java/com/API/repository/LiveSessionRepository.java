package com.API.repository;


import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.LiveSession;
import com.google.common.base.Optional;

import jakarta.transaction.Transactional;

@Repository
public interface LiveSessionRepository extends JpaRepository<LiveSession, Integer> {
	@Transactional
	@Modifying
	@Query("update Live_Product s set s.soLuongGioiHan=-1, s.giaGiam=0 where s.live.id=:id")
	Integer clearAllPromotion(@Param("id")Integer id);

	@Query("select p from LiveSession p where p.shop.shopId=:shopId and p.id=:liveId and p.startTime is not  null and p.endTime is null")
	Optional<LiveSession> getLiveByIdAndIsStart(@Param("liveId")Integer liveId,@Param("shopId")Integer shopId);
	
	@Query("select count(p.id) from Live_Product p where p.id in:ids and p.live.id=:liveId")
	public Integer getLiveItemInIds(@Param("ids")List<Integer>l,@Param("liveId")Integer liveId);
	
	@Query("select s.id from LiveSession s where s.id=:liveId and s.shop.id=:shopId and s.endTime is null")
	Integer getLiveIdByIdAndShopId(@Param("liveId")Integer liveId,@Param("shopId")Integer shopId);
	
	@Modifying
	@Transactional
	@Query("UPDATE Live_Product p SET p.soLuongGioiHan = :sl, p.giaGiam = :giaGiam WHERE p.id = :id")
	void upDateSoLuongAndGiaBan(@Param("id") Integer id, 
	                            @Param("sl") Integer sl, 
	                            @Param("giaGiam") Integer giaGiam);
	
	@Query("select p from LiveSession p where p.shop.shopId=:shopId order by p.id DESC")
	Page<LiveSession> getAllLiveByShopId(@Param("shopId")Integer shopId,Pageable p);
	
	// update lại số lượng khuyến mãi đã dùng trong livesession nha bạn ơi.
//	@Modifying
//	@Transactional
//	@Query("UPDATE Live_Product p SET p.soLuongGioiHan = p.soLuongGioiHan-:sl where p.soLuong")
//	void updateSoLuongDaDungCuaLive(@Param("id") Integer id, 
//	                            @Param("sl") Integer sl, 
//	                            @Param("giaGiam") Integer giaGiam);
	
	@Query("select new com.API.model.LiveSession(s.tieuDe,s.shop.shopId,s.shop.shopName,s.shop.hinhAnh) from LiveSession s where s.id=:id")
	Optional<LiveSession> getInfoLive(@Param("id")Integer id);
	
	
	@Query("select new com.API.model.LiveSession(s.id,s.tieuDe,s.hinhAnh,s.shop.shopId,s.shop.shopName,s.shop.hinhAnh) from LiveSession s "
			+ "where s.id!=:id and s.startTime is not null and s.endTime is null")
	ArrayList<LiveSession> getAllLive(@Param("id")Integer id);
	
	@Query("select s.id from LiveSession s where s.id=:id and s.startTime is not null and endTime is null")
	Optional<Integer> checkLiveIsAlive(@Param("id")Integer id);
	
	
	@Query("select s.id from LiveSession s where s.startTime is not null and s.endTime is null and s.id !=:liveId and s.shop.shopId=:shopId")
	Optional<Integer> checkShopLive(@Param("shopId")Integer id,@Param("liveId")Integer liveId);
	
	@Modifying
	@Transactional
	@Query("UPDATE LiveSession p set p.countLike=:countLike, p.cartCount=:cartCount  WHERE p.id = :id")
	Integer updateEndLive(@Param("id") Integer id, 
	                            @Param("countLike") Integer sl, 
	                            @Param("cartCount") Integer giaGiam);
	
}
