//package com.API.controller.user;
//
//import com.API.model.Account;
//
////package com.API.controller.user;
////
////
//import java.io.UnsupportedEncodingException;
////import java.lang.reflect.Array;
////import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Enumeration;
//import java.util.HashMap;
////import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
////import java.util.stream.IntStream;
//
//import org.apache.commons.codec.digest.HmacUtils;
//import org.hibernate.boot.beanvalidation.IntegrationException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
////import org.springframework.context.annotation.Scope;
////import org.springframework.data.jpa.repository.Modifying;
////import org.springframework.data.jpa.repository.Query;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.HttpStatusCode;
////import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.API.DTO.order.OrderInsert;
//import com.API.globalException.CustomeException;
////import com.API.DTO.order.OrderInsertItem;
//import com.API.model.Account;
//import com.API.model.Cart;
//import com.API.model.Order;
//import com.API.model.OrderDetail;
//import com.API.model.Shop;
////import com.API.model.ThanhToan;
//import com.API.model.TrangThai;
//import com.API.model.VoucherSan;
//import com.API.model.VoucherShop;
//import com.API.repository.CartRepository;
//import com.API.repository.DiaChiRepository;
//import com.API.repository.DonHangRepository;
//import com.API.repository.OrderDetailRepository;
//import com.API.repository.ProductDetailRepository;
//import com.API.repository.VoucherSanRepository;
//import com.API.repository.VoucherShopRepository;
//import com.API.service.TestService;
//import com.API.utils.GeneratedUrlPayment;
//import com.API.utils.ObjectRespone;
//import com.API.utils.OrderHelper;
////import com.fasterxml.jackson.databind.ObjectReader;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.transaction.Transactional;
//import jakarta.validation.Valid;
//
//
//@RestController
//@CrossOrigin("*")
//public class OrderController {
////	
//	@Autowired
//	GeneratedUrlPayment urlPayment;
//
//	@Autowired
//	CartRepository cart;
//	
//	@Autowired
//	HttpServletResponse respone;
//	
//	@Autowired
//	ProductDetailRepository p;
//	
//	@Autowired
//	OrderDetailRepository orderDetailRepository;
//	
//	@Autowired
//	VoucherSanRepository voucherSanRepository;
//	
//	@Autowired
//	DonHangRepository orderRepository;
//
//	@Autowired
//	HttpServletRequest request;
//
//	@Autowired
//	DiaChiRepository diaChiRepository;
//
//	@Autowired
//	OrderHelper orderHelper;
//	
//	@Autowired
//	ProductDetailRepository productDetailRepository;
//
//	List<Cart> c = new ArrayList<Cart>();
//
//	@Autowired
//	TestService t;
//	@Autowired
//	VoucherShopRepository voucherShopRepository;
//
//
//	@PostMapping("/user/auth/order/addorder")
//	@Transactional
//	public Object addOrder(@RequestBody @Valid OrderInsert o) throws UnsupportedEncodingException {
//		orderHelper.o = o;
//		Account a = getAccount();
//		orderHelper.account=a;
//		VoucherSan v1 =null;
//		if (diaChiRepository.getsDiaChiId(a.getId(), o.getDiaChi().getId()) != null) {
//			orderHelper.setOrderDetails(cart.getOrderDetailInlist(a.getId(),o.getItems()));
//			if (o.getVoucherSan() != null) {
//				v1 = voucherSanRepository.getVoucherSanByAccountIdAndVoucherId(a.getId(), o.getVoucherSan().getId()).orElse(null);
//				if(v1==null || calculateTotalPrice()<v1.getDonToiThieu()) {
//					return new ObjectRespone(400,"Không thể áp dụng voucher sàn này !!!", null);
//				}
//			}
//			if(orderHelper.getOrderDetails().size()<o.getItems().size()) {
//				return new ObjectRespone(200, "Danh sách item không hợp lệ !!!", null);
//			}
//			Map<Integer, List<OrderDetail>> listGroup = orderHelper.getOrderDetails().stream().collect(Collectors.groupingBy(OrderDetail::getShopId));
//			if(listGroup.size()<o.getOrderShop().size()) {
//				return new ObjectRespone(200, "Shop not match items !!!", null);
//			}
//			listGroup.forEach((k, v) -> {
//				asyncHandleOrder(k,v,o.getOrderShop().get(k));
//			});
//			orderRepository.saveAll(orderHelper.getOrder());
//			if(v1!=null) {
//				v1.calFee(orderHelper.getOrder());
////				voucherSanRepository.updateSoLuotDung(v1.getId(),a.getId());
//			}
////			cart.deleteAllByIdInBatch(o.getItems());
//			orderDetailRepository.saveAll(orderHelper.getOrderDetails());
//			if(o.getThanhToan().getId()==1) {
//				return new ObjectRespone(200, "Create order successfully !!!",orderHelper.getOrder());
//			}
//			return new ObjectRespone(200, "Create order successfully !!!",urlPayment.generatedUrlPayment(120000, 20000.0, "http://localhost:8080/user/auth/order/successpayment"));
//		}
//		return new ObjectRespone(400, "Địa chỉ không hợp lệ !!!",null);
//	}
//	
//	public double calculateTotalPrice() {
//		return 10000;
////		return orderHelper.getOrderDetails().stream()
////		        .mapToDouble(v -> v.getSoLuong() * v.getGiaBan())
////		        .sum();
//	}
//	
//	
//	@Transactional
//	@GetMapping("/user/auth/order/successpayment")
//	 public ObjectRespone processVNPayReturn(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Map<String, String> fields = new HashMap<>();
//        Enumeration<String> params = request.getParameterNames();
//        while (params.hasMoreElements()) {
//            String fieldName = params.nextElement();
//            String fieldValue = request.getParameter(fieldName);
//            if (fieldValue != null && fieldValue.length() > 0) {
//                fields.put(fieldName, fieldValue);
//            }
//        }
//        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//        fields.remove("vnp_SecureHashType");
//        fields.remove("vnp_SecureHash");
//        String signValue = hashAllFields(fields);
//        if (signValue.equals(vnp_SecureHash)) {
//            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
//            	String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
//            	int[] orderIds = Arrays.stream(vnp_OrderInfo.split("-"))
//                        .mapToInt(Integer::parseInt)
//                        .toArray(); 
//            	if(orderIds.length==orderRepository.countByIdFinishPayment(orderIds,getAccount().getId())) {
//            		orderRepository.updateTrangThaiOrder(Arrays.stream(orderIds).boxed().collect(Collectors.toList()));
//                    return new ObjectRespone(200, "Thanh toán đơn hàng thành công", orderIds);
//            	}else{
//            		return new ObjectRespone(200, "Thông tin đơn hàng không chính xác !!!", orderIds);
//            	}
//            } else {
//            	return new ObjectRespone(400, "Thanh toán đơn hàng thất bại", null);
//            }
//        } else {
//        	return new ObjectRespone(403, "Thông tin thanh toán không hợp lệ", null);
//        }
//    }
//	
//	public String hashAllFields(Map<String, String> fields) {
//        StringBuilder hashData = new StringBuilder();
//        fields.entrySet().stream()
//            .sorted(Map.Entry.comparingByKey())
//            .forEach(entry -> hashData.append(entry.getKey()).append("=").append(entry.getValue()).append("&"));
//        if (hashData.length() > 0) {
//            hashData.setLength(hashData.length() - 1);
//        }
//        String vnp_HashSecret = "UE5AQGNZSQFJD9VOXA4HAIFMYH4FZ1J0";
//        return HmacUtils.hmacSha512Hex(vnp_HashSecret, hashData.toString());
//    }
//	
//	@PostMapping("/user/auth/order/cancel/{id}/{message}")
//	@Transactional
//	public ObjectRespone cancelOrder(@PathVariable("id")Integer id,@PathVariable("message")String message) {
//		Order o=orderRepository.getTrangThaiDonHangById(id,getAccount().getId()).orElse(null);
//		if(o!=null) {
//			if(o.getTrangThai().getId()<2) {
//				orderRepository.updateVoucherUsage(o.getVoucherShopId().getId(), o.getVoucherSanId().getId());
//				orderRepository.updateOrderStatus(id,new TrangThai(5),message); 
//				orderRepository.deleteItemOrder(id);
//				return new ObjectRespone(200,"Hủy đơn hàng thành công!", null);
//			}
//			return new ObjectRespone(400,"yêu cầu hủy không thành công !", null);
//		}
//		return new ObjectRespone(403,"Đơn hàng không phải của bạn !", null);
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//   //hoàn thành đơn.
////	@PostMapping("/user/auth/order/finish/{id}")
////	@Transactional
////	public ObjectRespone finishShiping(@PathVariable("ids")String id) {
////////		if(orderRepository.getTrangThaiDonHangById(id,getAccount().getId()).orElse(-1)==3) { // ngưỡng để submit đơn hàng 
////////			orderRepository.updateOrderStatus(id,new TrangThai(4)); // trạng tthais 4 la da giao thanh cong
////////			// gửi thông báo đến người dùng.
////////			return new  ObjectRespone(400,"Nhận đơn hàng thành công !", null);
////////		}
//////		return new ObjectRespone(400,"Đơn hàng cập nhật không thành công !", orderRepository.getTrangThaiDonHangById(id,getAccount().getId()).orElse(-1));
////	}
//	
//	// copy order 
//	@PostMapping("/user/auth/order/copyorder/{id}")
//	@Transactional
//	public ObjectRespone copyOrder(@PathVariable("id")Integer id) {
//		List<Cart> c=cart.getCartCopyOrderDetail(id, getAccount().getId()).orElse(null);
//		if(c.size()!=0) {
//			cart.saveAllAndFlush(c);
//			return new ObjectRespone(200,"success",null);
//		}
//		return new ObjectRespone(403,"Có lỗi xảy ra !",null);
//	}
//	
////	 Lấy danh sách đơn hàng 
//	@GetMapping("/user/auth/order/getorder")
//	public ObjectRespone getOrder() {
//			List<OrderDetail>l= orderRepository.getOrder(getAccount().getId());
//			Map<Integer, List<OrderDetail>> groupedByOrder = l.stream()
//				    .collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
//			return new ObjectRespone(200,"success",groupedByOrder);
//	}
//	
////	@GetMapping("/user/auth/order/getorderbystatus")
////	public ObjectRespone getOrder() {
//////		if(key!=null) {
////			List<OrderDetail>l= orderRepository.getOrder();
////			Map<Integer, List<OrderDetail>> groupedByOrder = l.stream()
////				    .collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
////			return new ObjectRespone(200,"success",groupedByOrder);
//////		}
//////		return new ObjectRespone(200,"success",orderRepository.getOrder());
////	}
//	
//	@GetMapping("/user/auth/order/getall")
//	public Object getAllOrder() {
//		return orderRepository.getOrderById();
////		return orderRepository.getOrd
//	}
//	
//	@GetMapping("/user/auth/order/getorderbystatus")
//	public ObjectRespone getOrderByTrangThai(@RequestParam(name = "status",defaultValue = "1")Integer trangThai) {
//		List<OrderDetail>l= orderRepository.getOrder(trangThai,getAccount().getId());
//		Map<Integer, List<OrderDetail>> groupedByOrder = l.stream()
//			    .collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
//		return new ObjectRespone(200,"success",groupedByOrder);
//	}
//	
//	
//	
//	// lấy chi tiết đơn hàng 
//	@GetMapping("/user/auth/order/getorderdetail/{id}")
//	public ObjectRespone getOrderDetailById(@PathVariable("id")Integer id) {
//		Order o= orderRepository.getOrderById(id).orElse(null);
//		if(o!=null) {
//			o.setOrderDetails(orderRepository.getItemInOrder(id).get());
//			return new ObjectRespone(200,"success",o);
//		}
//		return new ObjectRespone(200,"Không tìm thấy đơn hàng !!!",null); 
//	}
//	
//	
//	
//	
//	
//	
//	public void asyncHandleOrder(Integer shopId,List<OrderDetail> o,Order order) {
//		if(order!=null) {
//			if(order.getVoucherShopId()==null) {
//				handleOrder(order,o);
//			}else { 
//				VoucherShop vs = cart.getVoucherByIdOfOrder(order.getVoucherShopId().getId(),shopId,getAccount().getId()).orElse(null);
//				if (vs == null || vs.getA().size() <1) {
//					throw new CustomeException("Voucher shop không hợp lệ !!!");
//				}else {
//					voucherShopRepository.updateCountUseVoucher(order.getVoucherShopId().getId(),orderHelper.account.getId());
//					handleOrder(order,o, vs);
//				}
//			}
//			o.forEach(v->{
//				productDetailRepository.updateSoLuongProduct(v.getProduct().getId(), v.getSoLuong());
//			});
//			order.setBasicInfo(orderHelper.o.getDiaChi(), orderHelper.o.getThanhToan(), orderHelper.o.getVoucherSan(),orderHelper.account, new Shop(shopId));
//		}else {
//			throw new CustomeException("Shop không hợp lệ");
//		}
//	}
//	
//	public void handleOrder(Order o, List<OrderDetail> os) {
//		os.forEach(v -> {
//			v.setOrder(o);
//			o.plusTotalOrder(v.getSoLuong() * v.getGiaBan());
//		});
//		orderHelper.addOrderItem(o);
//	}
//	
//	
//	@GetMapping("/getpayment")
//	public Object getMethodName(@RequestParam("orderIds")String orderId) throws UnsupportedEncodingException {
//		// tính toán ở đây
//		
//		return new ObjectRespone(200,"success",urlPayment.generatedUrlPayment(120000, 20000.0, "https://drive.google.com/drive/u/0/folders/1YQu9k9RTxcw1dIgOPebk0o1tLOtyXPXv"));
////		return urlPayment.generatedUrlPayment(120000, 20000.0, "");
//	}
//	
//	
//	
//
//	public void handleOrder(Order o, List<OrderDetail> os, VoucherShop vs) {
//		float tien = 0;
//		if (vs.getLoaiVoucher() == 0) {
//			os.forEach(v -> {
//				v.setOrder(o);
//				o.plusTotalOrder(v.getSoLuong() * v.getGiaBan());
//				if (vs.inList(String.valueOf(v.getProductId()))) {
//					
//					o.tienTru +=vs.getGiaTriGiam();
//					o.tienTinhDuoc+=v.getGiaBan();
//				}
//			});
//		} else {
//			os.forEach(v -> {
//				v.setOrder(o);
//				o.plusTotalOrder(v.getSoLuong() * v.getGiaBan());
//				if (vs.inList(String.valueOf(v.getProductId()))) {
//					o.tienTru += (v.getGiaBan() * v.getSoLuong()) * (vs.getGiaTriGiam()/100);
//					o.tienTinhDuoc+=v.getGiaBan();
//				}
//			});
//		}
//		if(o.tienTinhDuoc<vs.getDonToiThieu()) {
//			throw new CustomeException("Voucher "+vs.getMaVoucher()+" không thể áp dụng được");
//		}
//		tien = vs.getTienGiamToiDa() < o.tienTru ? vs.getTienGiamToiDa() : o.tienTru;
//		orderHelper.addOrderItemDetail(os);
//		orderHelper.addOrderItem(o);
//	}
//	
//	
//	
//	public Account getAccount() {
//		return new Account(1);
//	}
////	
////	
////
//}
