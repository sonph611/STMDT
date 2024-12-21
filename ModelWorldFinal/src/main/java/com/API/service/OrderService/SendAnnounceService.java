package com.API.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.API.model.Account;
import com.API.model.ChiTietThongBao;
import com.API.model.ThongBao;
import com.API.model.TypeAnnounce;
import com.API.repository.ChiTietThongBaoRepository;
import com.API.repository.DonHangRepository;
import com.API.repository.ThongBaoRepository;

public class SendAnnounceService {
//	@Autowired
//	private DonHangRepository orderRepository;
	
	@Autowired
	private ThongBaoRepository thongBaoRepository;
	
	@Autowired
	private ChiTietThongBaoRepository chiTietThongBaoRepository;
	
	@Async("taskExecutor")
	public void senThongBaoVoucher(Integer accountId) {
		ThongBao tb=new ThongBao("Thông báo cửa hàng.","Có một đơn hàng cần xử lý bây giờ", "lienketdenvoucher", "hinhanh.jpg",TypeAnnounce.VOUCHER, new Account(2));// set cứng id admin
		thongBaoRepository.save(tb);
		chiTietThongBaoRepository.save(new ChiTietThongBao(tb,new Account(accountId),0));
	}
}
