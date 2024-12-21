package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.ShopReport;
@Repository
public interface ShopReportRepository extends JpaRepository<ShopReport,Integer>{

}