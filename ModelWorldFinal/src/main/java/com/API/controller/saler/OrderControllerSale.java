package com.API.controller.saler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.model.Account;
import com.API.model.BaoCao;
import com.API.model.ChiTietThongBao;
import com.API.model.Order;
import com.API.model.OrderDetail;
import com.API.model.Shop;
import com.API.model.ThongBao;
import com.API.model.TrangThai;
import com.API.model.TypeAnnounce;
import com.API.repository.BaoCaoRepository;
import com.API.repository.DonHangRepository;
import com.API.repository.ViPhamRepository;
import com.API.service.PDFService;
import com.API.service.OrderService.SendAnnounceService;
import com.API.service.saler.ShopFilter;
import com.API.service.voucherService.VoucherSendAnnounceService;
import com.API.utils.ObjectRespone;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin("*")
public class OrderControllerSale {
	@Autowired
	ShopFilter shopFilter;

	@Autowired
	PDFService pdfService;

	@Autowired
	ViPhamRepository viPhamrepository;
	@Autowired
	DonHangRepository orderRepository;
	@Autowired
	BaoCaoRepository baoCaoRepository;
	
	@Autowired
	private VoucherSendAnnounceService send;
	// =====================  THÊM MỚI.  =============================
	
	
	@PostMapping("/sale/order/xacnhan")
	public Object postMethodNames(@RequestParam(name = "id",defaultValue = "-1")Integer id) {
		Date a=orderRepository.getOrderDateByIdAndShopIdAndStatus(id, shopFilter.getShopId());
		if(a!=null) {
			LocalDate localDate = a.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        LocalDate now = LocalDate.now();
	        long daysBetween = ChronoUnit.DAYS.between(localDate, now);
	        if(daysBetween >= 7) {
	        	orderRepository.updateStatusSingle(id, 6);
	    		return new ObjectRespone(200, "Đơn hàng đã chuyển sang trạng thái giao thành công",null);
	        }
			return new ObjectRespone(400, "Đơn hàng chưa đến hạn xác nhận",null);			
		}
		return new ObjectRespone(400, "Không tìm thấy đơn hàng của bạn",null);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ==================================================
	
	@PostMapping("/sale/order/nextorder")
	@Transactional
	public ObjectRespone updateNextOrderList(@RequestBody List<Integer> ids, @RequestParam(name="status",defaultValue = "1") Integer status) {
		if(status+1<6&&status>2) {
			if(orderRepository.countOrderByStatusAndShopId(ids,shopFilter.getShopId(),status)==ids.size()) {
				orderRepository.updateNextStatusInOrderIds(ids, status+1);
				return new ObjectRespone(200,"Cập nhật đơn hàng thành công.", null);
			}else {
				new ObjectRespone(400,"Danh sách đơn hàng cập nhật không hợp lệ", null);
			}
		}
		return new ObjectRespone(403, "Trạng thái yêu cầu cập nhật không hợp lệ !", null);
	}
	
	@PostMapping("/sale/order/nextordersingle")
	@Transactional
	public ObjectRespone updateNextOrderList(@RequestParam(name = "orderId", defaultValue = "-1") Integer orderId) {
	    Integer a = orderRepository.getOrderIdByIdAndShopIdAndStatus(orderId, shopFilter.getShopId());
	    if (a != -1) {
	        if (a + 1 < 8 && a >= 2 && a != 5) {
	            orderRepository.updateNextStatusInOrderId(orderId, a + 1);
	            send.senThongBaoOrder(orderId,"Cập nhật trạng thái đơn hàng mới nhất !!!",a);
	            return new ObjectRespone(200, "Cập nhật đơn hàng thành công.", null);
	        }
	        return new ObjectRespone(403, "Trạng thái yêu cầu cập nhật không hợp lệ!", null);
	    }
	    return new ObjectRespone(400, "Đơn hàng cập nhật không hợp lệ", null); 
	}

	
	
	@PostMapping("/sale/order/report")
	public ObjectRespone reportOrder(@RequestBody BaoCao reportOrder) {
		if(viPhamrepository.findById(reportOrder.getViPham().getId()).orElse(null)!=null) {
			reportOrder.setAccount(new Account(shopFilter.getAccountId()));
			baoCaoRepository.save(reportOrder);
			return new ObjectRespone(200, "Report thành công đơn hàng",null);
		}
		return new ObjectRespone(400, "Mã vi phạm không hợp lệ",null);
	}
	
	
	
	
	// ==================================================
	
	
	
	
	@GetMapping("/sale/order/getallchuanbihang")
	public Object getAllOrder(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "sortBy", defaultValue = "1") Integer sortBy,
			@RequestParam(name = "key", defaultValue = "") String key) {
		Sort sort = null;
		if (sortBy == 1) {
			sort = Sort.by("id").descending();
		} else {
			sort = Sort.by("ngayTaoDon").descending();
		}
		Page<Order> orders = orderRepository.getAllOrderSaler(shopFilter.getShopId(), PageRequest.of(page, 10, sort),
				"%" + key + "%");
		List<OrderDetail> l = orderRepository.getItemInOrder(orders.getContent()).get();
		Map<Integer, List<OrderDetail>> groupedOrderDetails = l.stream()
				.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
		orders.forEach(v -> v.setOrderDetails(groupedOrderDetails.get(v.getId())));
		return orders;
	}

