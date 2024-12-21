package com.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.API.model.KichThuoc;

@Repository
public interface KichThuocRespository extends JpaRepository<KichThuoc, Integer> {
	@Query(value = "SELECT id, tenKichThuoc FROM kichthuoc", nativeQuery = true)
	List<Object[]> getFillterKichThuoc();
}
