package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.TaiKhoanReport;
@Repository
public interface TaiKhoanReportRepository extends JpaRepository<TaiKhoanReport,Integer>{

}
