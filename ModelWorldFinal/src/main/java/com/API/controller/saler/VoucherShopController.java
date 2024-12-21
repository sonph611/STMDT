package com.API.controller.saler;

import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.khuyenMai.KhuyenMaiItem;
import com.API.DTO.khuyenMai.KhuyenMaiViewSaler;
import com.API.DTO.voucher.VoucherInsert;
import com.API.DTO.voucher.VoucherUpdate;
import com.API.DTO.voucher.VoucherView;
import com.API.model.Account;
import com.API.model.Order;
import com.API.model.OrderDetail;
import com.API.model.Product;
import com.API.model.Shop;
import com.API.model.VoucherShop;
import com.API.model.VoucherShopDetail;
import com.API.repository.DonHangRepository;
import com.API.repository.ProductRepository;
import com.API.repository.VoucherRepository;
import com.API.repository.VoucherShopDetailsRepository;
import com.API.service.saler.ShopFilter;
import com.API.service.voucherService.VoucherSendAnnounceService;
import com.API.utils.ObjectRespone;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin("*")
public class VoucherShopController {
	
	@Autowired
	ShopFilter shopFilter;
	
	
	@Autowired
	private VoucherRepository voucherRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private VoucherShopDetailsRepository voucherShopDetailRepository;
	
	@Autowired
	DonHangRepository orderRepository;
	
	@Autowired
	private VoucherSendAnnounceService voucherSend;
	
	public Shop getShop() {
		return (Shop) request.getAttribute("shop");
	}
	
	@GetMapping("/sale/voucher/getproductchoose")
	public ObjectRespone getProductChoose(@RequestParam(name = "page",defaultValue = "0")Integer page,@RequestParam(name = "size",defaultValue = "5")Integer size,@RequestParam(name = "key",defaultValue = "")String key) {
		return new ObjectRespone(200, null,voucherRepository.getProductForchoose(shopFilter.getShopId(),PageRequest.of(page,size),"%"+key+"%"));
	}

	@GetMapping("/sale/voucher/getreport")
	public Object getMethodNames(@RequestParam("countday")Integer countDays) {
		return voucherRepository.getReportOrder(LocalDateTime.now().minusDays(countDays), shopFilter.getShopId());
	}
	
	
	@PostMapping("/sale/voucher/addvoucher")
	public ObjectRespone postMethodName(@RequestBody @Valid VoucherInsert voucher,@RequestParam("issend")Boolean isSend ) {
		VoucherShop v=voucher.getVoucher();
		if(v.getLoaiVoucher()==0&&v.getGiaTriGiam()<1000) {
				return new ObjectRespone(400,"Giá trị giảm vui lòng lớn hơn 1000",null);
		}else if(v.getGiaTriGiam()>99||v.getGiaTriGiam()<1) {
				return new ObjectRespone(400,"Giá trị giảm phải nằm trong khoảng 1 - 99",null);
		}
		if(voucher.getVoucher().getSoLuocDung()<voucher.getVoucher().getSoLuocMoiNguoi()) {
			return new ObjectRespone(400,"Số lược dùng phải lớn hơn số lược mỗi người",null);
		}
		v.setShop(new Shop(shopFilter.getShopId()));
		if(voucherRepository.findVoucherIdByIdAndShopIdGetInteger(v.getMaVoucher()).orElse(-1)==-1) {
			if(v.startDateIsAfterDateNow()&&v.validDate()) {
				int count=productRepository.countByIdInAndShopId(voucher.getVoucherProducts(),shopFilter.getShopId());
				if(count>voucher.getVoucherProducts().size()-1) {
					List<VoucherShopDetail> l=new ArrayList<VoucherShopDetail>();
					voucher.getVoucherProducts().forEach(b->{
						l.add(new VoucherShopDetail(b,v));
					});
					voucherRepository.save(v);
					voucherShopDetailRepository.saveAll(l);
					if(true) {
						voucherSend.senThongBaoVoucher(v,shopFilter.getShopId(),shopFilter.getAccountId(),voucher.getMoTaThongBao());
					}
					return new ObjectRespone(200,"Thêm thành công voucher",null);
				}
				return new ObjectRespone(400,"Danh sách sản phẩm không phải của shop bạn, vui lòng kiểm tra lại",null);
			}
			return new ObjectRespone(400,"Ngày bắt đầu phải lớn hơn ngày hiện tại và thời gian áp dụng phải lớn hơn 1h",null);
		}
		return new ObjectRespone(400,"Mã voucher đã có trong shop của bạn",null);
	}
	
	
	@PostMapping("/sale/voucher/deletevoucher/{id}")
	@Transactional
	public ObjectRespone deleteVoucher(@PathVariable("id") int id) {
		Integer a=(voucherRepository.findVoucherIdByIdAndShopId(id,getShop().getShopId()));
		if(a!=null) {
			voucherRepository.updateTrangThai(0, id);
			return new ObjectRespone(1,"Ẩn voucher thành công !!!",null);
		}
		return new ObjectRespone(1,"Voucher không hợp lệ !!!",null);
	}
	
