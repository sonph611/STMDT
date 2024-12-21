package com.API.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.DTO.cart.VoucherAplly;
import com.API.DTO.user.cart.CartViewDTO;
import com.API.model.Cart;
import com.API.model.OrderDetail;
import com.API.model.ProductDetail;
import com.API.model.VoucherShop;

import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
	
	@Query(value = "SELECT id,soLuong FROM cart WHERE taiKhoanId=:accountId AND sanPhamId=:productId",nativeQuery = true)
	Optional<Integer[]> getCartByProductId(@Param("accountId")int accountId,@Param("productId")int productId);
	
	@Query(value = "SELECT new com.API.model.Cart(c.id,:productId,:accountId,c.soLuong) FROM Cart c WHERE c.account.id=:accountId AND c.product.id=:productId")
	Optional<Cart> getCartByProductIdAndAccountId(@Param("accountId")int accountId,@Param("productId")int productId);
	
	@Modifying
	@Transactional
	@Query("UPDATE Cart p SET p.soLuong = :soLuong WHERE p.id = :id")
	int updateCart(@Param("soLuong") int soLuong, @Param("id") int id);
	
	
	// THAY ĐỔI MỚI NHẤT
	@Modifying
    @Query(value="DELETE FROM GioHang c WHERE c.id = :id AND c.taiKhoanId = :accountId",nativeQuery = true)
    int deleteCartById(@Param("id") Integer id, @Param("accountId") int accountId);
	
	@Query(value="SELECT new  com.API.model.ProductDetail(pd.soLuong,p.trangThai)"
			+ " FROM Cart c JOIN ProductDetail pd ON pd.id=c.product.id join\r\n"
			+ "Product p ON p.id =pd.product.Id WHERE c.id=:id and c.account.id=:accountId")
	Optional<ProductDetail> getCartUpdate(@PathParam("id") Integer id,@PathParam("accountId") Integer accountId);
	
	// phần này của orders  -- mới 
	
	
	// int id, float giaTriGiam, int loaiVoucher, float giamToiDa, String items
	@Query(value = "SELECT new com.API.model.VoucherShop(vs.id,vs.giaTriGiam,vs.loaiVoucher,vs.tienGiamToiDa,vs.donToiThieu,GROUP_CONCAT(vd.product.id ,'')) \r\n"
			+ "	FROM VoucherShop vs   JOIN VoucherShopDetail vd ON vs.id=vd.voucher.id\r\n"
			+ "	JOIN VoucherShopNguoiDung vn ON vn.voucher.id=vs.id\r\n"
			+ "	WHERE vs.id=:voucherId AND vs.shop.shopId=:shopId AND vn.account.id=:accountId AND vn.soLuocDung+1< vs.soLuocMoiNguoi+1 \r\n"
			+ "	AND 1+ vs.soLuocDaDung< vs.soLuocDung+1\r\n"
			+ "	GROUP BY (vs.id)")
	 Optional<VoucherShop >getVoucherByIdOfOrder(@Param("voucherId")Integer orderId,@Param("shopId")Integer shopId
			 ,@Param("accountId")Integer accountId);
	
	@Query(value = "SELECT new  com.API.model.OrderDetail(pd.id,c.soLuong,pd.giaBan) \r\n"
			+ "FROM Cart c\r\n"
			+ "JOIN ProductDetail pd ON pd.id = c.product.id\r\n"
			+ "JOIN  Product p ON p.id = pd.product.id\r\n"
			+ "LEFT JOIN ChiTietKhuyenMai ck ON ck.product.id = p.id\r\n"
			+ "LEFT JOIN KhuyenMai km ON km.id = ck.khuyenMai.id AND CURRENT_TIMESTAMP  BETWEEN km.ngayBatDau AND km.ngayKetThuc\r\n"
			+ "WHERE c.account.id = :accountId AND p.shop.shopId=:shopId AND  p.trangThai = 1 AND c.id IN :items")
	ArrayList<OrderDetail> getCartPrepareOrder(@Param("accountId")Integer accountId,@Param("shopId")Integer shopId,
											@Param("items")Set<Integer> items);
	
	
	// copy order.
	// int id, int product, int account, int soLuong
	@Query("SELECT new com.API.model.Cart(c.id, cd.product.id,:accountId, COALESCE(c.soLuong, 0) + cd.soLuong) " +
		       "FROM ProductDetail pd " +
		       "JOIN Product p ON p.id = pd.product.id " +
		       "JOIN OrderDetail cd ON cd.product.id = pd.id " +
		       "LEFT JOIN Cart c ON c.product.id = pd.id " +
		       "JOIN Order h ON h.id = cd.order.id " +
		       "WHERE h.id = :orderId AND h.accountId.id = :accountId " +
		       "AND p.trangThai = 1 and pd.soLuong+1> COALESCE(c.soLuong, 0) + cd.soLuong")
		Optional<List<Cart>> getCartCopyOrderDetail(@Param("orderId") Integer orderId, @Param("accountId") Integer accountId);

	
