package com.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.API.model.MauSac;

@Repository
public interface MauSacRepository  extends JpaRepository<MauSac, Integer>{
	@Query(value = "SELECT id, tenMau FROM mausac", nativeQuery = true)
	List<Object[]> getFillterMauSac();
}
