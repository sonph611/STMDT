package com.API.repository;

import java.lang.foreign.Linker.Option;
import java.time.LocalDateTime;
import java.util.Date;
//import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.ParamDef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.API.model.Account;
import com.API.model.DiaChi;
//import com.API.model.DiaChi;
import com.API.model.Order;
import com.API.model.OrderDetail;
import com.API.model.ThanhToan;
//import com.API.model.ThanhToan;
//import com.API.model.ProductDetail;
//import com.API.model.Shop;
import com.API.model.TrangThai;

import jakarta.transaction.Transactional;

public interface DonHangRepository extends JpaRepository<Order, Integer> {
	
	
	// đoạn thêm mới =====================

	@Query("select count(p.id) from Order p where p.id in:ids and p.shop.shopId=:shopId and p.trangThai.id =:status")
	Integer countOrderByStatusAndShopId(@Param("ids")List<Integer>ids,@Param("shopId")Integer shopId,@Param("status")Integer status);
	
	@Modifying
	@Transactional
	@Query("update Order d set d.trangThai.id=:status where d.id in :ids")
	Integer updateNextStatusInOrderIds(@Param("ids")List<Integer>ids,@Param("status")Integer status);
	
	@Query("select p.trangThai.id from Order p where p.id=:id and p.shop.shopId=:shopId ")
	Integer getOrderIdByIdAndShopIdAndStatus(@Param("id")Integer ids,@Param("shopId")Integer shopId);
	
	@Query("select p.ngayTaoDon from Order p where p.id=:id and p.shop.shopId=:shopId and p.trangThai.id=5")
	Date getOrderDateByIdAndShopIdAndStatus(@Param("id")Integer ids,@Param("shopId")Integer shopId);
	
	@Query("select p.accountId.id from Order p where p.id=:orderId")
	Integer getAccountIdByOrderId(@Param("orderId")Integer orderId);
	
	@Modifying
	@Transactional
	@Query("update Order d set d.trangThai.id=:status where d.id =:id")
	Integer updateNextStatusInOrderId(@Param("id")Integer id,@Param("status")Integer status);
	
	@Modifying
	@Transactional
	@Query("update Order d set d.trangThai.id=:status where d.id =:ids")
	Integer updateStatusSingle(@Param("ids")Integer id,@Param("status")Integer status);
	
	
	// lấy accountId dựa trên đơn hàng 
	
	@Query("select s.account.id from Order d join Shop s on d.id=:id and s.id=d.shop.shopId")
	Integer getAccountByOrderId(@Param("id")Integer id);
	
	
	
	@Query(value = "SELECT  m.month_number,COUNT(o.id),SUM(o.tongTien),SUM(d.soLuong),COUNT(DISTINCT o.taiKhoanId),AVG(o.tongTien),SUM( o.tienTruVoucherShop)\r\n"
			+ "FROM \r\n"
			+ "    (SELECT 1 AS month_number\r\n"
			+ "     UNION ALL  SELECT 2  UNION ALL  SELECT 3  UNION ALL   SELECT 4  UNION ALL  SELECT 5\r\n"
			+ "     UNION ALL\r\n"
			+ "     SELECT 6\r\n"
			+ "     UNION ALL\r\n"
			+ "     SELECT 7\r\n"
			+ "     UNION ALL\r\n"
			+ "     SELECT 8\r\n"
			+ "     UNION ALL\r\n"
			+ "     SELECT 9\r\n"
			+ "     UNION ALL\r\n"
			+ "     SELECT 10\r\n"
			+ "     UNION ALL\r\n"
			+ "     SELECT 11\r\n"
			+ "     UNION ALL\r\n"
			+ "     SELECT 12) AS m\r\n"
			+ "LEFT JOIN \r\n"
			+ "    DonHang o \r\n"
			+ "LEFT JOIN chitietdonhang d ON d.donHangId=o.id\r\n"
			+ "ON \r\n"
			+ "    MONTH(o.ngayTaoDon) = m.month_number\r\n"
			+ "    AND YEAR(o.ngayTaoDon)=:year AND o.shopId=:shopId\r\n"
			+ "GROUP BY \r\n"
			+ "    m.month_number\r\n"
			+ "ORDER BY \r\n"
			+ "    m.month_number;\r\n"
			+ "",nativeQuery = true)
	List<Object[]>getReportOrder(@Param("shopId")Integer shopId,@Param("year")Integer year);
	
	
	// Seller
		// Hàm lấy hóa đơn theo id seller
		@Query(value = "CALL GetOrdersByShopId(:shopId)", nativeQuery = true)
		List<Object[]> findDonHangsByShopId(@Param("shopId") Integer shopId);

		// Oders
		// Hàm lấy tất car hóa đơn
		@Query(value = "CALL GetAllOrders()", nativeQuery = true)
		List<Object[]> getAllOrders();

