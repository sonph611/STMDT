package com.API.controller.saler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.InsertKhuyenMaiDTO;
import com.API.DTO.KhuyenMaiItemDTTO;
import com.API.DTO.khuyenMai.KhuyenMaiItem;
import com.API.DTO.khuyenMai.KhuyenMaiViewSaler;
import com.API.globalException.CustomeException;
import com.API.model.ChiTietKhuyenMai;
import com.API.model.KhuyenMai;
import com.API.model.Product;
import com.API.model.ProductDetail;
import com.API.model.Shop;
import com.API.repository.ChiTietKhuyenMaiRepository;
import com.API.repository.KhuyenMaiRePository;
import com.API.repository.ProductRepository;
import com.API.service.saler.ShopFilter;
import com.API.utils.ObjectRespone;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
public class KhuyenMaiController {
	
	@Autowired
	ShopFilter shopFilter;
	
	@Autowired
	private KhuyenMaiRePository khuyenMaiRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired 
	ChiTietKhuyenMaiRepository chiTietKhuyenMaiRepository;
	
	@Autowired
	private HttpServletRequest request;
	
	
	
//	@PostMapping("/sale/khuyenmai/delete")
//	public ObjectRespone postMethodName(@RequestParam(name = "id",defaultValue = "-1")Integer id) {
//		Integer a=khuyenMaiRepository.getIdByShopId(id,shopFilter.getShopId()).orElse(-1);
//		if(a!=-1) {
//			
//		}
//		return new ObjectRespone(400,"Không tìm thấy khuyến mãi trong shop của bạn", a)
//	}
	
	
	