	@GetMapping("/sale/order/getcountorderbystatus")
	public ObjectRespone getCountOrderByStatus() {
		
		return new ObjectRespone(200, null, orderRepository.getCountOrderByStatus(shopFilter.getShopId()).stream()
				.filter(array -> array.length >= 2).collect(Collectors.toMap(array -> array[0], array -> array[1])));
	}

	
	
	@PostMapping("/sale/order/submitorder")
	public ObjectRespone postMethodName(@RequestParam("id")Integer id) {
		Date date=orderRepository.getDateOrderById(id,shopFilter.getShopId()).orElse(null);
		if(date!=null) {
			LocalDateTime dateTimeFromDb = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
			LocalDateTime currentDateTime = LocalDateTime.now();
			long hoursDifference = ChronoUnit.HOURS.between(dateTimeFromDb, currentDateTime);
			if (hoursDifference >= 2) {
				orderRepository.updateNhanHangByOrderId(id);
				return new ObjectRespone(200,"Đơn hàng đã chuyển sang nhận hàng.",null);	
			}else {
				return new ObjectRespone(400,"Đơn hạn chưa đến hạn submit.",null);				
			}
		}
		return new ObjectRespone(400,"Không tìm thấy đơn hàng.",null);
	}
	

	@PostMapping("/sale/order/chuanbidon")
	public Object chuanBiDon(@RequestParam(value = "xuat", defaultValue = "false") Boolean xuat,
			@RequestBody List<Integer> ids) {
		if (ids.size() > 0) {
			Integer count = orderRepository.updateChuanBiHang(shopFilter.getShopId(), ids, new TrangThai(3));
			if (count != ids.size()) {
				return new ObjectRespone(400, "Trạng thái đơn hàng không hợp lệ", null);
			}
			return new ObjectRespone(200, "Đã cập nhật thành công đơn hàng ", null);
		} else {
			return new ObjectRespone(400, "Danh sách cập nhật không được rỗng", null);
		}
	}