//	@Query(value = "SELECT p.id,p.tenSanPham FROM chitietdonhang ch\r\n"
//			+ "JOIN productdetails pd ON pd.id=ch.productId\r\n"
//			+ "JOIN products p ON p.id=pd.productId\r\n"
//			+ "JOIN donhang d ON d.id=ch.donHangId\r\n"
//			+ "WHERE d.accountId=1",nativeQuery = true)
//	List<Object[]> getOrder();

	
	
	
	
	
	
	// test.
	@Query("SELECT DISTINCT new  com.API.model.OrderDetail(c.product.id,c.soLuong,pd.giaBan,pd.giaBan-(pd.giaBan *(COALESCE(ck.khuyenMai.giaTriKhuyenMai, 0.000001)/100))"
			+ ",p.id,p.shop.shopId,lp.live.id,lp.soLuongGioiHan,lp.giaGiam)  FROM Cart c\r\n"
			+ "JOIN ProductDetail pd ON pd.id=c.product.id\r\n"
			+ "JOIN Product p ON p.id=pd.product.id join Shop s on s.id=p.shop.id"
			+ " left join Live_Product lp on lp.product.id=p.id and (lp.soLuongGioiHan<1 or lp.soLuongGioiHan>=lp.soLuocDaDung+c.soLuong) and lp.live.startTime is not null and lp.live.endTime is null "
			+ "   " 
			+ "LEFT JOIN ChiTietKhuyenMai ck ON ck.product.id=p.id and NOW() BETWEEN ck.khuyenMai.ngayBatDau AND ck.khuyenMai.ngayKetThuc \r\n"
			+ ""
			+ "WHERE c.account.id=:accountId AND p.trangThai=1 and s.trangThai=1\r\n"
			+ "AND c.id IN :items")
	List<OrderDetail> getOrderDetailInlist(@Param("accountId") Integer id,Set<Integer> items);
//	Bản gốc  
//	@Query("SELECT new  com.API.model.OrderDetail(c.product.id,c.soLuong,pd.giaBan,pd.giaBan-(pd.giaBan *(COALESCE(km.giaTriKhuyenMai, 0.000001)/100)),p.id,p.shop.shopId)  FROM Cart c\r\n"
//			+ "JOIN ProductDetail pd ON pd.id=c.product.id\r\n"
//			+ "JOIN Product p ON p.id=pd.product.id\r\n"
//			+ "LEFT JOIN ChiTietKhuyenMai ck ON ck.product.id=p.id\r\n"
//			+ "LEFT JOIN KhuyenMai km ON km.id=ck.khuyenMai.id AND NOW() BETWEEN km.ngayBatDau AND km.ngayKetThuc\r\n"
//			+ "WHERE c.account.id=:accountId AND p.trangThai=1 and c.soLuong<pd.soLuong+1\r\n"
//			+ "AND c.id IN :items")
//	List<OrderDetail> getOrderDetailInlistTwo(@Param("accountId") Integer id,Set<Integer> items);
	
	
	
	
	@Query("select count(p.id) from Cart p where p.account.id=:accountId")
	Integer getCountCart(@Param("accountId")Integer c);
	
	
	
	
	// end ĐANG TEST PHẦN LẤY CART
	