		// Hàm lây chi tiết hóa đơn
		@Query(value = """
				SELECT
				    dh.id,
				    tk.id AS idKhachHang,
				    tk.hoVaTen AS tenKhachHang,
				    tk.email AS emailKhachHang,
				    tk.soDienThoai AS sdtKhachHang,
				    dc.soDienThoai,
				    dc.toanBoDiaChi,
				    vs.tenVoucher AS tenVoucherSan,
				    vch.tenVoucher AS tenVoucherShop,
				    ts.TenTrangThai,
				    tt.TenHinhThucThanhToan,
				    dh.ngayTaoDon,
				    dh.tongTien,
				    dh.ghiChu,
				    dh.lyDo,
				    dh.phiShip,
				    dh.tienTruVoucherSan,
				    dh.tienTruVoucherShop,
				    ch.id AS idCuaHang,
				    ch.tenShop AS tenCuaHang,
				    ch.hoVaTen AS chuCuaHang,
				    ch.email AS emailCuaHang,
				    tkShop.soDienThoai AS soDienThoaiShop,
				    tk.tenTaiKhoan
				FROM
				    donhang dh
				LEFT JOIN
				    taikhoan tk ON dh.taiKhoanId = tk.id
				LEFT JOIN
				    diachi dc ON dh.diaChiId = dc.id
				LEFT JOIN
				    vouchersan vs ON dh.voucherSanId = vs.id
				LEFT JOIN
				    voucherscuahang vch ON dh.voucherShopId = vch.id
				LEFT JOIN
				    trangthai ts ON dh.trangThaiId = ts.id
				LEFT JOIN
				    thanhtoan tt ON dh.thanhToanId = tt.id
				LEFT JOIN
				    cuahang ch ON dh.shopId = ch.id
				LEFT JOIN
				    taikhoan tkShop ON ch.taiKhoan_Id = tkShop.id
				WHERE
				    dh.id = ?;
				""", nativeQuery = true)
		Optional<Object[]> findOrderDetailsById(@Param("orderId") Integer orderId);

		// Lấy sản phẩm trong hóa đơn chi tiết

		@Query(value = """
				SELECT
				    ctdh.id AS chiTietDonHangId,
				    ctdh.soLuong AS soLuongDat,
				    ctdh.giaBan AS giaBanChiTietDonHang,
				    sp.tenSanPham,
				    sp.hinhAnh AS hinhAnhSanPham,
				    ctp.hinhAnhBienThe,
				    ms.tenMau,
				    kt.tenKichThuoc
				FROM
				    chitietdonhang ctdh
				INNER JOIN
				    chitietsanpham ctp ON ctdh.productId = ctp.id
				INNER JOIN
				    sanpham sp ON ctp.sanPhamId = sp.id
				INNER JOIN
				    mausac ms ON ctp.mauSacId = ms.id
				INNER JOIN
				    kichthuoc kt ON ctp.kichThuocId = kt.id
				WHERE
				    ctdh.donHangId = :orderId
				""", nativeQuery = true)
		List<Object[]> findChiTietDonHangByDonHangId(@Param("orderId") Integer donHangId);
	
	
	
	
	// ================================
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Query("select new com.API.model.OrderDetail(cd.id,cd.order.id,cd.soLuong,cd.giaBan,new com.API.model.ProductDetail"
			+ "(new com.API.model.Product(p.id,p.tenSanPham),pd.hinhAnh,pd.mauSac.tenMau,pd.kichThuoc.tenKichThuoc ))    "
			+ " from OrderDetail cd "
			+ "join ProductDetail pd on pd.id=cd.product.id "
			+ "join Product p on p.id=pd.product.Id"
			+ " where cd.id =:orderId")
	Optional<OrderDetail> getOrderDetailById(@Param("orderId")Integer id );
	
	@Query("SELECT cd.id FROM OrderDetail cd \r\n"
			+ "JOIN Order d  ON  cd.order.id=d.id\r\n"
			+ "WHERE cd.id=:orderId AND cd.trangThaiDanhGia=0 and d.trangThai.id=6 AND d.accountId.id=:accountId")
	Optional<Integer> getOrderDetailWhereNotRating(@Param("orderId")Integer orderDetailId,@Param("accountId") Integer accountId);
	
 	@Transactional
    @Modifying
    @Query("update OrderDetail d set d.trangThaiDanhGia=1 where d.id=:orderId")
    void updateRetingOrderDetail(@Param("orderId")Integer donHangId);
	
	// lất tất cả các người dùng từ đơn hàng.
	@Query("select DISTINCT accountId from Order d where d.shop.shopId=:shopId")
	List<Account> getUserOrderInShop(@Param(value ="shopId")Integer id);
	
