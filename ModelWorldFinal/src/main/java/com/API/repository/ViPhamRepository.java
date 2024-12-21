package com.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.ViPham;

@Repository
public interface ViPhamRepository extends JpaRepository<ViPham,Integer> {
	List<ViPham> findByLoai(int loai);
}
