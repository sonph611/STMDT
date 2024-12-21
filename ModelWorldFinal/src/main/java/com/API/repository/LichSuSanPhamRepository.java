package com.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.LichSuSanPham;

@Repository
public interface LichSuSanPhamRepository extends JpaRepository<LichSuSanPham, Integer>{
	
	@Query(value = "SELECT vc.category_id FROM viewedcategories vc WHERE vc.user_id = :userId", nativeQuery = true)
	List<Integer> findCaterogyByUserId(@Param("userId") int userId);
}