	@Query("select new com.API.model.Order(p.id,p.tongTien,p.phiShip,p.tienTru,p.tienTruVoucherSan) from Order p where p.id=:orderId and p.trangThai.id=1 and p.accountId.id=:accountId and p.ngayTaoDon>=:date")
	Optional<Order> getOrderCanPayment(@Param(value ="accountId")Integer accountId,@Param(value ="orderId")Integer orderId,@Param(value ="date")LocalDateTime date);

	@Query("SELECT new com.API.model.Order(d.id,d.tienTru,d.tongTien,d.accountId.id,d.accountId.hoVaTen,d.accountId.soDienThoai)"
			+ " FROM Order d WHERE d.voucherShopId.id=:voucherId")
	Page<Order>getOrderInVoucher(@Param("voucherId")Integer voucherId,Pageable p);
	
	
//	Integer id ,Float tienTruVoucherShop,Float total,Integer userId,String tenUser,String sdt
	
	// gọi store hủy số lược dùng đơn hàng lại.
	@Procedure(name = "UpdateVoucherUsage")
    void updateVoucherUsage(@Param("voucherShopId") Integer voucherShopId, 
                            @Param("voucherSanId") Integer voucherSanId);
	@Modifying
    @Transactional
	@Query("delete OrderDetail p where p.order.id=:id")
	void deleteItemOrder(@Param(value ="id")Integer id);
	
//	UPDATE SỐ LƯỢNG SẢN PHẨM KHI HỦY MỘT ĐƠN
	 @Transactional
	    @Modifying
	    @Query(value = "CALL UpdateSoLuongSanPhamKhiHuyDon(:donHangId)", nativeQuery = true)
	    void updateSoLuongSanPham(Integer donHangId);
	 
	 
	 @Query("SELECT d.ngayTaoDon FROM Order d where d.id=:orderId and d.shop.id=:shopId")
	 Optional<Date> getDateOrderById(@Param("orderId")Integer orderId,@Param("shopId")Integer shopId);
	
	 
	 @Modifying
	 @Transactional
	 @Query("update Order d set d.daNhanHang=1 where d.id=:orderId ")
	 void updateNhanHangByOrderId(@Param("orderId")Integer id);
	 
//	 @Transactional
//	    @Modifying
//	    @Query("update Orde")
//	    void canelOrderInList(@Param("ids") list);
	 
	 @Modifying
	@Query("UPDATE Order o SET o.trangThai.id =7 , o.lyDo=:lyDo WHERE o.id in :ids")
	void cancelOrderInList(@Param("ids") List<Integer>ids,@Param("lyDo") String message);
	
	 
	@Query("select new com.API.model.Order(p.trangThai.id,p.voucherSanId.id,p.voucherShopId.id) from Order p where p.id=:id and p.accountId.id=:accountId")
	Optional<Order> getTrangThaiDonHangById(@Param("id")Integer id,@Param("accountId")Integer accountId);
	
	// CHỈNH SỬA Ở ĐÂY 
	@Query("select p "
			+ "from Order p where p.id=:id and p.shop.shopId=:shopId ")
	Optional<Order> getTrangThaiDonHangByIdTwo(@Param("id")Integer id,@Param("shopId")Integer shopId);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Query("select new com.API.model.Order(p.trangThai.id,p.voucherSanId.id,p.voucherShopId.id) "
			+ "from Order p where p.id=:id and p.accountId.id=:shopId ")
	Optional<Order> getTrangThaiDonHangByIdTwoAccountId(@Param("id")Integer id,@Param("shopId")Integer shopId);
	
	@Modifying
	@Query("Update Order d set d.trangThai.id=6 where d.id=:orderId and d.accountId.id=:accountId and d.trangThai.id=5 ")
	Integer confirmOrder(@Param("orderId") Integer orderId,@Param("accountId") Integer accoutId);
	
	
	@Modifying
	@Query("Update Order d set d.trangThai.id=7 where d.id=:orderId and d.accountId.id=:accountId and d.trangThai.id=5 ")
	Integer UpdateNoteReaciveOrder(@Param("orderId") Integer orderId,@Param("accountId") Integer accoutId);
	
	
	@Modifying
	@Query("UPDATE Order o SET o.trangThai = :newStatus , o.lyDo=:lyDo WHERE o.id = :orderId")
	void updateOrderStatus(@Param("orderId") Integer orderId, @Param("newStatus") TrangThai newStatus,@Param("lyDo") String message);
	
	
	@Modifying
	@Query("UPDATE Order o SET o.nguoiHuy=0, o.trangThai = :newStatus , o.lyDo=:lyDo WHERE o.id = :orderId ")
	void updateOrderStatusUser(@Param("orderId") Integer orderId, @Param("newStatus") TrangThai newStatus,@Param("lyDo") String message);
//	new com.API.model.Order(p.trangThai.id,p.voucherSanId.id,p.voucherShopId.id)
	
