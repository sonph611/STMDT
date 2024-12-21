package com.API.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.LichSuTimKim;
import com.API.repository.SearchHistoryRepository;
import com.API.utils.ObjectRespone;

@RestController
@CrossOrigin("*")
public class SearchController {
	
	@Autowired
	SearchHistoryRepository searchHistoryRepository;
	
	
	@PostMapping("/add/searchHistory")
	public ResponseEntity<ObjectRespone> addSearchHistory(
	    @RequestParam int userId,
	    @RequestParam String search_term) {

	    // Kiểm tra số lượng lịch sử hiện tại của userId
	    List<LichSuTimKim> histories = searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId);
	    if (histories.size() >= 5) {
	        // Xóa bản ghi cũ nhất
	        LichSuTimKim oldestHistory = histories.get(histories.size() - 1); // Lấy bản ghi cuối cùng
	        searchHistoryRepository.delete(oldestHistory);
	    }

	    // Chuẩn bị đối tượng để lưu vào database
	    LichSuTimKim history = new LichSuTimKim();
	    history.setuserId(userId);
	    history.setSearch_term(search_term);
	    history.setSearched_at(LocalDateTime.now());

	    // Gọi repository để lưu
	    searchHistoryRepository.save(history);

	    // Tạo phản hồi thành công
	    return ResponseEntity.ok(new ObjectRespone(200, "success", null));
	}
	@GetMapping("/get/searchHistory")
	public ResponseEntity<List<LichSuTimKim>> getSearchHistory(@RequestParam int userId) {
	    List<LichSuTimKim> historyList = searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId);
	    return ResponseEntity.ok(historyList); 
	}
 
	@DeleteMapping("/delete/searchHistory")
	public ResponseEntity<ObjectRespone> deleteSearchHistory(@RequestParam int userId, @RequestParam String search_term) {
	    // Tìm bản ghi cần xóa
	    Optional<LichSuTimKim> history = searchHistoryRepository.findByUserIdAndSearchTerm(userId, search_term);
	    if (history.isPresent()) {
	        searchHistoryRepository.delete(history.get());
	        return ResponseEntity.ok(new ObjectRespone(200, "Deleted successfully", null));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectRespone(404, "History not found", null));
	    }
	}
}
