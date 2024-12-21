package com.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.LichSuTimKim;

@Repository
public interface LichSuTimKimRepository extends JpaRepository<LichSuTimKim, Integer>{
	
	@Query(value = "SELECT sh.search_term FROM searchhistory sh WHERE sh.user_id = :userId", nativeQuery = true)
	List<String> findSearchTermsByUserId(@Param("userId") int userId);

}
