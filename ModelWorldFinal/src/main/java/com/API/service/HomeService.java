package com.API.service;

import java.util.List;

public interface HomeService {
	public List<Object[]> getProducts(int page, int size);
	public List<Object[]> getProductsLoginIn(int page, int size, List<Integer> caterogy, List<String> searchTerm);
	public List<String> findSearchTerm(int userId);
	public List<Integer> findCaterogy(int userId);
}
