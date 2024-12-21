package com.API.controller.user;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.ChiTietDanhGia;
import com.API.model.DanhGiaSanPham;
import com.API.repository.ChiTietDanhGiaRepository;
import com.API.repository.DanhGiaRepository;
import com.API.repository.DonHangRepository;
import com.API.repository.OrderDetailRepository;
import com.API.repository.ProductRepository;
import com.API.service.saler.ShopFilter;
import com.API.service.saler.UserFilter;
import com.API.utils.ObjectRespone;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@CrossOrigin("*")
public class DanhGiaController {
	
	@Autowired
	DanhGiaRepository danhGiaRepository;
	
	@Autowired
	DonHangRepository orderRepository;
	
	@Autowired
	UserFilter userFilter;
	
	@Autowired
	ShopFilter shopFilter;
	
	@Autowired
	OrderDetailRepository orderDetailRepoitory;
	
	@Autowired
	ChiTietDanhGiaRepository chiTietDanhGiaRepository;
	@Autowired
	ProductRepository productRepository;
	
	@PostMapping("/danhgia/getbyproductid")
	public Object getMethodName(@RequestBody List<Integer> ids,@RequestParam(name="page",defaultValue = "0")Integer page,@RequestParam(name="key",defaultValue = "")String key) {
//		System.out.println(page);
		Page<DanhGiaSanPham>l=danhGiaRepository.getDanhGiaByProductIds(ids,PageRequest.of(page, 5),"%"+key.trim()+"%");
		List<ChiTietDanhGia> ct=danhGiaRepository.getChiTietDanhGiaInIds(l.getContent().stream() .map(DanhGiaSanPham::getId) .collect(Collectors.toList()));
		Map<Integer, List<ChiTietDanhGia>> danhGiaMap = ct.stream() .collect(Collectors.groupingBy(chiTiet -> chiTiet.getDanhGiaSanPham().getId()));
		l.getContent().forEach(v->{
			v.setChiTietDanhGias(danhGiaMap.get(v.getId()));
		});
//		System.out.println(l.getContent().size());
		return l;
	}
	
	@PostMapping("/danhgia/getcountstart")
	public Object  getMethodNames(@RequestBody List<Integer> ids,@RequestParam("shopId")Integer id) {
		List<Integer[]>l=danhGiaRepository.getCountStar(ids);
		Map<Integer, Integer> m = l .stream().collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
		m.put(7,0);
//		System.out.println(m.size());
		int totalSum = l.stream()
                .mapToInt(arr -> {
                	m.put(7,m.get(7)+arr[1]);
                    return arr[0] * arr[1];
                })
                .sum();
		m.put(6,totalSum);
		m.put(9,productRepository.countRatingInShopByShopid(id));
		m.put(8,productRepository.countProductInShopByShopid(id));
		return m;
	}
	
	
	@GetMapping("/sale/danhgia/getorderdetailbyid")
	public Object getMethodName(@RequestParam("id")Integer id) {
		return orderRepository.getOrderDetailById(id);
//		return orderDetailRepoitory.findById(id);
	}
	
	@PostMapping("/sale/danhgia/updatecommentdanhgia")
	public ObjectRespone postMethodName(@RequestBody  String comment,@RequestParam(name="danhGiaId",defaultValue = "-1")Integer id) {
		if (comment.strip().length() < 1) {
	        return new ObjectRespone(400, "Nội dung bình luận không được để trống", null);
	    }
		if(danhGiaRepository.getIdDanhGiaById(shopFilter.getShopId(),id).orElse(null)!=-1) {
			danhGiaRepository.updateComment(comment, id);
			return new ObjectRespone(400,"Phản hồi đơn đánh giá thành công", null);
		}
		return new ObjectRespone(400,"Đơn hàng đánh giá có vẻ không hợp lệ vui lòng thử lại sao", null);
	}
	
	
	
	
	@PostMapping("/user/auth/adddanhgia")
	public ObjectRespone postMethodName(@Valid @RequestBody DanhGiaSanPham d) {
		if(orderRepository.getOrderDetailWhereNotRating(d.getOrderDetail().getId(),userFilter.getAccount().getId()).orElse(-1)==-1) {
			d.setAccount(userFilter.getAccount());
			d.setNgayDanhGia(new Date());
			orderRepository.updateRetingOrderDetail(d.getOrderDetail().getId());
			d.getChiTietDanhGias().forEach(v->v.setDanhGiaSanPham(d));
			danhGiaRepository.save(d);
			chiTietDanhGiaRepository.saveAll(d.getChiTietDanhGias());
			return new ObjectRespone(200,"success",null);
		}else {
			return new ObjectRespone(400,"Đơn hàng có thể đã đánh giá hoặc không hợp lệ, vui lòng kiểm tra sao",null);
		}
	}
	
	@GetMapping("/user/auth/adddanhgia")
	public ObjectRespone getProductDetailById(@RequestParam("id")Integer id) {
		return null;
	}
	
	
	
	@PostMapping("/sale/danhgia/getdanhgiashop")
	public Object getDanhGiaInShop(@RequestParam(name="page",defaultValue = "0")Integer page,
										  @RequestBody List<Integer> start,@RequestParam(name="type",defaultValue = "0")Integer type,
										  @RequestParam("key")String key,
										  @RequestParam(name="spaceDate",defaultValue =  "36524")Integer date) {
		key="%"+key+"%";
		Page<DanhGiaSanPham>l=danhGiaRepository.getDanhGiaInShop(shopFilter.getShopId(), start,PageRequest.of(page, 2),type,key,LocalDateTime.now().minusDays(date));
		List<ChiTietDanhGia> ct=danhGiaRepository.getChiTietDanhGiaInIds(l.getContent().stream() .map(DanhGiaSanPham::getId) .collect(Collectors.toList()));
		Map<Integer, List<ChiTietDanhGia>> danhGiaMap = ct.stream() .collect(Collectors.groupingBy(chiTiet -> chiTiet.getDanhGiaSanPham().getId()));
		l.getContent().forEach(v->{
			v.setChiTietDanhGias(danhGiaMap.get(v.getId()));
		});
		return l;
	}
	
	
	
}