	@Query("select new com.API.model.Order(p.trangThai.id,p.voucherSanId.id,p.voucherShopId.id,p.accountId.id,p.id)"
			+ "  from Order p where  p.id in:ids and p.shop.shopId=:shopId and p.trangThai.id=1")
	List<Order> getAllOrderSalerInListTwo(@Param("shopId")Integer shopId,@Param("ids")List<Integer> ids);
	
	
//	@Query("select new com.API.model.Order(p.trangThai.id,p.voucherSanId.id,p.voucherShopId.id,p.accountId.id,p.id)"
//			+ "  from Order p where  p.id in:ids and p.shop.shopId=:shopId and p.trangThai.id=1")
//	List<Order> getOrderFinishThanhToan(@Param("ids")List<Integer> ids);
	
	
	@Modifying
	@Transactional
	@Query("UPDATE Order o SET o.trangThai.id =2  WHERE o.id in :orderIds and o.trangThai.id=1")
	Integer updatePaymentOrder(@Param("orderIds")List<Integer>ids);
	
	
	@Modifying
	@Query("UPDATE Order o SET o.trangThai = :newStatus WHERE o.id = :orderId")
	void updateOrderStatus(@Param("orderId") Integer orderId, @Param("newStatus") TrangThai newStatus);
	
	@Query("select p.id from Order p where p.id=:orderId and p.accountId.id=:accountId")
	Optional<Integer> getIdOrderById(@Param("orderId") Integer orderId,@Param("accountId")Integer accountId);
	
	@Query(value = "SELECT new  com.API.model.OrderDetail(d.id,s.shopId,s.shopName,pd.id,p.id,p.tenSanPham,ch.giaBan,ch.soLuong"
			+ ",ms.tenMau,k.tenKichThuoc,d.tongTien) FROM OrderDetail ch\r\n"
			+ "JOIN ProductDetail pd ON pd.id=ch.product.id\r\n"
			+ "JOIN KichThuoc k ON k.id=pd.kichThuoc.id\r\n"
			+ "JOIN MauSac ms ON ms.id=pd.mauSac.id\r\n"
			+ "JOIN Product p ON p.id=pd.product.id\r\n"
			+ "JOIN Order d ON d.id=ch.order.id\r\n"
			+ "JOIN Shop s ON d.shop.shopId=s.shopId\r\n"
			+ "WHERE d.accountId.id=:accountId \r\n"
			+ "")
	List<OrderDetail> getOrder(@Param("accountId")Integer accountId);
	
//	Integer orderId,Integer shopId,String shopName,Integer productOrderId,Integer productId,String productName,
//	float giaBanm,String tenMau,String tenKichThuoc,float tongDon
	
	
	
	
	
	@Query(value=" select count(p.id) from donHang p where p.id in :ids and p.trangThaiId=1 and p.taiKhoanId=:accountId",nativeQuery = true)
	Integer countByIdFinishPayment(@Param("ids")int[] orderId,@Param("accountId")Integer accountId);
	
	@Modifying
	@Query(value="Update donHang p set p.trangThaiId=2 where id in :ids ",nativeQuery = true)
	Integer updateTrangThaiOrder(@Param("ids")List<Integer> orderId);
	
	
	
	@Query(value = "SELECT new  com.API.model.OrderDetail(d.id,s.shopId,s.shopName,pd.id,p.id,p.tenSanPham,ch.giaBan,ch.soLuong"
			+ ",ms.tenMau,k.tenKichThuoc,d.tongTien) FROM OrderDetail ch\r\n"
			+ "JOIN ProductDetail pd ON pd.id=ch.product.id\r\n"
			+ "JOIN KichThuoc k ON k.id=pd.kichThuoc.id\r\n"
			+ "JOIN MauSac ms ON ms.id=pd.mauSac.id\r\n"
			+ "JOIN Product p ON p.id=pd.product.id\r\n"
			+ "JOIN Order d ON d.id=ch.order.id\r\n"
			+ "JOIN Shop s ON d.shop.shopId=s.shopId\r\n"
			+ "WHERE d.accountId.id=:accountId and d.trangThai.id=:status\r\n"
			+ "")
	List<OrderDetail> getOrder(@Param("status")Integer status,@Param("accountId")Integer accountId);
	
//	@Query(value = "SELECT p.id,p.tenSanPham,pd.giaBan, ch.giaBan,s.shopName,s.shopId,ms.tenMau,k.tenKichThuoc FROM chitietdonhang ch\r\n"
//			+ "JOIN ChiTietSanPham pd ON pd.id=ch.productId\r\n"
//			+ "JOIN kichthuoc k ON k.id=pd.kichThuocId\r\n"
//			+ "JOIN mausac ms ON ms.id=pd.mauSacId\r\n"
//			+ "JOIN SanPham p ON p.id=pd.productId\r\n"
//			+ "JOIN donhang d ON d.id=ch.donHangId\r\n"
//			+ "JOIN shop s ON d.shopId=s.shopId\r\n"
//			+ "WHERE d.accountId=1\r\n"
//			+ "",nativeQuery = true)
//	List<Object[]> getOrder(@Param("key")String key);
	
	
	// chi tiết đơn hàng.
	
