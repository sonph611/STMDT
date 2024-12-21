package com.API.controller.orderController;

import java.io.ObjectInputFilter.Config;
import java.io.UnsupportedEncodingException;
import java.lang.foreign.Linker.Option;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.DTO.order.OrderInsert;
import com.API.filter.UserFilter;
import com.API.globalException.CustomeException;
import com.API.model.Account;
import com.API.model.Cart;
import com.API.model.ChiTietThongBao;
import com.API.model.GiaoDich;
import com.API.model.Order;
import com.API.model.OrderDetail;
import com.API.model.Shop;
import com.API.model.ThongBao;
import com.API.model.TrangThai;
import com.API.model.VoucherSan;
import com.API.model.VoucherShop;
import com.API.repository.CartRepository;
import com.API.repository.ChiTietThongBaoRepository;
import com.API.repository.DiaChiRepository;
import com.API.repository.DonHangRepository;
import com.API.repository.GiaoDichRepository;
import com.API.repository.OrderDetailRepository;
import com.API.repository.ProductDetailRepository;
import com.API.repository.ThongBaoRepository;
import com.API.repository.VoucherSanRepository;
import com.API.repository.VoucherShopRepository;
import com.API.service.TestService;
import com.API.service.OrderService.SendAnnounceService;
import com.API.service.saler.ShopFilter;
import com.API.utils.GenerateToken;
import com.API.utils.GeneratedUrlPayment;
import com.API.utils.ObjectRespone;
import com.API.utils.OrderHelper;

import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
public class OrderController {
	
	@Autowired
	GeneratedUrlPayment urlPayment;
	
	@Autowired
	private GenerateToken token;
	
	@Autowired
	ShopFilter shopFilter;

	@Autowired
	CartRepository cart;
	
	@Autowired
	HttpServletResponse respone;
	
	@Autowired
	ProductDetailRepository p;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	VoucherSanRepository voucherSanRepository;
	
	@Autowired
	DonHangRepository orderRepository;

	@Autowired
	HttpServletRequest request;

	@Autowired
	DiaChiRepository diaChiRepository;

	@Autowired
	OrderHelper orderHelper;
	
	@Autowired
	ProductDetailRepository productDetailRepository;
	
	@Autowired
	GiaoDichRepository giaoDichRepository;
	
	@Autowired 
	ThongBaoRepository thongBaoRepository;
	
	@Autowired
	ChiTietThongBaoRepository chiTietThongBaoRepository;

	
	
	@Autowired
	com.API.service.saler.UserFilter userFilter;
	
	List<Cart> c = new ArrayList<Cart>();

	@Autowired
	TestService t;
	@Autowired
	VoucherShopRepository voucherShopRepository;
	
	@GetMapping("/user/auth/order/getorderdetail/{id}")
	public ObjectRespone getOrderDetailById(@PathVariable("id")Integer id) {
		Order o= orderRepository.getOrderByAccountId(id,userFilter.getAccount().getId()).orElse(null);
		System.out.println(o.getTienTru());
		if(o!=null) {
			o.setOrderDetails(orderRepository.getItemInOrder(id).get());
			return new ObjectRespone(200,"success",o);
		}
		return new ObjectRespone(200,"Không tìm thấy đơn hàng !!!",null); 
	}
	

