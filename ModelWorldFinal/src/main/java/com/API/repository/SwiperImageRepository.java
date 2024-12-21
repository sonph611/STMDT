package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.SwiperImage;

@Repository
public interface SwiperImageRepository extends JpaRepository<SwiperImage, Long> {
}
