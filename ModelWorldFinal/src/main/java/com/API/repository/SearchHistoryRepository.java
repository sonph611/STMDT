package com.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.LichSuTimKim;

@Repository
public interface SearchHistoryRepository extends JpaRepository<LichSuTimKim, Integer> {
	List<LichSuTimKim> findByUserIdOrderBySearchedAtDesc(int userId);
	Optional<LichSuTimKim> findByUserIdAndSearchTerm(int userId, String searchTerm);
}
