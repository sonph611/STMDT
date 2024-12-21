package com.API.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.API.model.Category;
import com.API.repository.CategoryRepository;

@Service
public class CategoryAdminServiceImpl implements CategoryAdminService{
	
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public Page<Object[]> getDanhMucTheoCay(Pageable pageable) {
	    int pageSize = pageable.getPageSize();
	    int offset = (int) pageable.getOffset();

	    // Lấy dữ liệu từ repository
	    List<Object[]> rawData = categoryRepository.findDanhMucTheoCayWithPaging(pageSize, offset);

	    // Lấy tổng số bản ghi
	    long totalElements = categoryRepository.countAllDanhMuc();

	    // Tạo Page và trả về
	    return new PageImpl<>(rawData, pageable, totalElements);
	}

	@Override
	public List<Map<String, Object>> getRootCategories() {
		 List<Object[]> results = categoryRepository.findRootCategories();
	        return mapCategoryResults(results);
	}

	@Override
	public List<Map<String, Object>> getSubCategories(Integer parentId) {
		List<Object[]> results = categoryRepository.findSubCategories(parentId);
        return mapCategoryResults(results);
	}

	@Override
	public List<Map<String, Object>> mapCategoryResults(List<Object[]> results) {
        List<Map<String, Object>> categories = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> category = new HashMap<>();
            category.put("id", row[0]);
            category.put("tenLoai", row[1]);
            category.put("trangThai", row[2]); // Luôn là cột cuối
            category.put("parentId", row[3]); // Nếu có parent_Id
            categories.add(category);
        }
        return categories;

	}

	@Override
	public int addTheLoai(Category category) {
		Category category2 = categoryRepository.save(category);
		if (category2 != null) {
			return 1;
		}
		return 0;
	}

	@Override
	public Page<Object[]> FillterDanhMucTheoCay(String nameC, Pageable pageable) {
	    int pageSize = pageable.getPageSize();
	    int offset = (int) pageable.getOffset();
	    String name = nameC;
	    // Lấy dữ liệu từ repository
	    List<Object[]> rawData = categoryRepository.fillterDanhMucTheoCayWithPaging(name,pageSize, offset);

	    // Lấy tổng số bản ghi
	    long totalElements = categoryRepository.countDanhMucTheoCay(name);

	    // Tạo Page và trả về
	    return new PageImpl<>(rawData, pageable, totalElements);
	}

	

}
