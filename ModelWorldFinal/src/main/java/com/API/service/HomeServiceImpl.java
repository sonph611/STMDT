package com.API.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.API.repository.LichSuSanPhamRepository;
import com.API.repository.LichSuTimKimRepository;
import com.API.repository.ProductRepository;

@Service
public class HomeServiceImpl implements HomeService{
	@Autowired
	ProductRepository productRepository;
	@Autowired
	LichSuSanPhamRepository lichSuSanPham;
	@Autowired
	LichSuTimKimRepository lichSuTimKim;
	
	String trangThaiSanPham = "DangBan";
	
	@Override
	public List<Object[]> getProducts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<Object[]> productPage = productRepository.findAllByTrangThaiOrderByIdDesc(1, pageable);
		return productPage.getContent();
	}

	public List<String> findSearchTerm(int userId) {
		
		return lichSuTimKim.findSearchTermsByUserId(userId);
	}

	@Override
	public List<Integer> findCaterogy(int userId) {
		
		return lichSuSanPham.findCaterogyByUserId(userId);
	}

	@Override
	public List<Object[]> getProductsLoginIn(int page, int size, List<Integer> caterogy, List<String> searchTerm) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<Object[]> productPage = productRepository.productLoginIn(1, searchTerm, caterogy, pageable);
		return productPage.getContent();
	}
}