	@PostMapping("/sale/voucher/updatevoucher")
	@Transactional
	public ObjectRespone updateVoucher(@Valid @RequestBody VoucherUpdate voucher) {
		VoucherShop v=voucherRepository.findById(voucher.getId()).orElse(null);
		if(v!=null&&new Date().before(v.getNgayKetThuc())) {
			List<Integer> sets=productRepository.findProductIdsByShopIdNotNavtive(shopFilter.getShopId());
			if(voucher.getVoucherShop().getSoLuocDung()<voucher.getVoucherShop().getSoLuocMoiNguoi()) {
				return new ObjectRespone(403,"Số lược dùng không thể nhỏ hơn số lược dùng mỗi người ", null);
			}
			Date d=new Date();
			voucher.getVoucherShop().setNgayBatDau(v.getNgayBatDau());
			if(d.before(voucher.getVoucherShop().getNgayKetThuc())&&voucher.getVoucherShop().validDate()){
				List<Integer> setInteger=voucherShopDetailRepository.SetfindProductIdsByShopIdAndProductIds(v.getId());	
				if( productRepository.findByShopIdAndInIds(shopFilter.getShopId(),voucher.getProductInsertVoucher()).size()> voucher.getProductInsertVoucher().size()-1) {
					if(setInteger.containsAll(voucher.getProductDeleteVoucher())) {
						List<VoucherShopDetail>list=new ArrayList<VoucherShopDetail>();
						voucher.getProductInsertVoucher().forEach(v1->list.add(new VoucherShopDetail(v1, v)));
						voucherShopDetailRepository.deleteByProductIds(voucher.getProductDeleteVoucher());
						voucherShopDetailRepository.saveAll(list);
//						if(d.after(v.getNgayBatDau())&&d.before(v.getNgayKetThuc())) {
//							voucherRepository.updateVoucherDetails(voucher.getVoucherShop().getMoTa(),
//									voucher.getVoucherShop().getTenVoucher(),voucher.getVoucherShop().getNgayKetThuc(),
//									voucher.getVoucherShop().getSoLuocDung(),voucher.getId());
//						}else {
							voucher.getVoucherShop().setMaVoucher(v.getMaVoucher());
//							voucher.getVoucherShop().setSoLuocMoiNguoi(v.getSoLuocMoiNguoi());
							voucher.getVoucherShop().setShop(new Shop(shopFilter.getShopId()));
							voucherRepository.save(voucher.getVoucherShop());
//						}
						return new ObjectRespone(200,"Cập nhật voucher thành công",null);
					}
					return new ObjectRespone(403,"Danh sách sản phẩm voucher không hợp lệ !!!", null);
				}
				return new ObjectRespone(403,"Sản phẩm của shop không hợp lệ", null);
			}
			return new ObjectRespone(400,"Ngày kết thúc phải lớn hơn ngày hiện tại và lớn hơn ngày bắt đầu ít nhất 1h",null);
		}
		return new ObjectRespone(403,"Voucher không hợp lệ !!",null);
	}
	
	@PostMapping("/sale/voucher/getvoucher")
	public Object getMethodNamePs(@RequestParam(name = "page",defaultValue ="0")Integer page,
								@RequestParam(name = "size",defaultValue ="1")Integer size,
								@RequestParam(name = "type",defaultValue ="0")Integer type,
								@RequestParam(name = "key",defaultValue ="")String key,
								@RequestParam(name = "typeSort",defaultValue ="0")Integer typeSort,
								@RequestBody List<Integer>ids) {
			key="%"+key.trim().toLowerCase()+"%";
			Sort sort=null;
			switch (typeSort) {
			case 0: {
				sort = Sort.by(Sort.Order.desc("id"));
				break;
			}
			case 1: {
				sort = Sort.by(Sort.Order.desc("soLuocDung"));
				break;
			}
			case 2: {
				sort = Sort.by(Sort.Order.desc("soLuocMoiNguoi"));
				break;
			}
			default:
				sort = Sort.by(Sort.Order.asc("tenVoucher"));
			}
			System.out.println("Xin chào");
			Pageable p=PageRequest.of(page,size,sort);
			return new ObjectRespone(400,"success",voucherRepository.findVouchersByShopIdWithProductCount(p,shopFilter.getShopId(),type,key,ids));
	}
	
	@PostMapping("/sale/voucher/updatestate")
	public Object updateStateVoucher(@RequestParam("id")Integer id,@RequestParam("trangThai")Integer trangThaiId) {
		if(voucherRepository.updateStateVoucher(trangThaiId, id, shopFilter.getShopId())==1) {
			return new ObjectRespone(200,"",null);
		}
		return new ObjectRespone(200,"Voucher không hợp lệ",null);
	}
	
