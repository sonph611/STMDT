package com.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.API.repository.KichThuocRespository;
import com.API.repository.MauSacRepository;

@Service
public class Sr {
	@Autowired
	private MauSacRepository m;
	@Autowired
	private KichThuocRespository kt;
//	public void saveMauSacAndKc
}
