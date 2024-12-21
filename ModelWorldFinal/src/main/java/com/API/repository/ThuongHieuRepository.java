package com.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.API.model.ThuongHieu;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu,Integer> {
	@Query(value = "SELECT id, tenThuongHieu FROM thuonghieu", nativeQuery = true)
	List<Object[]> getFillterThuongHieu();
}
