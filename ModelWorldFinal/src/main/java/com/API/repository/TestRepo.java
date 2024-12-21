package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.tests;

import jakarta.persistence.Entity;

@Repository
public interface TestRepo extends JpaRepository<tests,Integer>{
	
}
