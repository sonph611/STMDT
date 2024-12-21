package com.API.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface OdersServie {
	
	public Page<Object[]> getAllOders(int page, int size);
	public Optional<Object[]> getOrderDetailsById(Integer orderId);
	public List<Object[]> getChiTietDonHangByDonHangId(Integer orderId);
}