	@GetMapping("/sale/khuyenmai/getall")
	public ObjectRespone getMethodName(@RequestParam(name = "page",defaultValue ="0")Integer page,
										@RequestParam(name = "size",defaultValue ="5")Integer size,
										@RequestParam(name = "type",defaultValue ="0")Integer type,
										@RequestParam(name = "key",defaultValue ="")String key,
										@RequestParam(name = "typeSort",defaultValue ="0")Integer typeSort,
										@RequestParam(name = "startDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
										@RequestParam(name = "endDate",required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  Date endDate) {
		Sort sort=null;
		switch (typeSort) {
		case 0: {
			sort = Sort.by(Sort.Order.desc("id"));
			break;
		}
		case 1: {
			sort = Sort.by(Sort.Order.desc("giaTriKhuyenMai"));
			break;
		}
		default:
			sort = Sort.by(Sort.Order.asc("tenKhuyenMai"));
			return null;
		}
		key="%"+key.trim()+"%";
		if(startDate!=null&&endDate!=null) {
			return new ObjectRespone(200,"success",khuyenMaiRepository.findKhuyenMaiByShopIdAndDate(PageRequest.of(page,size,sort),shopFilter.getShopId(),type,key,startDate,endDate));

		}
		return new ObjectRespone(200,"success",khuyenMaiRepository.findKhuyenMaiByShopId(PageRequest.of(page,size,sort),shopFilter.getShopId(),type,key));
	
	}


	@PostMapping("/sale/khuyenmai/updatestate/{id}/{state}")
	public ObjectRespone postMethodName(@PathVariable("id")Integer id,@PathVariable("state")Integer state ) {
		System.out.println(state);
		Integer count=khuyenMaiRepository.updateStateKhuyenMai(state, id, shopFilter.getShopId());
		if(count==1) {
			return new ObjectRespone(200,"",null);
		}
		return new ObjectRespone(400,"Khuyến mãi không hợp lệ",null);
	}
	
	
	
	@GetMapping("/sale/khuyenmai/detail/{id}")
	public ObjectRespone getMethodNamess(@PathVariable(value = "id") Integer id) {
		KhuyenMai k=khuyenMaiRepository.findKhuyenMaiByIdAndShopIdVersion(id,shopFilter.getShopId()).orElse(null);
		if(k!=null) {
			ResponTamp r=new ResponTamp(k,khuyenMaiRepository.getProductIdInKhuyenMaiById(id));
			return new ObjectRespone(200,"success",r);
		}
		return new ObjectRespone(200,"success",k);
	}
	
	@PostMapping("/sale/khuyenmai/getitem")
	public ObjectRespone getItemKhuyenMai(@RequestBody List<Integer>l) {
		List<KhuyenMaiItem>k=khuyenMaiRepository.getKhuyenMaiItemInList(shopFilter.getShopId(),l);
		Map<Integer,KhuyenMaiItem>m=k.stream().collect(Collectors.toMap(KhuyenMaiItem::getProductId, item -> item));
		 productRepository.getProductDetailInListIdVersion(l).forEach(v->{
			m.get(v.getProduct().getId()).getProductDetails().add(v);
		});
		return new ObjectRespone(200,"success",k);
	}
	
	
	
//	@GetMapping("/test")
//	public Object testDare(
//			@RequestParam("from") String fromDate
//			) {
//		 Date d=parseDate(fromDate);
//		return d;
//	}
//	
//	
//	public Date parseDate(String date) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            return formatter.parse(date);
//        } catch (java.text.ParseException e) {
//			return null;
//		}
//	}
	
	
	
	
//	public Shop getShop() {
//		return (Shop) request.getAttribute("shop");
//	}
	
	
	@PostMapping("/sale/khuyenmai/insert") // -- HOÀN TẤT.
	@Transactional
	public ObjectRespone addKhuyenMai( @RequestBody @Valid InsertKhuyenMaiDTO km) {
		if(km.getKhuyenMai().getNgayBatDau().after(new Date())) {
			if(km.getKhuyenMai().validDate()) {
				if(productRepository.countByIdInAndShopId(km.set,shopFilter.getShopId())==km.set.size()) {
						if(chiTietKhuyenMaiRepository.findDistinctProductIds(km.getKhuyenMai().getNgayBatDau(),km.getKhuyenMai().getNgayKetThuc(),km.set).size()<1) {
						KhuyenMai k=km.getKhuyenMai();
						k.setShop(new Shop(shopFilter.getShopId()));
						List<ChiTietKhuyenMai>chiTietKm=new ArrayList<ChiTietKhuyenMai>();
						km.set.forEach(v->chiTietKm.add(new ChiTietKhuyenMai(k,new Product(v),0)));
						khuyenMaiRepository.save(km.getKhuyenMai());
						chiTietKhuyenMaiRepository.saveAll(chiTietKm);
						return new ObjectRespone(200,"Thêm thành công khuyến mãi !!!",null);	
					}	
					return new ObjectRespone(400,"Thêm thất bại, sản phẩm bạn cung cấp đang trong chương trình khuyến mãi khác !!!",null); 
				}
				return new ObjectRespone(403,"Danh sách sản phẩm không hợp lệ !!!",null);			
			}
			return new ObjectRespone(400,"Khuyến mãi kéo dài ít nhất một giờ !!!",null); 
		}
		return new ObjectRespone(400,"Ngày bắt đầu khuyến mãi phải lớn hơn ngày giờ hiện tại!!!",null);
	}
	
	
	@PostMapping("/sale/khuyenmai/checkvalid")
	public List<Integer> checkValidProduct(@RequestBody CheckValidDate d){
		return khuyenMaiRepository.getValidProductNotPromotionVesion(d.getL(),shopFilter.getShopId(),d.startDate,d.endDate);
		
	}
	
	@PostMapping("/sale/khuyenmai/checkvalidupdate")
	public List<Integer> checkValidProductUpdate(@RequestBody CheckValidDate d,@RequestParam("id")Integer id ){
		return khuyenMaiRepository.getValidProductNotPromotionVesion2(d.getL(),shopFilter.getShopId(),d.startDate,d.endDate,id);
		
	}
//	public void getList(InsertKhuyenMaiDTO d){
//		d.set= d.getListProduct().stream().map(KhuyenMaiItemDTTO::getProduct).collect(Collectors.toSet());
//	}
	
	
	
	@PostMapping("/sale/khuyenmai/delete/{kmid}") // -- HOÀN TẤT 
	@Transactional
	public ObjectRespone postMethodName(@PathVariable("kmid") int kmid) {
//		Shop s=getShop();
		Integer km= khuyenMaiRepository.getIdByShopId(kmid,shopFilter.getShopId()).orElse(null);
		if(km==null) {
			return new ObjectRespone(403,"Chương trình khuyến mãi không hợp lệ.",null);
		}
		khuyenMaiRepository.deleteById(km);
		khuyenMaiRepository.deleteKhuyenMaiItem(km);
		return new ObjectRespone(200,"Đã xóa thành công khuyến mãi.",null);
	}
	
	

	@PostMapping("/sale/khuyenmai/deleteitem/{kmid}/{itemid}")
	public ObjectRespone postMethodName(@PathVariable("kmid") int kmid,@PathVariable("itemid") int itid) {
		Shop s=new Shop();
		if(khuyenMaiRepository.findKhuyenMaiByIdAndShopId(kmid,s.getShopId()).orElse(null)!=null) {
			if(chiTietKhuyenMaiRepository.findByCustomQuery(itid,kmid).orElse(null)!=null) {
				chiTietKhuyenMaiRepository.deleteById(itid);
				return  new ObjectRespone(200, "Xóa thành công sản phẩm khỏi khuyến mãi !!!", null);
			}
			return  new ObjectRespone(403, "Sản phẩm này không có trong chương trình khuyến mãi !!!", null);
		}
		return  new ObjectRespone(400, "Chương trình khuyến mãi không phải của shop!!!", null);
	}
	
	
	@PostMapping("/sale/khuyenmai/deleteitems/{kmid}")
	public ObjectRespone postMethodName(@PathVariable("kmid")Integer id ,@RequestBody List<Integer>l) {
		if(khuyenMaiRepository.findKhuyenMaiByIdAndShopId(id,shopFilter.getShopId()).orElse(null)!=null) {
			if(chiTietKhuyenMaiRepository.deleteAllByIds(l,id)==l.size()) {
				return  new ObjectRespone(200, "Xóa thành công sản phẩm khỏi khuyến mãi !!!", null);
			}
			throw new CustomeException("Danh sách items không hợp lệ !!!");
		}
		return  new ObjectRespone(400, "Chương trình khuyến mãi không hợp lệ!!!", null);
	}
	
	
	
//	
//	@GetMapping("/sale/khuyenmai/getkhuyenmaiall")
//	public Object getMethodNames() {
//		Shop s=getShop();
//		return khuyenMaiRepository.findKhuyenMaiByShopIdAndKhuyenMaiId(s.getShopId());
//	}
//	
	
	// PHUOWMH THỨC NAY DD =================================
//	@GetMapping("/sale/khuyenmai/getproductnotpromotion/{startDate}/{endDate}")
//	public ObjectRespone getMethodName(@PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
//	Date startDate,@PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate,
//	@RequestParam(name="page",defaultValue = "0")Integer page) {
//		System.out.println(page);
//		List<Product> l= chiTietKhuyenMaiRepository.getIdProductNotInPromotion(getShop().getShopId(),startDate,endDate,PageRequest.of(page,10));
//		return new ObjectRespone(0,"success",l);
//	}
	

	@PostMapping("/sale/khuyenmai/update")
	@Transactional
	public ObjectRespone updateKhuyenMai(@Valid  @RequestBody InsertKhuyenMaiDTO km) {
		KhuyenMai k=khuyenMaiRepository.findById(km.getKhuyenMai().getId()).orElse(null);
//		Shop s=getShop();
		if(km.getKhuyenMai().getNgayKetThuc().before(km.getKhuyenMai().getNgayBatDau())||!km.getKhuyenMai().validDate()) {
			return new ObjectRespone(400,"Ngày bắt đầu phải lớn hơn ngay hiện tại và phải kéo dài ít nhất một giờ",null); 
		}
		if(k!=null) {
			Integer id =khuyenMaiRepository.findPromotionsWithinDateRangeCustome(shopFilter.getShopId(), km.getKhuyenMai().getNgayBatDau(), km.getKhuyenMai().getNgayKetThuc(),km.getKhuyenMai().getId(),km.set);
			if(id>0) {
				return new ObjectRespone(400,"Cập nhật thất bại, sản phẩm bạn cung cấp đang áp dụng khuyến mãi khác",null); 
			}
			if(productRepository.countByIdInAndShopId(km.set,shopFilter.getShopId())<km.set.size()) {
				return new ObjectRespone(403,"Các sản phẩm của bạn không thuộc danh sách này",null);
			}
			km.getKhuyenMai().setShop(new Shop(shopFilter.getShopId()));
			km.set.forEach(v->k.getChitietKhuyenMai().add(new ChiTietKhuyenMai(km.getKhuyenMai(),new Product(v),0)));
				chiTietKhuyenMaiRepository.saveAll(k.getChitietKhuyenMai());
				khuyenMaiRepository.save(km.getKhuyenMai());
				return new ObjectRespone(200,"Đã cập nhật khuyến mãi thành công",null);
		}
		return new ObjectRespone(403,"Chương trình khuyến mãi này không thuộc shop bạn",null);
	}
	
	
	@PostMapping("/sale/khuyenmai/getproductnotinptomotion")
	public ObjectRespone getProductNotInPromotion(@RequestBody CheckValidDate d,@RequestParam(name="page",defaultValue = "0")Integer page) {
		Pageable pageable = PageRequest.of(page,10);
		Page< KhuyenMaiItem>l=khuyenMaiRepository.getProductNotInPromotion(shopFilter.getShopId(),pageable,d.startDate,d.endDate);
		Map<Integer,KhuyenMaiItem>m=l.getContent().stream()
                .collect(Collectors.toMap(KhuyenMaiItem::getProductId, item -> item));
		List<ProductDetail>p= productRepository.getProductDetailInListIdVersion(l.stream()
		        .map(KhuyenMaiItem::getProductId) 
		        .collect(Collectors.toList()));
		p.forEach(v->{
			m.get(v.getProduct().getId()).getProductDetails().add(v);
		});
		return new ObjectRespone(200,"success",l);
	}
	
	
//	@PostMapping("/sale/khuyenmai/update")
//	@Transactional
//	public ObjectRespone updateKhuyenMai(@Valid  @RequestBody InsertKhuyenMaiDTO km) {
//		KhuyenMai k=khuyenMaiRepository.findById(km.getKhuyenMai().getId()).orElse(null);
//		Shop s=getShop();
//		if(k!=null) {
////			if(k.getNgayBatDau().after(km.getKhuyenMai().getNgayBatDau())||km.getKhuyenMai().getNgayKetThuc().after(k.getNgayKetThuc())||!km.getKhuyenMai().validDate()) {
////				return new ObjectRespone(400,"Ngày bắt đầu phải muộn hơn ngày Bắt đầu trước, ngày kết thúc phải sơm hơn ngày kết thúc trước và kéo dài ít nhất 1h !!!",null); 
////			}
//			Integer id =khuyenMaiRepository.findPromotionsWithinDateRangeCustome(s.getShopId(), km.getKhuyenMai().getNgayBatDau(), km.getKhuyenMai().getNgayKetThuc(),km.getKhuyenMai().getId(),km.set);
//////			getList(km);
//			if(id>0) {
//				return new ObjectRespone(400,"Cập nhật thất bại, sản phẩm bạn cung cấp đang áp dụng khuyến mãi khác",null); 
//			}
////			Set<Integer> l=  khuyenMaiRepository.findPromotionsWithinDateRange(s.getShopId(), km.getKhuyenMai().getNgayBatDau(), km.getKhuyenMai().getNgayKetThuc(),km.getKhuyenMai().getId());
////			// lất danh sách các khuyến mãi lên
////			l=chiTietKhuyenMaiRepository.findByKhuyenMaiIds(l);
////			if(l.stream().anyMatch(km.set::contains)) {
////				return new ObjectRespone(400,"Cập nhật thất bại, sản phẩm bạn cung cấp đang áp dụng khuyến mãi khác",null); 
////			}	
//			if(productRepository.countByIdInAndShopId(km.set,s.getShopId())<km.set.size()) {
//				return new ObjectRespone(403,"Các sản phẩm của bạn không thuộc danh sách này",null);
//			}
//			km.getKhuyenMai().setShop(s);
////			Set<ChiTietKhuyenMai> chiTietKm=new HashSet<ChiTietKhuyenMai>();
////			km.set.forEach(v->chiTietKm.add(new ChiTietKhuyenMai(km.getKhuyenMai(),new Product(v),0)));
////			if(!k.getChitietKhuyenMai().stream().anyMatch(chiTietKm::contains)) {
////				chiTietKhuyenMaiRepository.saveAll(chiTietKm);
////				khuyenMaiRepository.save(km.getKhuyenMai());
////				return new ObjectRespone(403,"Đã cập nhật khuyến mãi thành công",null);
////			}
////			return new ObjectRespone(403,"Sản phẩm thêm mới đã có trong cập nhật",null);
//			// mới 
//			km.set.forEach(v->k.getChitietKhuyenMai().add(new ChiTietKhuyenMai(km.getKhuyenMai(),new Product(v),0)));
//////		if(!k.getChitietKhuyenMai().stream().anyMatch(chiTietKm::contains)) {
//			chiTietKhuyenMaiRepository.saveAll(k.getChitietKhuyenMai());
//			khuyenMaiRepository.save(km.getKhuyenMai());
//			return new ObjectRespone(403,"Đã cập nhật khuyến mãi thành công",null);
//////		}
//		}
//		return new ObjectRespone(403,"Chương trình khuyến mãi này không thuộc shop bạn",null);
//	}
//	
	
	
	public Set<Integer> getListInteger(Set<ChiTietKhuyenMai> k){
		return 	k.stream().map(ChiTietKhuyenMai::getId).collect(Collectors.toSet());
		
	}
	
	
	@GetMapping("/sale/getid")
	public Object getKhuyenMaiByIds() {
		khuyenMaiRepository.findById(147);
		return khuyenMaiRepository.getALl();
	}
	
	
	
	
//	@PostMapping("/sale/khuyenmai/insert")
//	@Transactional
//	// Kiểm tra lại
//	public ObjectRespone addKhuyenMai(@Valid  @RequestBody InsertKhuyenMaiDTO km) {
//		Shop s=getShop();
//		if(!km.getKhuyenMai().lonHonHienTai()) {
//			return new ObjectRespone(400,"Ngày bắt đầu khuyến mãi phải lớn hơn ngày giờ hiện tại!!!",null); 			
//		}
//		if(!km.getKhuyenMai().validDate()) {
//			return new ObjectRespone(400,"Khuyến mãi kéo dài ít nhất một giờ !!!",null); 			
//		}
//		if(productRepository.countByIdInAndShopId(km.getListProduct(),s.getShopId())<km.getListProduct().size()) {
//			return new ObjectRespone(403,"Danh sách sản phẩm không hợp lệ !!!",null);
//		}
//		Set<Integer> l=  khuyenMaiRepository.findPromotionsWithinDateRange(s.getShopId(), km.getKhuyenMai().getNgayBatDau(), km.getKhuyenMai().getNgayKetThuc());
//		l=chiTietKhuyenMaiRepository.findByKhuyenMaiIds(l);
//		
//		if(l.size()>0&&l.containsAll(km.getListProduct())) {
//			return new ObjectRespone(400,"Thêm thất bại,ản phẩm bạn cung cấp đang trong chương trình khuyến mãi khác !!!",null); 
//		}	
//		km.getKhuyenMai().setShop(s);
//		List<ChiTietKhuyenMai>chiTietKm=new ArrayList<ChiTietKhuyenMai>();
//		khuyenMaiRepository.save(km.getKhuyenMai());
//		km.getListProduct().forEach(v->chiTietKm.add(new ChiTietKhuyenMai(km.getKhuyenMai(),new Product(v))));
//		chiTietKhuyenMaiRepository.saveAll(chiTietKm);
//		System.out.println(km.getKhuyenMai().getChitietKhuyenMai());
//		return new ObjectRespone(200,"Thên thành công khuyến mãi  !!!",null);
//	}
	
	
	
}



class ResponTamp{
	public ResponTamp(KhuyenMai khuyenMai, List<Integer> item) {
		this.khuyenMai = khuyenMai;
		this.item = item;
	}
	KhuyenMai khuyenMai;
	List<Integer> item;
	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}
	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	public List<Integer> getItem() {
		return item;
	}
	public void setItem(List<Integer> item) {
		this.item = item;
	}
	
}

class CheckValidDate{
	Date startDate;
	Date endDate;
	List<Integer> l;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<Integer> getL() {
		return l;
	}
	public void setL(List<Integer> l) {
		this.l = l;
	}
}