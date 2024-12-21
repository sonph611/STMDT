package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.API.model.ViewedCategory;

public interface ViewedCategoryRepository extends JpaRepository<ViewedCategory, Integer> {
	 long countByUserId(int userId);

	    // Lấy bản ghi cũ nhất của một user (theo ngày xem)
	    ViewedCategory findTopByUserIdOrderByViewedAtAsc(int userId);
}