	@Query("select new com.API.model.Order(p.id,new com.API.model.Shop(p.shop.shopId,p.shop.shopName)"
			+ ",p.ngayTaoDon,p.tongTien,p.phiShip,p.diaChiId,p.trangThai,p.thanhToanId) "
			+ "from Order p where p.id=:orderId ")
	Optional<Order> getOrderById(@Param("orderId")Integer orderId);
	
	@Query("select new com.API.model.Order(p.id,new com.API.model.Shop(p.shop.shopId,p.shop.shopName)"
			+ ",p.ngayTaoDon,p.tongTien,p.phiShip,p.diaChiId,p.trangThai,p.thanhToanId,p.accountId,p.tienTru,p.tienTruVoucherSan) "
			+ "from Order p where p.id=:orderId and p.shop.shopId=:shopId ")
	Optional<Order> getOrderById(@Param("orderId")Integer orderId,@Param("shopId")Integer shopId);
	
	@Query("select new com.API.model.Order(p.id,new com.API.model.Shop(p.shop.shopId,p.shop.shopName,p.shop.email)"
			+ ",p.ngayTaoDon,p.tongTien,p.phiShip,p.diaChiId,p.trangThai,p.thanhToanId,p.tienTru,p.tienTruVoucherSan) "
			+ "from Order p where p.id=:orderId and p.accountId.id=:shopId ")
	Optional<Order> getOrderByAccountId(@Param("orderId")Integer orderId,@Param("shopId")Integer shopId);

	@Query("select new com.API.model.OrderDetail(cd.soLuong,cd.giaBan,new com.API.model.ProductDetail"
			+ "(new com.API.model.Product(p.id,p.tenSanPham),pd.hinhAnh,pd.mauSac.tenMau,pd.kichThuoc.tenKichThuoc ))    "
			+ " from OrderDetail cd "
			+ "join ProductDetail pd on pd.id=cd.product.id "
			+ "join Product p on p.id=pd.product.Id"
			+ " where cd.order.id=:orderId")
	Optional<List<OrderDetail>> getItemInOrder(@Param("orderId")Integer orderId);
	
	
	@Query("select new com.API.model.Order(p.id,new com.API.model.Shop(p.shop.shopId,p.shop.shopName)"
			+ ",p.ngayTaoDon,p.tongTien,p.phiShip,p.diaChiId,p.trangThai,p.thanhToanId,p.diaChiId) "
			+ "from Order p order by p.ngayTaoDon")
	Optional<List<Order>> getOrderById();
	
	
//	caanf ghi chu laij nha ban hien
	@Query(value = "SELECT d.trangThai.id,COUNT(d.id) AS soLuong"
			+ " FROM Order d  WHERE d.shop.shopId=:shopId GROUP BY (d.trangThai.id)")
	List<Long[]> getCountOrderByStatus(@Param("shopId")Integer shopId);
	
	
	@Query(value = "	SELECT COUNT(id),d.trangThaiId AS trangthai,MONTH(d.ngayTaoDon) FROM donhang d\r\n"
			+ "	WHERE YEAR(d.ngayTaoDon)=YEAR(NOW()) AND d.trangThaiId IN(5,7,6)\r\n"
			+ "	GROUP BY MONTH( d.ngayTaoDon),d.trangThaiId  order BY (MONTH(d.ngayTaoDon)),d.trangThaiId",nativeQuery = true)
	List<Long[]> getCountOrderByMonth(@Param("shopId")Integer shopId);
	
	@Modifying
	@Transactional
	@Query("update Order d set d.trangThai=:trangThai where d.id in :ids and d.shop.shopId=:shopId and d.trangThai.id=2")
	Integer updateChuanBiHang(@Param("shopId")Integer shopId,@Param("ids")List<Integer> ids,@Param("trangThai")TrangThai trangThai);
	
	// ************ SALER
	
	@Query("select new com.API.model.Order(p.id,p.ngayTaoDon,p.diaChiId,p.thanhToanId,p.ghiChu,p.accountId.hoVaTen,p.accountId.soDienThoai)"
			+ "  from Order p where  p.shop.shopId=:shopId and p.trangThai.id=2 and (p.accountId.hoVaTen like :key or  concat('',p.id,'') like :key)")
	Page<Order> getAllOrderSaler(@Param("shopId")Integer shopId,Pageable p,@Param("key")String key);
	
