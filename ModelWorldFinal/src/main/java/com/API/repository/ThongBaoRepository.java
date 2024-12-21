package com.API.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.ChiTietThongBao;
import com.API.model.ThongBao;

import jakarta.transaction.Transactional;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao,Integer>{
	
	
	@Query("select new com.API.model.ChiTietThongBao(p.id,p.thongBao,p.TrangThaiDoc) "
			+ "from ChiTietThongBao p where p.account.id=:accountId and  p.thongBao.loaiThongBao like:type order by p.id DESC")
	Page<ChiTietThongBao> getThongBaoByType(@Param("type")String type,Pageable p,@Param("accountId")Integer accountId);
	
	@Query("select new com.API.model.ChiTietThongBao(p.id,p.thongBao,p.TrangThaiDoc) "
			+ "from ChiTietThongBao p where p.account.id=:accountId  order by p.id DESC")
	Page<ChiTietThongBao> getThongBaoHeader(Pageable p,@Param("accountId")Integer accountId);
	
	@Query("select count(d.id) from ChiTietThongBao d where d.account.id=:accountId and d.TrangThaiDoc=0")
	Integer countNotReadedByAccountId(@Param("accountId")Integer accountId);
	
	
	@Modifying
	@Transactional
	@Query("update ChiTietThongBao t set t.TrangThaiDoc=1 where t.id=:id and t.account.id=:accountId")
	Integer updateReadedByIdAnUserId(@Param("id")Integer id,@Param("accountId")Integer accountId);
	
	@Modifying
	@Transactional
	@Query("update ChiTietThongBao t set t.TrangThaiDoc=1 where t.id in:id and t.account.id=:accountId")
	Integer updateReadedByIdAnUserInIds(@Param("id")List<Integer> id,@Param("accountId")Integer accountId);
	
}
