package com.API.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "searchhistory")
public class LichSuTimKim {
	@Id
	private int id;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "search_term")
	private String searchTerm;
	@Column(name = "searched_at") 
	private LocalDateTime searchedAt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getuserId() {
		return userId;
	}
	public void setuserId(int userId) {
		this.userId = userId; 
	}
	public String getSearch_term() {
		return searchTerm;
	}
	public void setSearch_term(String search_term) {
		this.searchTerm = search_term;
	}
	public LocalDateTime getSearched_at() {
		return searchedAt;
	}
	public void setSearched_at(LocalDateTime searched_at) {
		this.searchedAt = searched_at; 
	}
	public LichSuTimKim(int id, int userId, String search_term, LocalDateTime searched_at) {
		this.id = id;
		this.userId = userId;
		this.searchTerm = search_term;
		this.searchedAt = searched_at;
	}
	public LichSuTimKim() {
	
	}
	
	
	
}