package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.ReportOrder;
@Repository
public interface OrdeReportRepository extends JpaRepository<ReportOrder, Integer>{
	
}
