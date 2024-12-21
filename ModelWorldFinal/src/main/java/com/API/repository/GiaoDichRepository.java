package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.GiaoDich;

@Repository
public interface GiaoDichRepository extends JpaRepository<GiaoDich,Integer>{
	
}
