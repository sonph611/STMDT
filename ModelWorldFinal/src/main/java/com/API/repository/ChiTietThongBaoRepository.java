package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.ChiTietThongBao;

@Repository
public interface ChiTietThongBaoRepository extends JpaRepository<ChiTietThongBao, Integer>{

}
