package com.API.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.API.model.Category;

public interface CategoryAdminService {
	public Page<Object[]> getDanhMucTheoCay(Pageable pageable);
	public List<Map<String, Object>> getRootCategories();
	public List<Map<String, Object>> getSubCategories(Integer parentId);
	public List<Map<String, Object>> mapCategoryResults(List<Object[]> results);
	public int addTheLoai(Category category);
	public Page<Object[]> FillterDanhMucTheoCay(String nameC,Pageable pageable);
	
}
