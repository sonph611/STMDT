package com.API.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.API.configuation.VNPayConfig;

@Component
public class GeneratedUrlPayment {
//	
	@Autowired
	private VNPayConfig vnpayConfig;
	private String vnp_HashSecret = "UE5AQGNZSQFJD9VOXA4HAIFMYH4FZ1J0";
//	private String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
	
	public String generatedUrlPayment(double amount,double shippingFee,String url,String orderInfo) throws UnsupportedEncodingException{
		String vnp_Version = "2.1.0";
		amount=amount*100;
		String vnp_Command = "pay";
		String orderType = "billpayment";
		String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
		String vnp_IpAddr = "127.0.0.1";
		String vnp_TmnCode = "QIPASG5N";
		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf((int)amount));
		vnp_Params.put("vnp_CurrCode", "VND");
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderType", orderType);
		vnp_Params.put("vnp_Locale", "vn");
		vnp_Params.put("vnp_ReturnUrl",url);	
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
		vnp_Params.put("vnp_OrderInfo",orderInfo);
		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
		cld.add(Calendar.MINUTE, 120);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
		List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator<String> itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = itr.next();
			String fieldValue = vnp_Params.get(fieldName);
			if (fieldValue != null && fieldValue.length() > 0) {
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, "UTF-8"));
				query.append(URLEncoder.encode(fieldName, "UTF-8"));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, "UTF-8"));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = HmacUtils.hmacSha512Hex(vnp_HashSecret, hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = vnpayConfig.getVnp_Url() + "?" + queryUrl;
		return paymentUrl;
	}
}
