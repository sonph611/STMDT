package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.model.OtherImageBanner;

@Repository
public interface OtherImageRepository extends JpaRepository<OtherImageBanner, Long> {
}