	@Query("select new com.API.model.Order(p.id,p.ngayTaoDon,p.diaChiId,p.thanhToanId,p.ghiChu,p.accountId.hoVaTen,p.accountId.soDienThoai,p.accountId.id)"
			+ "  from Order p where  p.id in:ids and p.shop.shopId=:shopId")
	List<Order> getAllOrderSalerInList(@Param("shopId")Integer shopId,@Param("ids")List<Integer> ids);
	
	@Query("select new com.API.model.Order(p.id,p.ngayTaoDon,p.thanhToanId,p.ghiChu,p.accountId.hoVaTen,p.accountId.soDienThoai,p.lyDo,p.tongTien+p.phiShip-p.tienTru-p.tienTruVoucherSan,p.trangThai,p.nguoiHuy)"
			+ "  from Order p where  p.shop.shopId=:shopId and p.trangThai.id=:trangThaiId and (p.accountId.hoVaTen like :key or  concat('',p.id,'') like :key)")
	Page<Order> getAllOrderSaler(@Param("shopId")Integer shopId,Pageable p,@Param("key")String key,@Param("trangThaiId")Integer trangThaiId);
	
	@Query("SELECT new com.API.model.Order(p.id, p.ngayTaoDon, p.diaChiId, p.thanhToanId, p.ghiChu, p.accountId.hoVaTen, p.accountId.soDienThoai) " +
		       "FROM Order p " +
		       "WHERE p.shop.shopId = :shopId AND p.trangThai.id = 1 AND p.ngayTaoDon <= :tenHoursAgo " +
		       "AND (p.accountId.hoVaTen LIKE :key OR CONCAT('', p.id, '') LIKE :key) order by p.id DESC")
		Page<Order> getAllOrderSalerByCHoThanhToan
		(@Param("shopId") Integer shopId, Pageable pageable,@Param("key") 
		String key, @Param("tenHoursAgo") LocalDateTime tenHoursAgo);

//	(int id,Date ngayTao,DiaChi dc,ThanhToan thanhToan,String ghiChu,String shopName,Integer shopId,String lyDo,Integer daNhanHang)

	@Query("select new com.API.model.Order(p.id,p.ngayTaoDon,p.diaChiId,p.thanhToanId,p.ghiChu,p.shop.shopName,p.shop.shopId,p.lyDo,p.daNhanHang,p.trangThai)"
			+ "  from Order p where  p.accountId.id=:accountId and p.trangThai.id=:trangThaiId and (p.accountId.hoVaTen like :key or  concat('',p.id,'') like :key or p.shop.shopName like  :key)")
	Page<Order> getAllOrderUser(@Param("accountId")Integer shopId,Pageable p,@Param("key")String key,@Param("trangThaiId")Integer trangThaiId);
	
	
	// getAll 
	
	@Query("select new com.API.model.Order(p.id,p.ngayTaoDon,p.diaChiId,p.thanhToanId,p.ghiChu,p.shop.shopName,p.shop.shopId,p.lyDo,p.daNhanHang,p.trangThai)"
			+ "  from Order p where p.accountId.id=:accountId and  (p.accountId.hoVaTen like :key or  concat('',p.id,'') like :key or p.shop.shopName like  :key)")
	Page<Order> getAllOrderUser(@Param("accountId")Integer shopId,Pageable p,@Param("key")String key);
	
	
//	SELECT
//	FROM donhang d
//	WHERE d.ngayTaoDon < DATE_SUB(NOW(), INTERVAL 10 HOUR);
	
	
	
	
	@Query("select new com.API.model.OrderDetail(cd.id,cd.order.id,cd.soLuong,cd.giaBan,new com.API.model.ProductDetail"
			+ "(new com.API.model.Product(p.id,p.tenSanPham),pd.hinhAnh,pd.mauSac.tenMau,pd.kichThuoc.tenKichThuoc ),cd.trangThaiDanhGia)    "
			+ " from OrderDetail cd "
			+ "join ProductDetail pd on pd.id=cd.product.id "
			+ "join Product p on p.id=pd.product.Id"
			+ " where cd.order in :orderId")
	Optional<List<OrderDetail>> getItemInOrder(@Param("orderId")List<Order> orderId);
	
	
	
	// SƠN
		// Trang dasboard
		// hàm lấy tổng doanh thu hôm nay
		@Query(value = "SELECT COALESCE(SUM(d.tongTien), 0) FROM DonHang d JOIN TrangThai t ON d.trangThaiId = t.id WHERE DATE(d.ngayTaoDon) = CURRENT_DATE AND t.TenTrangThai = 'Giao hàng thành công'", nativeQuery = true)
		Double getTodayRevenue();

