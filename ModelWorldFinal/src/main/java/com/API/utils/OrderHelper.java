package com.API.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

import com.API.DTO.order.OrderInsert;
import com.API.DTO.user.cart.CartViewDTO;
import com.API.model.Account;
import com.API.model.Order;
import com.API.model.OrderDetail;
import com.API.model.ThanhToan;

//@Component
//@Service
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrderHelper {
	
	
	public List<CartViewDTO> getCartsView() {
		return cartsView;
	}

	public void setCartsView(List<CartViewDTO> cartsView) {
		this.cartsView = cartsView;
	}

	public Account account;
	public OrderInsert o;
	List<Order> order=new ArrayList<Order>();
	List<OrderDetail> orderDetails=new ArrayList<OrderDetail>();
	List<CartViewDTO> cartsView=new ArrayList<CartViewDTO>();
	
	public OrderInsert getO() {
		return o;
	}

	public void setO(OrderInsert o) {
		this.o = o;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public void addOrderItem(Order o) {
		this.order.add(o);
	}

	public void addOrderItemDetail(OrderDetail o) {
		this.orderDetails.add(o);
	}
	public void addOrderItemDetail(List<OrderDetail> o) {
		this.orderDetails.addAll(o);
	}
	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}
	
}