	@GetMapping("/sale/voucher/getvoucher")
	public Object getMethodName(@RequestParam(name = "page",defaultValue ="0")Integer page,
								@RequestParam(name = "size",defaultValue ="1")Integer size,
								@RequestParam(name = "type",defaultValue ="0")Integer type,
								@RequestParam(name = "key",defaultValue ="")String key,
								@RequestParam(name = "typeSort",defaultValue ="0")Integer typeSort) {
			key="%"+key.trim().toLowerCase()+"%";
			Sort sort=null;
			switch (typeSort) {
			case 0: {
				sort = Sort.by(Sort.Order.desc("id"));
				break;
			}
			case 1: {
				sort = Sort.by(Sort.Order.desc("soLuocDung"));
				break;
			}
			case 2: {
				sort = Sort.by(Sort.Order.desc("soLuocMoiNguoi"));
				break;
			}
			default:
				sort = Sort.by(Sort.Order.asc("tenVoucher"));
			}
//			System.out.println("Là: "+page);
			Pageable p=PageRequest.of(page,size,sort);
			return new ObjectRespone(400,"success",voucherRepository.findVouchersByShopIdWithProductCount(p,shopFilter.getShopId(),type,key));
	}
	
	@GetMapping("/sale/voucher/getvoucherbyidandorderreport")
	public Object getVoucDeai(@RequestParam(name="id",defaultValue = "-1")Integer id) {
		VoucherShop s=voucherRepository.findVoucherIdByIdAndShopIdNha(id,shopFilter.getShopId()).orElse(null);
		if(s!=null) {
			VoucherReport voucherReport=new VoucherReport();
			voucherReport.setVoucherShop(s);
			voucherReport.setOrders(orderRepository.getOrderInVoucher(id,PageRequest.of(0, 10)).getContent());
			s.setVoucherShopDetails(null);
			return new ObjectRespone(200,null,voucherReport);
		}
		return  new ObjectRespone(401,"Không tìm thấy voucher", null);
	}
	
	@GetMapping("/sale/voucher/getordernext")
	public Object getVoucDeais(@RequestParam(name="id",defaultValue = "-1")Integer id,@RequestParam(name="page",defaultValue = "1")Integer page) {
		Integer s=voucherRepository.getVoucherIdByIdAndShopId(id,shopFilter.getShopId()).orElse(-1);
//		System.out.println(page);
		if(s!=-1) {
//			System.out.println(orderRepository.getOrderInVoucher(id,PageRequest.of(page, 10)).getContent().size());
			return new ObjectRespone(200,null,orderRepository.getOrderInVoucher(id,PageRequest.of(page, 10)).getContent());
		}
		return  new ObjectRespone(401,"Không tìm thấy voucher", null);
	}
	
	
	@GetMapping("/sale/voucher/getvoucherdetail")
	public Object getOrderNext(@RequestParam(name="id",defaultValue = "-1")Integer id) {
		Integer s=voucherRepository.getVoucherIdByIdAndShopId(id,shopFilter.getShopId()).orElse(-1);
		if(s!=-1) {
			return new ObjectRespone(200,null,voucherRepository.getVoucherDetailById(s));
		}
		return  new ObjectRespone(401,"Không tìm thấy voucher", null);
	}
	
	
	@GetMapping("/sale/voucher/getvoucherbyid")
	public Object getVoucDeailAndOrder(@RequestParam(name="id",defaultValue = "-1")Integer id) {
		VoucherShop s=voucherRepository.findVoucherIdByIdAndShopIdNha(id,shopFilter.getShopId()).orElse(null);
		if(s!=null) {
			VoucherInsert v=new VoucherInsert(s,voucherRepository.getVoucherDetailById(s.getId()));
			s.setVoucherShopDetails(null);
			return new ObjectRespone(200,null,v);
		}
		return  new ObjectRespone(401,"Không tìm thấy voucher", null);
	}
	
	
}
class responePage{
	private  ObjectRespone ObjectRespone;
	public responePage(ObjectRespone o,int page) {
		this.pageSize=page;
		this.ObjectRespone=o;
	}
	public ObjectRespone getObjectRespone() {
		return ObjectRespone;
	}
	public void setObjectRespone(ObjectRespone objectRespone) {
		ObjectRespone = objectRespone;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	private  int  pageSize;
}

class VoucherReport{
	private VoucherShop voucherShop;
	public VoucherShop getVoucherShop() {
		return voucherShop;
	}
	public void setVoucherShop(VoucherShop voucherShop) {
		this.voucherShop = voucherShop;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public List<OrderDetail> getProducts() {
		return products;
	}
	public void setProducts(List<OrderDetail> products) {
		this.products = products;
	}
	private List<Order> orders;
	private List<OrderDetail> products;
}