		// hàm lấy tổng lợi nhuận hôm nay
		@Query(value = "SELECT COALESCE(SUM((d.tongTien + COALESCE(d.tienTruVoucherShop, 0)) * 0.04), 0) FROM DonHang d JOIN TrangThai t ON d.trangThaiId = t.id WHERE DATE(d.ngayTaoDon) = CURRENT_DATE AND t.TenTrangThai = 'Giao hàng thành công'", nativeQuery = true)
		Double getTodayRevenuePercentage();

		// Hàm đếm số đơn hàng hôm nay
		@Query(value = "SELECT COUNT(*) FROM donhang d JOIN trangthai t ON d.trangThaiId = t.id WHERE DATE(d.ngayTaoDon) = CURRENT_DATE AND t.TenTrangThai = 'Giao hàng thành công'", nativeQuery = true)
		Long countCompletedOrdersToday();

		// Hàm đếm tổng số user
		@Query(value = "SELECT COUNT(*) FROM taikhoan WHERE trangThai = 'HoatDong' AND vaiTro = 'NguoiDung'", nativeQuery = true)
		Long countActiveAccounts();

		// Hàm đếm tổng số seller
		@Query(value = "SELECT COUNT(*) FROM taikhoan WHERE trangThai = 'HoatDong' AND vaiTro = 'NguoiBan'", nativeQuery = true)
		Long countActiveSeller();

		// Hàm đếm tổng sản pahmar
		@Query(value = "SELECT COUNT(*) FROM sanpham s JOIN cuahang c ON s.cuaHangId = c.id WHERE s.trangThai = 1 AND c.trangThai = 1", nativeQuery = true)
		Long countActiveProducts();

		// siown 5/12

		@Query(value = """
				    SELECT
				        COALESCE(SUM(CASE WHEN tt.TenTrangThai = 'Giao hàng thành công' THEN dh.tongTien ELSE 0 END), 0) AS tongGiaTriDonHangThanhCong,
				        COALESCE(SUM(CASE WHEN tt.TenTrangThai = 'Đã hủy' THEN dh.tongTien ELSE 0 END), 0) AS tongGiaTriDonHangHuy,
				        COUNT(CASE WHEN tt.TenTrangThai = 'Giao hàng thành công' THEN 1 ELSE NULL END) AS tongSoDonHangThanhCong,
				        COUNT(CASE WHEN tt.TenTrangThai = 'Đã hủy' THEN 1 ELSE NULL END) AS tongSoDonHangHuy
				    FROM
				        donhang dh
				    JOIN
				        trangthai tt ON dh.trangThaiId = tt.id
				    WHERE
				        dh.taiKhoanId = :taiKhoanId;
				""", nativeQuery = true)
		Object[] calculateOrderStats(@Param("taiKhoanId") Integer taiKhoanId);

		@Query(value = """
				SELECT
				             dh.id AS donHangId,
				             dh.tongTien,
				             dh.ngayTaoDon,
				             tt.TenTrangThai AS tenTrangThai,
				             ch.tenShop AS tenShop
				FROM
				    donhang dh
				JOIN
				    cuahang ch ON dh.shopId = ch.id
				JOIN
				             trangthai tt ON dh.trangThaiId = tt.id
				         WHERE
				             dh.taiKhoanId = :taiKhoanId
				         ORDER BY
				             dh.ngayTaoDon DESC
				         LIMIT :pageSize OFFSET :offset
				         """, nativeQuery = true)
		List<Object[]> findOrderDetailsByTaiKhoanIdWithPagination(@Param("taiKhoanId") Integer taiKhoanId,
				@Param("pageSize") int pageSize, @Param("offset") int offset);

		@Query(value = """
				SELECT COUNT(*)
				FROM donhang dh
				WHERE dh.taiKhoanId = :taiKhoanId
				""", nativeQuery = true)
		long countOrdersByTaiKhoanId(@Param("taiKhoanId") Integer taiKhoanId);
		
		//lấy doanh thu - lợi nhuận
		    