	@GetMapping("/sale/order/chothanhtoan")
	public Object chuanBiDon(@RequestParam(value = "soGio", defaultValue = "1") Integer soGio,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "key", defaultValue = "") String key) {
		Page<Order> orders = orderRepository.getAllOrderSalerByCHoThanhToan(shopFilter.getShopId(), PageRequest.of(page, 10),
				"%" + key + "%", LocalDateTime.now().minusHours(soGio));
		List<OrderDetail> l = orderRepository.getItemInOrder(orders.getContent()).get();
		Map<Integer, List<OrderDetail>> groupedOrderDetails = l.stream()
				.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
		orders.forEach(v -> v.setOrderDetails(groupedOrderDetails.get(v.getId())));
		return orders;
	}

	@PostMapping("/sale/order/cancelsingle/{id}/{message}")
	@Transactional
	public ObjectRespone cancelOrder(@PathVariable("id") Integer id, @PathVariable("message") String message) {

		Order o = orderRepository.getTrangThaiDonHangByIdTwo(id, shopFilter.getShopId()).orElse(null);
		if (o != null) {
			if (o.getTrangThai().getId() ==2) {
				if(o.getVoucherShopId()!=null) {
					orderRepository.updateVoucherUsage(o.getVoucherShopId().getId(), o.getVoucherSanId().getId());
				}
				orderRepository.updateOrderStatus(id, new TrangThai(8), message);
				orderRepository.updateSoLuongSanPham(id);
	            send.senThongBaoOrderCancel(o.getShop().getHinhAnh(),id,"Đơn hàng bị hủy do "+message,"❌ ❌ ❌ Đơn hàng "+id+" của bạn đã bị hủy,"
	            		+ " xem ngay để biết thêm chi tiết "+(o.getThanhToanId().getId()==2?"Vui lòng liên hệ shop "+o.getShop().getEmail()+"-"+o.getShop().getAccount().getSoDienThoai()+" để thanh toán lại đơn hàng, cám ơn":"")+"");
				return new ObjectRespone(200, "Hủy đơn hàng thành công!", null);
			}
			return new ObjectRespone(400, "Trạng thái đơn hàng không được phép hủy!", null);
		}
		return new ObjectRespone(403, "Đơn hàng không phải của bạn !", null);
	}

	@PostMapping("/sale/order/cancellist/{message}")
	@Transactional
	public ObjectRespone cancelOrderInList(@RequestBody List<Integer> ids, @PathVariable("message") String message) {
		List<Order> l = orderRepository.getAllOrderSalerInListTwo(shopFilter.getShopId(), ids);
		if (l.size() == ids.size()) {
			orderRepository.cancelOrderInList(ids, "Quá hạn thanh toán đơn hàng");
			l.forEach(v -> {
				orderRepository.updateVoucherUsage(v.getVoucherShopId().getId(), v.getVoucherSanId().getId());
				orderRepository.updateSoLuongSanPham(v.getId());

			});
			return new ObjectRespone(200, "Hủy đơn hàng thành công!", null);
		}
		return new ObjectRespone(403, "Danh sách đơn hàng không hợp lệ !", null);
	}

	@PostMapping("/sale/order/chuanbidon/pdf")
	public ResponseEntity<byte[]> chuanBiDon(
			@RequestParam(name = "message", defaultValue = "Không có ghi chú") String message,
			@RequestBody List<Integer> ids) throws IOException {
		if (ids.size() > 0) {
			List<Order> l = orderRepository.getAllOrderSalerInList(shopFilter.getShopId(), ids);
			l.forEach(v -> {
				v.setTrangThai(new TrangThai(3));
				v.setShop(new Shop(shopFilter.getShopId()));
			});
			List<OrderDetail> ll = orderRepository.getItemInOrder(l).get();
			Map<Integer, List<OrderDetail>> groupedOrderDetails = ll.stream()
					.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
			l.forEach(v -> v.setOrderDetails(groupedOrderDetails.get(v.getId())));
			orderRepository.saveAll(l);
			byte[] pdfBytes = pdfService.createPdf(message, l);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "generated.pdf");
			headers.setContentLength(pdfBytes.length);
			return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/sale/order/getAllOrderNormal")
	public Object getAllOrderNormal(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "sortBy", defaultValue = "1") Integer sortBy,
			@RequestParam(name = "key", defaultValue = "") String key,
			@RequestParam(name = "trangThaiId", defaultValue = "") Integer trangThaiId
			
			) {
		Sort sort = null;
		if (sortBy == 1) {
			sort = Sort.by("id").descending();
		} else {
			sort = Sort.by("ngayTaoDon").descending();
		}
		Page<Order> orders = orderRepository.getAllOrderSaler(shopFilter.getShopId(), PageRequest.of(page, 10, sort),
				"%" + key + "%",trangThaiId);
		List<OrderDetail> l = orderRepository.getItemInOrder(orders.getContent()).get();
		Map<Integer, List<OrderDetail>> groupedOrderDetails = l.stream()
				.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
		orders.forEach(v -> v.setOrderDetails(groupedOrderDetails.get(v.getId())));
		return orders;
	}
	
	@GetMapping("/sale/order/getAllOrderNormal/donhuy")
	public Object getAllOrderNormalHuyDon(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "sortBy", defaultValue = "1") Integer sortBy,
			@RequestParam(name = "key", defaultValue = "") String key,
			@RequestParam(name = "trangThaiId", defaultValue = "") Integer trangThaiId
			
			) {
		Sort sort = null;
		if (sortBy == 1) {
			sort = Sort.by("id").descending();
		} else {
			sort = Sort.by("ngayTaoDon").descending();
		}
		Page<Order> orders = orderRepository.getAllOrderSaler(shopFilter.getShopId(), PageRequest.of(page, 10, sort),
				"%" + key + "%",8);
		List<OrderDetail> l = orderRepository.getItemInOrder(orders.getContent()).get();
		Map<Integer, List<OrderDetail>> groupedOrderDetails = l.stream()
				.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
		orders.forEach(v -> v.setOrderDetails(groupedOrderDetails.get(v.getId())));
		return orders;
	}
	
	@GetMapping("/sale/order/getorderdetail/{id}")
	public ObjectRespone getOrderDetailById(@PathVariable("id")Integer id) {
		Order o= orderRepository.getOrderById(id,shopFilter.getShopId()).orElse(null);
		if(o!=null) {
			o.setOrderDetails(orderRepository.getItemInOrder(id).get());
			return new ObjectRespone(200,"success",o);
		}
		return new ObjectRespone(200,"Không tìm thấy đơn hàng !!!",null); 
	}
	
	
//	@GetMapping("/sale/order/getorderdetail/{id}")
//	public ObjectRespone getOrderDetailById(@PathVariable("id")Integer id) {
//		Order o= orderRepository.getOrderById(id,shopFilter.getShopId()).orElse(null);
//		if(o!=null) {
//			o.setOrderDetails(orderRepository.getItemInOrder(id).get());
//			return new ObjectRespone(200,"success",o);
//		}
//		return new ObjectRespone(200,"Không tìm thấy đơn hàng !!!",null); 
//	}
		
	
	
//	@GetMapping("/sale/order/get")
//	public Object getAllDuLieu() {
//		return orderRepository.findAll();
//	}

//	public int getShopId() {
//		return 1;
//	}
}