//	
//	@Query(value="select new com.API.DTO.user.cart.CartViewDTO(c.id,pd.soLuong,pd.giaBan,"
//			+ "p.tenSanPham,c.soLuong,pd.hinhAnh,CONCAT(ms.tenMau,' - ', k.tenKichThuoc)"
//			+ ",s.shopId,s.shopName,CASE \r\n"
//			+ "    WHEN ck.product IS NOT NULL THEN km.giaTriKhuyenMai\r\n"
//			+ "    ELSE 0 \r\n"
//			+ "END,p.id,ls.id,lp.giaGiam,lp.soLuongGioiHan,lp.soLuocDaDung) from Cart c join ProductDetail pd on c.product.id=pd.id and c.account.id=:accountId join mauSac ms on ms.id=pd.mauSac.id"
//			+ " join KichThuoc k on k.id=pd.kichThuoc.id join"
//			+ " Product p on p.id=pd.product.id and p.trangThai=1 join shop s on s.shopId=p.shop.shopId"
//			+ " left join Live_Product lp on lp.product.id=p.id "
//			+ "	left join LiveSession ls on ls.id=lp.live.id and ls.startTime is not null and ls.endTime is NULL left join KhuyenMai km on km.shop.shopId=s.shopId and now() between km.ngayBatDau and km.ngayKetThuc  "
//			+ "  left join ChiTietKhuyenMai ck on ck.product.id=p.id and ck.khuyenMai.id=km.id "
//			+ " ")
//	List<CartViewDTO> getCartWithLive(@Param("accountId")Integer accountId);
	
	
	@Query(value="select new com.API.DTO.user.cart.CartViewDTO(c.id,pd.soLuong,pd.giaBan,"
	        + "p.tenSanPham,c.soLuong,pd.hinhAnh,CONCAT(ms.tenMau,' - ', k.tenKichThuoc),"
	        + "s.shopId,s.shopName, "
	        + "ck.khuyenMai.giaTriKhuyenMai, p.id) "
	        + "from Cart c "
	        + "join ProductDetail pd on c.product.id = pd.id and c.account.id = :accountId "
	        + "join mauSac ms on ms.id = pd.mauSac.id "
	        + "join KichThuoc k on k.id = pd.kichThuoc.id "
	        + "join Product p on p.id = pd.product.id and p.trangThai = 1 "
	        + "join shop s on s.shopId = p.shop.shopId "
	        + "left join ChiTietKhuyenMai ck on ck.product.id=p.id and now() between ck.khuyenMai.ngayBatDau and ck.khuyenMai.ngayKetThuc"
	        + "")
	List<CartViewDTO> getCartWithLive(@Param("accountId")Integer accountId);
	
	// HẾT PHẦN LẤY CART
	
	@Query(value="select new com.API.DTO.user.cart.CartViewDTO(c.id,pd.soLuong,pd.giaBan,"
			+ "p.tenSanPham,c.soLuong,pd.hinhAnh,CONCAT(ms.tenMau,' - ', k.tenKichThuoc)"
			+ ",s.shopId,s.shopName,km.giaTriKhuyenMai,p.id) from Cart c join ProductDetail pd on c.product.id=pd.id and c.account.id=:accountId join mauSac ms on ms.id=pd.mauSac.id"
			+ " join KichThuoc k on k.id=pd.kichThuoc.id join"
			+ " Product p on p.id=pd.product.id and p.trangThai=1 join shop s on s.shopId=p.shop.shopId left join KhuyenMai km on km.shop.shopId=s.shopId and NOW() BETWEEN km.ngayBatDau AND km.ngayKetThuc"
			+ " left join ChiTietKhuyenMai ck on ck.product.id=p.id and ck.khuyenMai.id=km.id"
			+ " ")
	List<CartViewDTO> getCart(@Param("accountId")Integer accountId);
	
	
	// PHẦN LẤY CHEKCKOUUT
	
//	@Query(value = "select DISTINCT  new com.API.DTO.user.cart.CartViewDTO(p.shop,c.id, p.soLuong, pd.giaBan, "
//	        + "p.tenSanPham, c.soLuong, pd.hinhAnh, CONCAT(ms.tenMau, ' - ', k.tenKichThuoc), "
//	        + "s.shopId, s.shopName, km.giaTriKhuyenMai, p.id,ls.id,lp.giaGiam,lp.soLuongGioiHan,lp.soLuocDaDung) "
//	        + "from Cart c "
//	        + "join ProductDetail pd on c.product.id = pd.id  and c.id in :ids"
//	        + " "
//	        + "join MauSac ms on ms.id = pd.mauSac.id "
//	        + "join KichThuoc k on k.id = pd.kichThuoc.id "
//	        + "join Product p on p.id = pd.product.id "
//	        + "join Shop s on s.shopId = p.shop.shopId "
//	        + "left join Live_Product lp on lp.product.id = p.id "
//	        + "left join LiveSession ls on ls.id = lp.live.id and ls.startTime is not null and ls.endTime is null left join KhuyenMai km on km.shop.id = s.shopId and NOW() between km.ngayBatDau and km.ngayKetThuc and km.trangThai=1 "
//	        + "left join ChiTietKhuyenMai ck on ck.product.id = p.id and ck.khuyenMai.id=km.id"
//	        + " "
//	        + "where c.id in :ids  and c.account.id = :accountId")
//	List<CartViewDTO> getCartInlist(@Param("accountId") Integer accountId, @Param("ids") List<Integer> ids);
	@Query(value = "select   new com.API.DTO.user.cart.CartViewDTO(p.shop,c.id, p.soLuong, pd.giaBan, "
	        + "p.tenSanPham, c.soLuong, pd.hinhAnh, CONCAT(ms.tenMau, ' - ', k.tenKichThuoc), "
	        + "s.shopId, s.shopName, ck.khuyenMai.giaTriKhuyenMai, p.id,lp.live.id,lp.giaGiam,lp.soLuongGioiHan,lp.soLuocDaDung) "
	        + "from Cart c "
	        + "join ProductDetail pd on c.product.id = pd.id  and c.id in :ids"
	        + " "
	        + "join MauSac ms on ms.id = pd.mauSac.id "
	        + "join KichThuoc k on k.id = pd.kichThuoc.id "
	        + "join Product p on p.id = pd.product.id "
	        + "join Shop s on s.shopId = p.shop.shopId "
	        + "left join Live_Product lp on lp.product.id = p.id and lp.live.startTime is not null and lp.live.endTime is null "
	        + "left join ChiTietKhuyenMai ck on ck.product.id = p.id and now() between ck.khuyenMai.ngayBatDau and ck.khuyenMai.ngayKetThuc"
	        + " "
	        + "where c.id in :ids  and c.account.id = :accountId")
	List<CartViewDTO> getCartInlist(@Param("accountId") Integer accountId, @Param("ids") List<Integer> ids);
	
	// HẾT PHẦN LẤY CHECK OUT
	
