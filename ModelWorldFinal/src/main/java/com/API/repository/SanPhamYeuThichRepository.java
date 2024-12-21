package com.API.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.SanPhamYeuThich;
import com.google.common.base.Optional;

@Repository
public interface SanPhamYeuThichRepository extends JpaRepository<SanPhamYeuThich, Integer>{
	@Query("select p from SanPhamYeuThich p where p.product.id=:id and p.account.id=:accountId")
	Optional<SanPhamYeuThich> getProductFaByProductIdAndAccountId(@Param("id")Integer id,@Param("accountId")Integer accountId);
	
//	@Query("select p from SanPhamYeuThich p where p")
	
//	@Query("select p.product from SanPhamYeuThich p where p.product.id=:id and p.account.id=:accountId")
//	Optional<SanPhamYeuThich> delete (@Param("id")Integer id,@Param("accountId")Integer accountId);
	@Query("select p from SanPhamYeuThich p where p.account.id=:accountId and p.product.tenSanPham like:key  order by p.id DESC")
	Page<SanPhamYeuThich> getAllSanPhamYeuThich (Pageable p,@Param("accountId")Integer accountId,@Param("key")String key);
	
	@Query("select p.id from SanPhamYeuThich p where p.id=:id and p.account.id=:accountId")
	Optional<Integer> getIdByIdAndAccountId(@Param("id")Integer id ,@Param("accountId")Integer accountId );
	
//	@Query("delete from SanPhamYeuThich p where p.id=:id and p.account.id=:accountId")
//	Optional<Integer> deleteById(@Param("id")Integer id ,@Param("accountId")Integer accountId );
}
