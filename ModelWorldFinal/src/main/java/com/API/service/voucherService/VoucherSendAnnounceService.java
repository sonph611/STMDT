package com.API.service.voucherService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.API.model.Account;
import com.API.model.ChiTietThongBao;
import com.API.model.Order;
import com.API.model.ThongBao;
import com.API.model.TypeAnnounce;
import com.API.model.VoucherShop;
import com.API.repository.ChiTietThongBaoRepository;
import com.API.repository.DonHangRepository;
import com.API.repository.ThongBaoRepository;
@Service
public class VoucherSendAnnounceService {
	@Autowired
	private DonHangRepository orderRepository;
	
	@Autowired
	private ThongBaoRepository thongBaoRepository;
	
	@Autowired
	private ChiTietThongBaoRepository chiTietThongBaoRepository;
	
	@Autowired
	DonHangRepository r;
	
	@Async("taskExecutor")

	public void senThongBaoVoucher(VoucherShop v,Integer shopId,Integer accountId,String moTaThongBao) {
		List<Account> accounts= orderRepository.getUserOrderInShop(shopId);
		ThongBao tb=new ThongBao(moTaThongBao,v.getMoTa(),v.getId()+"", "http://res.cloudinary.com/doa9sdr6z/image/upload/v1733626751/nwx7p0aspauh7imbjynr.png",TypeAnnounce.VOUCHER, new Account(accountId));
		List<ChiTietThongBao> chiTietThongBaos=new ArrayList<ChiTietThongBao>();
		accounts.forEach(vv->{
			chiTietThongBaos.add(new ChiTietThongBao(tb, vv,0));
		});
		thongBaoRepository.save(tb);
		chiTietThongBaoRepository.saveAll(chiTietThongBaos);
	}
	@Async("taskExecutor")

	public void senThongBaoOrder(Integer v,String moTaThongBao,Integer a) {
		Integer accountId=r.getAccountIdByOrderId(v);
		ThongBao tb=new ThongBao(moTaThongBao,"➡︎ ➡︎ ➡︎ Đơn hàng "+v+" đã chuyển sang trạng thái "+state.get(a+1)+" xem ngay để biết thêm chi tiết.",v+"", "http://res.cloudinary.com/doa9sdr6z/image/upload/v1733623648/xjsrbpup01rf6fbdzqjj.png",TypeAnnounce.ORDER, new Account(accountId));
		
		List<ChiTietThongBao> chiTietThongBaos=new ArrayList<ChiTietThongBao>();

		thongBaoRepository.save(tb);
		
		chiTietThongBaoRepository.save(new ChiTietThongBao(tb,new Account(accountId),0));
	}
	
	@Async("taskExecutor")

	public void senThongBaoOrderCancel(String hinhAnh,Integer v,String moTaThongBao,String a) {
		Integer accountId=r.getAccountIdByOrderId(v);
		ThongBao tb=new ThongBao(moTaThongBao,a,v+"",hinhAnh,TypeAnnounce.ORDER, new Account(accountId));
		
		List<ChiTietThongBao> chiTietThongBaos=new ArrayList<ChiTietThongBao>();

		thongBaoRepository.save(tb);
		chiTietThongBaoRepository.save(new ChiTietThongBao(tb,new Account(accountId),0));
	}
	
	private Map<Integer, String> state = new HashMap<Integer, String>() {{
	    put(3, "Đã giao cho đơn vị vận chuyển");
	    put(4, "Đang giao hàng");
	    put(5, "Đã giao hàng");
	    put(6, "Đơn đã hoàn thành");
	    put(7, "Giao  thất bại");
	    put(8, "Đơn hủy");
	}};

	
}