//	@Query(value = "select new com.API.DTO.user.cart.CartViewDTO(c.id, p.soLuong, pd.giaBan, "
//	        + "p.tenSanPham, c.soLuong, pd.hinhAnh, CONCAT(ms.tenMau, ' - ', k.tenKichThuoc), "
//	        + "s.shopId, s.shopName, km.giaTriKhuyenMai, p.id,ls.id,lp.giaGiam,lp.soLuongGioiHan,lp.soLuocDaDung) "
//	        + "from Cart c "
//	        + "join ProductDetail pd on c.product.id = pd.id  and c.id in :ids"
//	        + " "
//	        + "join MauSac ms on ms.id = pd.mauSac.id "
//	        + "join KichThuoc k on k.id = pd.kichThuoc.id "
//	        + "join Product p on p.id = pd.product.id "
//	        + "join Shop s on s.shopId = p.shop.shopId "
//	        + "left join Live_Product lp on lp.product.id = p.id "
//	        + "left join LiveSession ls on ls.id = lp.live.id and ls.startTime is not null and ls.endTime is null "
//	        + "left join ChiTietKhuyenMai ck on ck.product.id = p.id "
//	        + "left join KhuyenMai km on km.id = ck.khuyenMai.id and NOW() between km.ngayBatDau and km.ngayKetThuc "
//	        + "where c.id in :ids  and c.account.id = :accountId")
//	List<CartViewDTO> getCartInlist(@Param("accountId") Integer accountId, @Param("ids") List<Integer> ids);

	
	
	
 // ĐOẠN NÀY VỪA MỚI COMMNET 6/12
