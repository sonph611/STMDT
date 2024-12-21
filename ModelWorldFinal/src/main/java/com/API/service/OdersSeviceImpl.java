package com.API.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.API.repository.DonHangRepository;

@Service
public class OdersSeviceImpl implements OdersServie{
	
	@Autowired
	DonHangRepository donHangRepository;
	
	@Override
	public Page<Object[]> getAllOders(int page, int size) {
		List<Object[]> allOrders = donHangRepository.getAllOrders();
		Pageable pageable = PageRequest.of(page, size);
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), allOrders.size());
		return new PageImpl<>(allOrders.subList(start, end), pageable, allOrders.size());
	}

	@Override
	public Optional<Object[]> getOrderDetailsById(Integer orderId) {
		return donHangRepository.findOrderDetailsById(orderId);
	}

	@Override
	public List<Object[]> getChiTietDonHangByDonHangId(Integer orderId) {
		return donHangRepository.findChiTietDonHangByDonHangId(orderId);
	}

}
