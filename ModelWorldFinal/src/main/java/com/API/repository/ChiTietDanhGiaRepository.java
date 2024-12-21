package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.ChiTietDanhGia;
@Repository
public interface ChiTietDanhGiaRepository extends JpaRepository<ChiTietDanhGia, Integer>{

}