//	@Query(value = "select new com.API.DTO.user.cart.CartViewDTO(c.id, p.soLuong, pd.giaBan, "
//	        + "p.tenSanPham, c.soLuong, pd.hinhAnh, CONCAT(ms.tenMau, ' - ', k.tenKichThuoc), "
//	        + "s.shopId, s.shopName, km.giaTriKhuyenMai, p.id,ls.id,lp.giaGiam,lp.soLuongGioiHan) "
//	        + "from Cart c "
//	        + "join ProductDetail pd on c.product.id = pd.id "
//	        + "and c.account.id = :accountId "
//	        + "join MauSac ms on ms.id = pd.mauSac.id "
//	        + "join KichThuoc k on k.id = pd.kichThuoc.id "
//	        + "join Product p on p.id = pd.product.id "
//	        + "join Shop s on s.shopId = p.shop.shopId "
//	        + "left join Live_Product lp on lp.product.id = p.id "
//	        + "left join LiveSession ls on ls.id = lp.live.id and ls.startTime is not null and ls.endTime is null "
//	        + "left join ChiTietKhuyenMai ck on ck.product.id = p.id "
//	        + "left join KhuyenMai km on km.id = ck.khuyenMai.id and NOW() between km.ngayBatDau and km.ngayKetThuc "
//	        + "where c.id in :ids")
//	List<CartViewDTO> getCartInlistSet(@Param("accountId") Integer accountId, @Param("ids") Set<Integer> ids);
	
	
//	@Query(value="select new com.API.DTO.user.cart.CartViewDTO(c.id,p.soLuong,pd.giaBan,"
//			+ "p.tenSanPham,c.soLuong,pd.hinhAnh,CONCAT(ms.tenMau,' - ', k.tenKichThuoc)"
//			+ ",s.shopId,s.shopName,km.giaTriKhuyenMai,p.id) from Cart c join ProductDetail pd on c.product.id=pd.id"
//			+ " and c.account.id=:accountId join mauSac ms on ms.id=pd.mauSac.id"
//			+ " join KichThuoc k on k.id=pd.kichThuoc.id join"
//			+ " Product p on p.id=pd.product.id  join shop s on s.shopId=p.shop.shopId"
//			+ " left join ChiTietKhuyenMai ck on ck.product.id=p.id "
//			+ " left join KhuyenMai km on km.id=ck.khuyenMai.id and NOW() BETWEEN km.ngayBatDau AND km.ngayKetThuc where c.id in:ids")
//	List<CartViewDTO> getCartInlists(@Param("accountId")Integer accountId,@Param("ids")List<Integer>ids);
	
	//

	
	@Query(value = "SELECT new com.API.DTO.cart.VoucherAplly(vc.id,vn.voucher.id,vc.loaiVoucher,vc.giaTriGiam,vc.donToiThieu,vc.tenVoucher,"
			+ "vc.soLuocDung,vc.soLuocDaDung,GROUP_CONCAT(vs.product.id,''),vc.tienGiamToiDa) \r\n"
	        + "FROM VoucherShop vc\r\n"
	        + "JOIN VoucherShopDetail vs ON vs.voucher.id = vc.id and vc.trangThai=1\r\n"
	        + "LEFT JOIN VoucherShopNguoiDung vn ON vn.voucher.id = vc.id and vn.account.id=:accountId \r\n"
	        + "WHERE vc.shop.shopId = :shopId and vs.product.id in:ids \r\n"
	        + "AND NOW() BETWEEN vc.ngayBatDau AND vc.ngayKetThuc\r\n"
	        + "AND vc.soLuocDaDung < vc.soLuocDung\r\n"
	        + "AND (COALESCE(vn.soLuocDung, 0) + 1 <= vc.soLuocMoiNguoi)\r\n"
	        + "GROUP BY vc.id")
	public List<VoucherAplly> getVoucherCanAplyProductList(@Param("ids")List<Integer>ids,@Param("shopId")Integer shopId,@Param("accountId")Integer accountId);

	
//	ssss
//	vc.id,vc.tenVoucher, vc.loaiVoucher, vc.giaTriGiam, vc.donToiThieu,vn.vouchercuahangId,\r\n"
//			+ "      vc.soLuocDaDung, vc.soLuocDung,GROUP_CONCAT('', vs.sanPham_Id,' ') AS sanPhams
	
//	Integer voucherId, Integer accountId, Integer loaiVoucher, Float giaTriGiam, Double donToiThieu,
//	String voucherName, Integer voucherSoLuocDung, Integer voucherSoLuocDaDung, List<String> productIds,
//	Integer canApply, Double priceDiscount
	
//	public void testt() {
//		new VoucherAplly(null, null, null, null, null, null, null, null, null, null, null)
//	}
	
	
	
	
//	@Query(value="SELECT v.* from vouchernguoidung v \r\n"
//			+ "	JOIN vouchershop vs ON vs.id=v.voucherId\r\n"
//			+ "	JOIN vouchershopdetails vd ON vd.voucherId=vs.id\r\n"
//			+ "	JOIN products p ON p.id=vd.id\r\n"
//			+ "	JOIN productdetails pd ON pd.productId=p.id\r\n"
//			+ "	JOIN cart c ON c.productId=pd.id\r\n"
//			+ "	WHERE c.accountId=1 and v.soLuocDung<vs.soLuocMoiNguoi+1 AND v.soLuocDung AND c.id IN (1,2,3)",nativeQuery = true)
//	List<Object[]> getCarts();
	
//	@Query(value="SELECT  c.soLuong,c.productId,pd.soLuong,pd.giaBan,km.giaTriKhuyenMai FROM cart c JOIN productdetails pd ON pd.id=c.productId\r\n"
//			+ "			JOIN products p ON p.id=pd.productId\r\n"
//			+ "			JOIN chitietkhuyenmai ck ON ck.productId=p.id\r\n"
//			+ "			LEFT join  khuyenmai km ON km.id=ck.khuyenMaiId\r\n"
//			+ "			WHERE c.accountId=1 AND p.shopId=1\r\n"
//			+ "			AND NOW() BETWEEN km.ngayBatDau AND km.ngayKetThuc and c.id in(1,2,2,3,4,5,6,6)",nativeQuery = true)
//	List<Object[]> getCarts();

	

	
}