		 @Query(value = """
		            SELECT 
		                COALESCE(SUM(dh.tongTien + COALESCE(dh.tienTruVoucherShop, 0)), 0) AS doanhThuSan,
		                COALESCE(SUM((dh.tongTien + COALESCE(dh.tienTruVoucherShop, 0)) * 0.04), 0) AS loiNhuanSan
		            FROM 
		                donhang dh
		            JOIN 
		                trangthai tt ON dh.trangThaiId = tt.id
		            WHERE 
		                tt.tenTrangThai = 'Giao hàng thành công'
		                AND (
		                    -- Lọc theo ngày (mặc định)
		                    (:filterType = 'day' AND DATE(dh.ngayTaoDon) = CURDATE())
		                    
		                    -- Lọc theo tuần
		                    OR (:filterType = 'week' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE()) AND WEEK(dh.ngayTaoDon) = WEEK(CURDATE()))
		                    
		                    -- Lọc theo tháng
		                    OR (:filterType = 'month' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE()) AND MONTH(dh.ngayTaoDon) = MONTH(CURDATE()))
		                    
		                    -- Lọc theo năm
		                    OR (:filterType = 'year' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE()))
		                    
		                    -- Lọc theo khoảng thời gian tùy chỉnh
		                    OR (:filterType = 'custom' AND dh.ngayTaoDon BETWEEN :startDate AND :endDate)
		                )
		            """, nativeQuery = true)
		    List<Object[]> calculateRevenueAndProfit(
		        @Param("filterType") String filterType,
		        @Param("startDate") String startDate,
		        @Param("endDate") String endDate);
		    
		    
		    @Query(value = """
		    	    SELECT 
		    	        SUM(CASE WHEN tt.tenTrangThai = 'Giao hàng thành công' THEN 1 ELSE 0 END) AS soDonGiaoThanhCong,
		    	        SUM(CASE WHEN tt.tenTrangThai IN ('Đã hủy', 'Giao không thành công') THEN 1 ELSE 0 END) AS soDonHuyHoacGiaoKhongThanhCong,
		    	        SUM(CASE WHEN tt.tenTrangThai NOT IN ('Giao hàng thành công', 'Đã hủy', 'Giao không thành công') THEN 1 ELSE 0 END) AS soDonCacTrangThaiConLai
		    	    FROM 
		    	        donhang dh
		    	    JOIN 
		    	        trangthai tt ON dh.trangThaiId = tt.id
		    	    WHERE 
		    	        (
		    	            -- Lọc theo ngày (mặc định)
		    	            :filterType = 'day' AND DATE(dh.ngayTaoDon) = CURDATE()
		    	            
		    	            -- Lọc theo tuần
		    	            OR (:filterType = 'week' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE()) AND WEEK(dh.ngayTaoDon) = WEEK(CURDATE()))
		    	            
		    	            -- Lọc theo tháng
		    	            OR (:filterType = 'month' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE()) AND MONTH(dh.ngayTaoDon) = MONTH(CURDATE()))
		    	            
		    	            -- Lọc theo năm
		    	            OR (:filterType = 'year' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE()))
		    	            
		    	            -- Lọc theo khoảng thời gian tùy chỉnh
		    	            OR (:filterType = 'custom' AND dh.ngayTaoDon BETWEEN :startDate AND :endDate)
		    	        )
		    	""", nativeQuery = true)
		    	Object[] countOrderStatus(
		    	    @Param("filterType") String filterType, 
		    	    @Param("startDate") String startDate, 
		    	    @Param("endDate") String endDate
		    	);
		    
		    @Query(value = """
			        SELECT 
			            dh.id, 
			            ch.tenShop AS tenShop,          -- Tên cửa hàng
			            tk.tenTaiKhoan AS hoVaTen,          -- Tên người đặt hàng
			            dh.ngayTaoDon AS ngayTaoDon,    -- Ngày tạo đơn
			            dh.tongTien AS tongTien,        -- Tổng tiền
			            ts.TenTrangThai AS trangThaiTen -- Trạng thái đơn hàng
			        FROM 
			            donhang dh
			        JOIN 
			            taikhoan tk ON dh.taiKhoanId = tk.id -- Liên kết với tài khoản
			        JOIN 
			            cuahang ch ON ch.id = dh.shopId      -- Liên kết với cửa hàng
			        LEFT JOIN 
			            diachi dc ON dh.diaChiId = dc.id     -- Địa chỉ (có thể null)
			        JOIN 
			            trangthai ts ON dh.trangThaiId = ts.id -- Trạng thái
			        WHERE 
			            (:id IS NULL OR dh.id LIKE CONCAT('%', :id, '%')) -- Tìm kiếm theo ID
			            AND (:trangThai IS NULL OR dh.trangThaiId = :trangThai) -- Theo trạng thái
			            AND (:shopName IS NULL OR ch.tenShop LIKE CONCAT('%', :shopName, '%')) -- Tên shop
			            AND (:name IS NULL OR tk.tenTaiKhoan LIKE CONCAT('%', :name, '%')) -- Tên người đặt
			        ORDER BY 
			            dh.ngayTaoDon DESC
			        """, nativeQuery = true)
			Page<Object[]> FillterAllOrders(
			    @Param("id") String id,
			    @Param("trangThai") Integer trangThai,
			    @Param("shopName") String shopName,
			    @Param("name") String name,
			    Pageable pageable
			);
	
}