	@PostMapping("/user/auth/order/cancelsingle/{id}/{message}")
	@Transactional
	public ObjectRespone cancelOrder(@PathVariable("id") Integer id, @PathVariable("message") String message) {
		Order o = orderRepository.getTrangThaiDonHangByIdTwoAccountId(id, userFilter.getAccount().getId()).orElse(null);
		if (o != null) {
			if (o.getTrangThai().getId() <= 2) {
				orderRepository.updateVoucherUsage(o.getVoucherShopId().getId(), o.getVoucherSanId().getId());
				orderRepository.updateOrderStatusUser(id, new TrangThai(8), message);
				orderRepository.updateSoLuongSanPham(id);
				return new ObjectRespone(200, "Hủy đơn thành công", null);
			}
			return new ObjectRespone(400, "yêu cầu hủy không thành công !", null);
		}
		return new ObjectRespone(403, "Đơn hàng không phải của bạn !", null);
	}
	
	
	// lấy danh sách order 
	@GetMapping("/user/auth/order/getAllOrderNormal")
	public Object getAllOrderNormal(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "sortBy", defaultValue = "1") Integer sortBy,
			@RequestParam(name = "key", defaultValue = "") String key,
			@RequestParam(name = "trangThaiId", defaultValue = "1") Integer trangThaiId
			
			) {
		Sort sort = null;
		if (sortBy == 1) {
			sort = Sort.by("id").descending();
		} else {
			sort = Sort.by("ngayTaoDon").descending();
		}
		Page<Order> orders=null;
		if(trangThaiId!=1) {
			
			orders= orderRepository.getAllOrderUser(userFilter.getAccount().getId(), PageRequest.of(page, 10, sort),
					"%" + key + "%",trangThaiId);
		}else {
			
			orders=orderRepository.getAllOrderUser(userFilter.getAccount().getId(), PageRequest.of(page, 10, sort),
						"%" + key + "%");
		}
		List<OrderDetail> l = orderRepository.getItemInOrder(orders.getContent()).get();
		Map<Integer, List<OrderDetail>> groupedOrderDetails = l.stream()
				.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
		orders.forEach(v -> v.setOrderDetails(groupedOrderDetails.get(v.getId())));
		return orders;
	}
	
	
	@GetMapping("/user/auth/order/geturlpayment")
	public ObjectRespone getMethodName(@RequestParam(name="orderId",defaultValue = "-1") Integer orderId) throws UnsupportedEncodingException {
		Order order=orderRepository.getOrderCanPayment(userFilter.getAccount().getId(),orderId, LocalDateTime.now().minusHours(2)).orElse(null);
		if(order!=null) {
			double t=order.getTongTien()+order.getPhiShip()-order.getTienTru()-order.getTienTruVoucherSan();
			return new ObjectRespone(200,"success",
					urlPayment.generatedUrlPayment(t, 0, "http://localhost:8080/user/paymentfinish",token.generateToken(orderId+" ")));
		}else {
			return new ObjectRespone(401,"Không tìm thấy đơn hàng, vui lòng kiểm tra lại thời gian và đơn hàng của bạn",null);
		}
	}
	
	
	
	
	
	@PostMapping("/user/auth/order/addorder") // thêm ràng buộc nữa l xong nha bạn ơi
	@Transactional
	public Object addOrder(@RequestBody @Valid OrderInsert o) throws UnsupportedEncodingException {
		orderHelper.o = o;
		Account a = getAccount();
		orderHelper.account=a;
		VoucherSan v1 =null;
		if (diaChiRepository.getsDiaChiId(userFilter.getAccount().getId(), o.getDiaChi().getId()) != null) {
			orderHelper.setOrderDetails(cart.getOrderDetailInlist(userFilter.getAccount().getId(),o.getItems()));
			if (o.getVoucherSan() != null) {
				v1 = voucherSanRepository.getVoucherSanByAccountIdAndVoucherId(userFilter.getAccount().getId(), o.getVoucherSan().getId()).orElse(null);
				if(v1==null || calculateTotalPrice()<v1.getDonToiThieu()) {
					return new ObjectRespone(400,"Không thể áp dụng voucher sàn này !!!", null);
				}
			}
//			System.out.println(orderHelper.getOrderDetails().size()<o.getItems().size());
			if(orderHelper.getOrderDetails().size()<o.getItems().size()) {
				return new ObjectRespone(400, "Danh sách item không hợp lệ !!!", null);
			}
			Map<Integer, List<OrderDetail>> listGroup = orderHelper.getOrderDetails().stream().collect(Collectors.groupingBy(OrderDetail::getShopId));
			if(listGroup.size()<o.getOrderShop().size()) {
				return new ObjectRespone(400, "Shop not match items !!!", null);
			}
			listGroup.forEach((k, v) -> {
				asyncHandleOrder(k,v,o.getOrderShop().get(k));
			});
			orderRepository.saveAll(orderHelper.getOrder());
			if(v1!=null) {
				v1.calFee(orderHelper.getOrder());
				voucherSanRepository.updateSoLuotDaDung(v1.getId(),userFilter.getAccount().getId());
			}
			cart.deleteAllByIdInBatch(o.getItems());
			orderDetailRepository.saveAll(orderHelper.getOrderDetails());
			if(o.getThanhToan().getId()==1) {
				orderHelper.getOrder().forEach(v->v.setTrangThai(new TrangThai(2)));
				return new ObjectRespone(200, "Create order successfully !!!",orderHelper.getOrder());
			}
			String result = orderHelper.getOrder().stream().map(Order::getId).map(String::valueOf) .collect(Collectors.joining(" "));
			Double b=orderHelper.getOrder().stream()
            .mapToDouble(order ->  order.getTongTien() + order.getPhiShip() - order.getTienTru() -  order.getTienTruVoucherSan() ).sum();
			return new ObjectRespone(200, "Create order successfully !!!",urlPayment.generatedUrlPayment(b, 0, "http://localhost:8080/user/paymentfinish",token.generateToken(result)));
		}
		return new ObjectRespone(400, "Địa chỉ không hợp lệ !!!",null);
	}
	
	public void asyncHandleOrder(Integer shopId,List<OrderDetail> o,Order order) {
		order.setTongTien(0.0);
		order.setNgayTaoDon(new Date());
		if(order!=null) {
			o.forEach(v->{
				System.out.println("Phiên live là: "+v.getLiveId());
				System.out.println("Giá trị của số lượng là "+v.getSoLuong());
				if(v.getLiveId()!=null&&(v.getSoLuongGioHan()<0||v.getSoLuong()<=v.getSoLuongGioHan())) {
					v.setGiaBan(v.getGiaBan()*(1-Float.parseFloat((v.getGiaGiam()/100)+"")));
					System.out.println("Giá đã giảm bằng "+v.getGiaBan());
					System.out.println("Tổng kết lại là: "+v.getGiaBan()*v.getSoLuong());
//					System.out.println("Giá bạn hiện tại : "+v.getGiaBan());
//					System.out.println("Giá trị của LIVE: "+v.getGiaGiam());
//					System.out.println("Giá sau khi có LIVE: "+v.get);
				}
			});
			if(order.getVoucherShopId()==null) {
				handleOrder(order,o);
			}else { 
				VoucherShop vs = cart.getVoucherByIdOfOrder(order.getVoucherShopId().getId(),shopId,userFilter.getAccount().getId()).orElse(null);
				if (vs == null || vs.getA().size() <1) {
					throw new CustomeException("Voucher shop không hợp lệ !!!");
				}else {
					handleOrder(order,o, vs);
					voucherShopRepository.updateCountUseVoucher(order.getVoucherShopId().getId(),orderHelper.account.getId());
				}
			}
			o.forEach(v->{
				productDetailRepository.updateSoLuongProduct(v.getProduct().getId(), v.getSoLuong());
			});
			order.setBasicInfo(orderHelper.o.getDiaChi(), orderHelper.o.getThanhToan(), orderHelper.o.getVoucherSan(),userFilter.getAccount(), new Shop(shopId));
		}else {
			throw new CustomeException("Shop không hợp lệ");
		}
	}
	
	
	@GetMapping("/user/paymentfinish")
	@Transactional
	public Object vnpayReturn(@RequestParam Map<String, String> params) {
        String orderInfo = params.get("vnp_OrderInfo");
        String transactionId = params.get("vnp_TxnRef"); 
        String responseCode = params.get("vnp_ResponseCode"); 
        String transactionPayment=params.get("vnp_TransactionNo"); 
        try {
        	if ("00".equals(responseCode)) {
            	if(token.validateToken(orderInfo)) {
            		List<Integer> integerList = java.util.Arrays.stream(token.getOrderInfoFromToken(orderInfo).split(" "))
            	            .map(Integer::parseInt) 
            	            .collect(Collectors.toList());
            		if(orderRepository.updatePaymentOrder(integerList)==integerList.size()) {
            			List<GiaoDich> p=new ArrayList<GiaoDich>();
            			integerList.forEach(v->{
            				p.add(new GiaoDich(v,0.0, transactionPayment));
            			});
            			giaoDichRepository.saveAll(p);
//            			new SendAnnounceService().senThongBaoVoucher(shopFilter.getAccountId());
                        respone.sendRedirect("http://localhost:3000/order/paymentsuccess");
            		}
                    return "Thông tin đơn hàng thanh toán không hợp lệ" ;
            	}else {
            		return "Thanh toán thất bại";
            	}
            } else {
                return "Thanh toán không thành công: " + responseCode;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return "Thanh toán không thành côngx: ";
		}
    }

	
	
	public double calculateTotalPrice() {
		return  orderHelper.getOrderDetails().stream()
                .mapToDouble(orderDetail ->{
                	return  orderDetail.getGiaDaGiam() * orderDetail.getSoLuong();
                })
                .sum();
	}
	
	// copy order 
	@PostMapping("/user/auth/order/copyorder/{id}")
	@Transactional
	public ObjectRespone copyOrder(@PathVariable("id")Integer id) {
		List<Cart> c=cart.getCartCopyOrderDetail(id,userFilter.getAccount().getId()).orElse(null);
		if(c.size()!=0) {
			cart.saveAllAndFlush(c);
			return new ObjectRespone(200,"success",null);
		}
		return new ObjectRespone(403,"Có lỗi xảy ra !",null);
	}
	
	@PostMapping("/user/auth/order/confirm/{id}")
	@Transactional
	public ObjectRespone confirmOrder(@PathVariable("id")Integer id) {
		Integer count=orderRepository.confirmOrder(id,userFilter.getAccount().getId());
		if(count>0) {
			return new ObjectRespone(200,"Đã xác nhận đơn hàng...",null);
		}
		return new ObjectRespone(403,"Yêu cầu xác nhận không thành công...",null);
	}
	
	@PostMapping("/user/auth/order/notreciveorder/{id}")
	@Transactional
	public ObjectRespone confirmOrderNotRecive(@PathVariable("id")Integer id) {
		Integer count=orderRepository.UpdateNoteReaciveOrder(id,userFilter.getAccount().getId());
		if(count>0) {
			ThongBao thongBao=new ThongBao("Đơn hàng có vấn đề","Đơn hàng :"+id+" không nhận được hàng, xử lý ngay bây giờ ","", "","ORDER",userFilter.getAccount());
			ChiTietThongBao ct =new ChiTietThongBao(thongBao,new Account(orderRepository.getAccountByOrderId(id)),1);
			thongBaoRepository.save(thongBao);
			chiTietThongBaoRepository.save(ct);
			return new ObjectRespone(200,"Đã xác nhận đơn hàng...",null);
		}
		return new ObjectRespone(403,"Yêu cầu xác nhận không thành công...",null);
	}
	

	@GetMapping("/user/auth/order/getorder")
	public ObjectRespone getOrder() {
			List<OrderDetail>l= orderRepository.getOrder(getAccount().getId());
			Map<Integer, List<OrderDetail>> groupedByOrder = l.stream()
				    .collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
			return new ObjectRespone(200,"success",groupedByOrder);
	}
	

	
	@GetMapping("/user/auth/order/getall")
	public Object getAllOrder() {
		return orderRepository.getOrderById();
	}
	
	@GetMapping("/user/auth/order/getorderbystatus")
	public ObjectRespone getOrderByTrangThai(@RequestParam(name = "status",defaultValue = "1")Integer trangThai) {
		List<OrderDetail>l= orderRepository.getOrder(trangThai,getAccount().getId());
		Map<Integer, List<OrderDetail>> groupedByOrder = l.stream()
			    .collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()));
		return new ObjectRespone(200,"success",groupedByOrder);
	}
	
	
	
	

	public void handleOrder(Order o, List<OrderDetail> os) {
		os.forEach(v -> {
			v.setOrder(o);
			o.plusTotalOrder(v.getSoLuong() * v.getGiaBan());
		});
		orderHelper.addOrderItem(o);
	}
	
	

	public void handleOrder(Order o, List<OrderDetail> os, VoucherShop vs) {
		float tien = 0;
			os.forEach(v -> {
				v.setOrder(o);
				Float a=v.getSoLuong() * v.getGiaBan();
				o.plusTotalOrder(a);
				vs.getA().forEach(vss->System.out.println(vss));
				if (vs.inList(String.valueOf(v.getProductId()))) {
					o.tienTru +=v.getSoLuong()*v.getGiaDaGiam();
					o.tienTinhDuoc+=a;
				}
			});
			if(vs.getDonToiThieu()<1||(o.tienTru>0&&o.tienTru>=vs.getDonToiThieu())) {
				if(vs.getLoaiVoucher()==1) {
					tien=o.tienTinhDuoc*(vs.getGiaTriGiam()/100);
					o.setTienTru(tien>=vs.getTienGiamToiDa()&&vs.getTienGiamToiDa()>0?vs.getTienGiamToiDa():tien);
				}else {
					tien=o.tienTinhDuoc-vs.getGiaTriGiam();
					o.setTienTru(tien>0?tien:0);
				}
				orderHelper.addOrderItemDetail(os);
				orderHelper.addOrderItem(o);
				o.setVoucherShopId(vs);
			}else {
				throw new CustomeException("Không thể áp dụng voucher này");
			}
	}
	
	
	
	public Account getAccount() {
		return new Account(1);
	}
	
	
}

