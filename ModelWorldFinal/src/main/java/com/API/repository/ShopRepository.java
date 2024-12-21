package com.API.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.Shop;

import jakarta.transaction.Transactional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {

	// TẤN MỚI NHẤT

	@Query(value = "select p.id from Shop p where p.shopName like :shopName")
	Optional<Integer> getShopIdByShopName(@Param("shopName") String accountId);

	@Query(value = "select new com.API.model.Shop(p.shopId,p.shopName,p.hinhAnh,p.trangThai) from Shop p where p.account.id=:accountId")
	Optional<Shop> getInfoShopByAccountId(@Param("accountId") Integer accountId);

	//
	@Query(value = "CALL GetActiveShopsByAccountId(:accountId)", nativeQuery = true)
	Optional<Integer> findActiveShopsByAccountId(@Param("accountId") Integer accountId);

	@Query("select p from Shop p where p.shopId=:shopId and p.account.id=:accountId")
	Optional<Shop> getShopByShopIdAndAccountId(@Param("shopId") Integer shopId, @Param("accountId") Integer accountId);

	// theem mới - NAM
	@Query(value = """
			SELECT
			    ch.id AS cuaHangId,
			    ch.hinhAnh,
			    ch.tenShop,
			    COUNT(sp.id) AS soLuongSanPham,
			    dc.toanBoDiaChi AS diaChiShop,
			    tk.soDienThoai AS soDienThoaiShop,
			    CASE
			        WHEN TIMESTAMPDIFF(YEAR, ch.ngayDangKy, NOW()) > 0 THEN CONCAT(TIMESTAMPDIFF(YEAR, ch.ngayDangKy, NOW()), ' năm trước')
			        WHEN TIMESTAMPDIFF(MONTH, ch.ngayDangKy, NOW()) > 0 THEN CONCAT(TIMESTAMPDIFF(MONTH, ch.ngayDangKy, NOW()), ' tháng trước')
			        WHEN TIMESTAMPDIFF(WEEK, ch.ngayDangKy, NOW()) > 0 THEN CONCAT(TIMESTAMPDIFF(WEEK, ch.ngayDangKy, NOW()), ' tuần trước')
			        WHEN TIMESTAMPDIFF(DAY, ch.ngayDangKy, NOW()) > 0 THEN CONCAT(TIMESTAMPDIFF(DAY, ch.ngayDangKy, NOW()), ' ngày trước')
			        ELSE 'Hôm nay'
			    END AS thoiGianTaoShop
			FROM
			    cuahang ch
			LEFT JOIN
			    sanpham sp ON ch.id = sp.cuaHangId
			LEFT JOIN
			    diachi dc ON ch.taiKhoan_Id = dc.taiKhoanId
			LEFT JOIN
			    taikhoan tk ON ch.taiKhoan_Id = tk.id
			WHERE
			    ch.id = :shopId
			GROUP BY
			    ch.id, ch.hinhAnh, ch.tenShop, dc.toanBoDiaChi, tk.soDienThoai, ch.ngayDangKy
			""", nativeQuery = true)
	List<Object[]> getShopById(@Param("shopId") int shopId);

	@Query(value = "SELECT id, giaTriGiam, donToiThieu, DATE(thoiGianKetThuc) as thoiGianKetThuc, loaiVoucher FROM voucherscuahang WHERE cuaHangid = :shopId", nativeQuery = true)
	List<Object[]> getShopVoucherById(@Param("shopId") int shopId);

	@Query(value = """
			     SELECT
			         sp.id AS sanPhamId,
			         sp.tenSanPham AS tenSanPham,
			         sp.hinhAnh AS hinhAnhSanPham,
			         MIN(ctsp.giaBan) AS giaSanPham,
			         ROUND(MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)), 0) AS giaSauKhuyenMai,
			         km.giaTriKhuyenMai AS phanTramKhuyenMai,
			         sp.soLuongDaBan AS soLuongDaBan
			     FROM
			         sanpham sp
			     JOIN
			         chitietsanpham ctsp ON sp.id = ctsp.sanPhamId
			     LEFT JOIN
			         chitietkhuyenmai ctkm ON sp.id = ctkm.productId
			     LEFT JOIN
			         khuyenmai km ON ctkm.khuyenMaiId = km.id
			     LEFT JOIN
			         viewedcategories vc ON sp.theLoaiId = vc.category_id
			     LEFT JOIN
			searchhistory sh ON sh.user_id = :userId
			     WHERE
			         sp.trangThai = 1
			         AND ctsp.soLuong > 0
			         AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW())
			         AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW())
			         AND sp.cuaHangId = :shopId
			         AND
			             (
			                 NOT EXISTS (SELECT 1 FROM viewedcategories WHERE user_id = :userId)
			                 OR sp.theLoaiId IN (SELECT category_id FROM viewedcategories WHERE user_id = :userId)
			                 OR sh.search_term IS NULL OR sp.tenSanPham LIKE CONCAT('%', sh.search_term, '%')
			             )
			     GROUP BY
			         sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan
			     ORDER BY RAND()
			      LIMIT 6
			     """, nativeQuery = true)
	List<Object[]> getSanPham(@Param("shopId") int shopId, @Param("userId") Integer userId);

	@Query(value = """
			SELECT
			    sp.id AS sanPhamId,
			    sp.tenSanPham AS tenSanPham,
			    sp.hinhAnh AS hinhAnhSanPham,
			    MIN(ctsp.giaBan) AS giaSanPham,
			    MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai,
			    km.giaTriKhuyenMai AS phanTramKhuyenMai,
			    sp.soLuongDaBan AS soLuongDaBan
			FROM
			    sanpham sp
			JOIN
			    chitietsanpham ctsp ON sp.id = ctsp.sanPhamId
			LEFT JOIN
			    chitietkhuyenmai ctkm ON sp.id = ctkm.productId
			LEFT JOIN
			    khuyenmai km ON ctkm.khuyenMaiId = km.id
			LEFT JOIN
			    viewedcategories vc ON sp.theLoaiId = vc.category_id
			WHERE
			    sp.trangThai = 1
			    AND ctsp.soLuong > 0
			    AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW())
			    AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW())
			    AND sp.cuaHangId = :shopId
			GROUP BY
			    sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan
			ORDER BY RAND()

			""", nativeQuery = true)
	List<Object[]> getSanPham(@Param("shopId") int shopId);

	@Query(value = """
			SELECT
			     sp.id AS sanPhamId,
			     sp.tenSanPham AS tenSanPham,
			     sp.hinhAnh AS hinhAnhSanPham,
			     MIN(ctsp.giaBan) AS giaSanPham,
			     MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai,
			     km.giaTriKhuyenMai AS phanTramKhuyenMai,
			     sp.soLuongDaBan AS soLuongDaBan
			 FROM
			     sanpham sp
			 JOIN
			     chitietsanpham ctsp ON sp.id = ctsp.sanPhamId
			 LEFT JOIN
			     chitietkhuyenmai ctkm ON sp.id = ctkm.productId
			 LEFT JOIN
			     khuyenmai km ON ctkm.khuyenMaiId = km.id
			 WHERE
			     sp.trangThai = 1
			     AND ctsp.soLuong > 0
			     AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW())
			     AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW())
			     AND sp.cuaHangId = :shopId
			 GROUP BY
			     sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan
			 ORDER BY
			     sanPhamId DESC;
			""", nativeQuery = true)
	List<Object[]> getSanPhamDesc(@Param("shopId") int shopId);

	@Query(value = "SELECT " + "sp.id AS sanPhamId, " + "sp.tenSanPham AS tenSanPham, "
			+ "sp.hinhAnh AS hinhAnhSanPham, " + "MIN(ctsp.giaBan) AS giaSanPham, "
			+ "MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai, "
			+ "km.giaTriKhuyenMai AS phanTramKhuyenMai, " + "sp.soLuongDaBan AS soLuongDaBan " + "FROM sanpham sp "
			+ "JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId "
			+ "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId "
			+ "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id " + "WHERE sp.trangThai = 1 "
			+ "AND ctsp.soLuong > 0 " + // Sản phẩm phải có biến thể
			"AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW()) " + // Khuyến mãi đang có hiệu lực
			"AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "AND sp.cuaHangId = :id " + // Tên sản phẩm chứa
																									// từ :name
			"GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan", nativeQuery = true)
	Page<Object[]> findProductByName(int id, Pageable pageable);

	@Query(value = "WITH RECURSIVE subcategories AS ( " + "    SELECT id " + "    FROM theloai "
			+ "    WHERE (COALESCE(:theLoaiIds) IS NULL OR parent_Id IN (:theLoaiIds)) " + "    UNION ALL "
			+ "    SELECT t.id " + "    FROM theloai t " + "    JOIN subcategories s ON s.id = t.parent_Id " + ") "
			+ "SELECT " + "    sp.id AS sanPhamId, " + "    sp.tenSanPham AS tenSanPham, "
			+ "    sp.hinhAnh AS hinhAnhSanPham, " + "    MIN(ctsp.giaBan) AS giaSanPham, "
			+ "    MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai, "
			+ "    km.giaTriKhuyenMai AS phanTramKhuyenMai, " + "    sp.soLuongDaBan AS soLuongDaBan "
			+ "FROM sanpham sp " + "JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId "
			+ "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId "
			+ "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id " + "WHERE sp.trangThai = 1 "
			+ "  AND ctsp.soLuong > 0 " + "  AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW()) "
			+ "  AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "  AND sp.cuaHangId = :id "
			+ "  AND (COALESCE(:theLoaiIds) IS NULL OR sp.theLoaiId IN (:theLoaiIds) OR sp.theLoaiId IN (SELECT id FROM subcategories)) "
			+ "  AND (COALESCE(:kichThuocIds) IS NULL OR ctsp.kichThuocId IN :kichThuocIds) "
			+ "  AND (COALESCE(:thuongHieuIds) IS NULL OR sp.thuongHieuId IN :thuongHieuIds) "
			+ "  AND (COALESCE(:mauSacIds) IS NULL OR ctsp.mauSacId IN :mauSacIds) "
			+ "  AND (COALESCE(:giaTu) IS NULL OR ctsp.giaBan >= :giaTu) "
			+ "  AND (COALESCE(:giaDen) IS NULL OR ctsp.giaBan <= :giaDen) "
			+ "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan", countQuery = "WITH RECURSIVE subcategories AS ( "
					+ "    SELECT id " + "    FROM theloai "
					+ "    WHERE (COALESCE(:theLoaiIds) IS NULL OR parent_Id IN (:theLoaiIds)) " + "    UNION ALL "
					+ "    SELECT t.id " + "    FROM theloai t " + "    JOIN subcategories s ON s.id = t.parent_Id "
					+ ") " + "SELECT " + "    sp.id AS sanPhamId, " + "    sp.tenSanPham AS tenSanPham, "
					+ "    sp.hinhAnh AS hinhAnhSanPham, " + "    MIN(ctsp.giaBan) AS giaSanPham, "
					+ "    MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai, "
					+ "    km.giaTriKhuyenMai AS phanTramKhuyenMai, " + "    sp.soLuongDaBan AS soLuongDaBan "
					+ "FROM sanpham sp " + "JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId "
					+ "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId "
					+ "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id " + "WHERE sp.trangThai = 1 "
					+ "  AND ctsp.soLuong > 0 " + "  AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW()) "
					+ "  AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "  AND sp.cuaHangId = :id "
					+ "  AND (COALESCE(:theLoaiIds) IS NULL OR sp.theLoaiId IN (:theLoaiIds) OR sp.theLoaiId IN (SELECT id FROM subcategories)) "
					+ "  AND (COALESCE(:kichThuocIds) IS NULL OR ctsp.kichThuocId IN :kichThuocIds) "
					+ "  AND (COALESCE(:thuongHieuIds) IS NULL OR sp.thuongHieuId IN :thuongHieuIds) "
					+ "  AND (COALESCE(:mauSacIds) IS NULL OR ctsp.mauSacId IN :mauSacIds) "
					+ "  AND (COALESCE(:giaTu) IS NULL OR ctsp.giaBan >= :giaTu) "
					+ "  AND (COALESCE(:giaDen) IS NULL OR ctsp.giaBan <= :giaDen) "
					+ "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan", nativeQuery = true)
	Page<Object[]> findSanPhamWithFilters(@Param("id") int id, @Param("theLoaiIds") List<Integer> theLoaiIds,
			@Param("kichThuocIds") List<Integer> kichThuocIds, @Param("thuongHieuIds") List<Integer> thuongHieuIds,
			@Param("mauSacIds") List<Integer> mauSacIds, @Param("giaTu") Double giaTu, @Param("giaDen") Double giaDen,
			Pageable pageable);

	// else

	@Query("select s.shopId from Shop s where s.email=:email")
	Optional<Integer> getShopIdByEmail(@Param("email") String id);

	@Query(value = "select p from Shop p where p.shopId=:shopId and p.account.id=:accountId")
	Optional<Shop> getShopByIdAndAccountId(@Param("shopId") Integer shopId, @Param("accountId") Integer accountId);

	@Query("select s.shopId from Shop s where s.account.id=:accountId")
	Optional<Integer> getShopByAccountId(@Param("accountId") Integer id);

	@Query("select p.shopId from Shop p where p.id=:shopId and p.account.id=:accountId and p.trangThai=1")
	Optional<Integer> getIdShopS(@Param("shopId") Integer shopId, @Param("accountId") Integer accountId);

//			    	cũ 
	@Query(value = "CALL GetAllShopInfoWithRevenueAndOrderCount()", nativeQuery = true)
	List<Object[]> findAllShops();

	@Query(value = "SELECT " + "c.id, c.tenShop, c.hoVaTen, c.email, c.hinhAnh, c.ngayDangKy, "
			+ "c.hinhChupThe, c.anhDangCam, c.maSoThue, c.giayPhepKinhDoanh, c.loaiHinhKinhDoanh, "
			+ "c.xacMinh, c.trangThai, d.soDienThoai, d.toanBoDiaChi , c.tenCongTy " + "FROM cuahang c "
			+ "JOIN diachi d ON c.id = d.cuaHangId " + "WHERE c.id = :shopId", nativeQuery = true)
	Optional<Object[]> findShopDetailsById(@Param("shopId") Integer shopId);

	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM donhang d WHERE d.shopId = :id) AS tongSoDonHang, "
			+ "(SELECT SUM(d.tongTien) FROM donhang d JOIN trangthai t ON d.trangThaiId = t.id WHERE d.shopId = :id AND t.TenTrangThai = 'Giao hàng thành công') AS tongDoanhThu, "
			+ "(SELECT COUNT(*) FROM sanpham s WHERE s.cuaHangId = :id) AS tongSoSanPham ", nativeQuery = true)
	List<Object[]> findStatisticsByShopIdAndCuaHangId(@Param("id") Integer id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE cuahang SET trangThai = :trangThai WHERE id = :id", nativeQuery = true)
	int updateTrangThaiById(@Param("id") Integer id, @Param("trangThai") Integer trangThai);

	@Query(value = "SELECT " + "ch.id AS shopId, " + "ch.tenShop, " + "ch.hoVaTen, " + "ch.hinhAnh, "
			+ "ch.ngayDangKy, " + "ch.trangThai, " + "dc.soDienThoai, " + "COUNT(dh.id) AS totalOrders, "
			+ "SUM(dh.tongTien) AS totalRevenue " + "FROM cuahang ch " + "JOIN diachi dc ON ch.id = dc.cuaHangId "
			+ "LEFT JOIN donhang dh ON dh.shopId = ch.id "
			+ "WHERE (COALESCE(:trangThai) IS NULL OR ch.trangThai = :trangThai) " + "AND ch.tenShop LIKE %:name% "
			+ "GROUP BY ch.id, dc.soDienThoai", countQuery = "SELECT COUNT(*) FROM (" + "SELECT ch.id "
					+ "FROM cuahang ch " + "JOIN diachi dc ON ch.id = dc.cuaHangId "
					+ "LEFT JOIN donhang dh ON dh.shopId = ch.id "
					+ "WHERE (COALESCE(:trangThai) IS NULL OR ch.trangThai = :trangThai) "
					+ "AND ch.tenShop LIKE %:name% " + "GROUP BY ch.id" + ") AS subquery", nativeQuery = true)
	Page<Object[]> getAdminShopFillter(@Param("trangThai") Integer trangThai, @Param("name") String name,
			Pageable pageable);
	
	@Query(value = """
			SELECT 
			    ch.id AS cuaHangId,
			    ch.tenShop AS TenCuaHang,
			    ch.hinhAnh AS AnhDaiDien,
			    ch.hoVaTen AS TenChuCuaHang,
			    dc.soDienThoai AS SoDienThoai,
			    SUM(dh.tongTien + IFNULL(vs.giaTriGiam, 0) - (dh.tongTien * 0.04)) AS DoanhThu,
			    COUNT(dh.id) AS SoLuongDonHang,
			    ch.ngayDangKy AS NgayDangKy
			FROM 
			    cuahang ch
			INNER JOIN 
			    donhang dh ON ch.id = dh.shopId
			LEFT JOIN 
			    vouchersan vs ON dh.voucherSanId = vs.id
			INNER JOIN 
			    diachi dc ON dc.cuaHangId = ch.id
			WHERE 
			    dh.trangThaiId = (SELECT id FROM trangthai WHERE tenTrangThai = 'Giao hàng thành công')
			    AND ch.trangThai = 1
			    AND (
			        :filterType = 'DAY' AND DATE(dh.ngayTaoDon) = CURDATE()
			        OR :filterType = 'WEEK' AND YEARWEEK(dh.ngayTaoDon, 1) = YEARWEEK(CURDATE(), 1)
			        OR :filterType = 'MONTH' AND MONTH(dh.ngayTaoDon) = MONTH(CURDATE()) AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE())
			        OR :filterType = 'YEAR' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE())
			    )
			GROUP BY 
			    ch.id, ch.tenShop, ch.hinhAnh, ch.hoVaTen, dc.soDienThoai
			ORDER BY 
			    DoanhThu DESC
			""", nativeQuery = true)
			Page<Object[]> getTop30ShopBestSell(@Param("filterType") String filterType, Pageable pageable);
			
			
}

//package com.API.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.API.model.Shop;
//import com.API.service.saler.ShopFilter;
//
//import jakarta.transaction.Transactional;
//@Repository
//public interface ShopRepository extends JpaRepository<Shop, Integer> {
//	@Query(value = "CALL GetActiveShopsByAccountId(:accountId)", nativeQuery = true)
//    Optional<Integer> findActiveShopsByAccountId(@Param("accountId") Integer accountId);
//	
//	@Query(value = "select p from Shop p where p.shopId=:shopId and p.account.id=:accountId")
//    Optional<Shop> getShopByIdAndAccountId(@Param("shopId") Integer shopId,@Param("accountId") Integer accountId);
//	
//	@Query("select p.shopId from Shop p where p.id=:shopId and p.account.id=:accountId and p.trangThai=1")
//	Optional<Integer> getIdShopS(@Param("shopId")Integer shopId,@Param("accountId")Integer accountId);
//	
//	@Query("select s.shopId from Shop s where s.account.id=:accountId")
//	Optional<Integer> getShopByAccountId(@Param("accountId")Integer id );
//	
//	@Query("select s.shopId from Shop s where s.email=:email")
//	Optional<Integer> getShopIdByEmail(@Param("email")String id );
//	
//	
//	
//	
//	
//	// theem mới - NAM
//	@Query(value = """
//	        SELECT 
//	            ch.id AS cuaHangId,
//	            ch.hinhAnh,
//	            ch.tenShop,
//	            COUNT(sp.id) AS soLuongSanPham,
//	            dc.toanBoDiaChi AS diaChiShop,
//	            tk.soDienThoai AS soDienThoaiShop,
//	            CONCAT(TIMESTAMPDIFF(YEAR, ch.ngayDangKy, NOW()), ' năm trước') AS thoiGianTaoShop
//	        FROM 
//	            cuahang ch
//	        LEFT JOIN 
//	            sanpham sp ON ch.id = sp.cuaHangId
//	        LEFT JOIN 
//	            diachi dc ON ch.taiKhoan_Id = dc.taiKhoanId AND dc.isShop =:shopId
//	        LEFT JOIN 
//	            taikhoan tk ON ch.taiKhoan_Id = tk.id
//	        WHERE 
//	            ch.id = :shopId
//	        GROUP BY 
//	            ch.id, ch.hinhAnh, ch.tenShop, dc.toanBoDiaChi, tk.soDienThoai, ch.ngayDangKy
//	        """, nativeQuery = true)
//	    List<Object[]> getShopById(@Param("shopId") int shopId);
//    
//	    
//	    @Query(value = "SELECT id, giaTriGiam, donToiThieu, DATE(thoiGianKetThuc) as thoiGianKetThuc FROM voucherscuahang WHERE cuaHangid = :shopId", nativeQuery = true)
//	    List<Object[]> getShopVoucherById(@Param("shopId") int shopId);
//	    
//	    @Query(value = """
//	    	    SELECT 
//	    	        sp.id AS sanPhamId,
//	    	        sp.hinhAnh AS hinhAnhSanPham,
//	    	        sp.tenSanPham AS tenSanPham,
//	    	        MIN(ctsp.giaBan) AS giaThapNhat,
//	    	        sp.soLuongDaBan AS soLuongDaBan
//	    	    FROM 
//	    	        sanpham sp
//	    	    INNER JOIN 
//	    	        chitietsanpham ctsp 
//	    	    ON 
//	    	        sp.id = ctsp.sanPhamId
//	    	    WHERE 
//	    	        sp.cuaHangId = :shopId
//	    	    GROUP BY 
//	    	        sp.id, sp.hinhAnh, sp.soLuongDaBan
//	    	    LIMIT 6 
//	    	    """, nativeQuery = true)
//	    	List<Object[]> getSanPham(@Param("shopId") int shopId);
//
//	    	
//	    	@Query(value = """
//	    		    SELECT 
//	    		        sp.id AS sanPhamId,
//	    		        sp.hinhAnh AS hinhAnhSanPham,
//	    		        sp.tenSanPham AS tenSanPham,
//	    		        MIN(ctsp.giaBan) AS giaThapNhat,
//	    		        sp.soLuongDaBan AS soLuongDaBan
//	    		    FROM 
//	    		        sanpham sp
//	    		    INNER JOIN 
//	    		        chitietsanpham ctsp 
//	    		    ON 
//	    		        sp.id = ctsp.sanPhamId
//	    		    WHERE 
//	    		        sp.cuaHangId = :shopId
//	    		    GROUP BY 
//	    		        sp.id, sp.hinhAnh, sp.soLuongDaBan
//	    		    ORDER BY 
//	    		        sp.id DESC -- Sắp xếp theo ID giảm dần
//	    		    LIMIT 6 -- Chỉ lấy 6 sản phẩm
//	    		    """, nativeQuery = true)
//	    		List<Object[]> getSanPhamDesc(@Param("shopId") int shopId);
//	    		@Query(value = "CALL GetActiveShopsByAccountId(:accountId)", nativeQuery = true)
//	    		Optional<Integer> findActiveShopsByAccountIdS(@Param("accountId") Integer accountId);
//
//	    		// Lấy toàn bộ seller
//	    		Page<Shop> findAll(Pageable pageable);
//
//	    		// Lấy toàn bộ seller và doanh thu
//	    		@Query(value = "CALL GetAllShopInfoWithRevenueAndOrderCount()", nativeQuery = true)
//	    		List<Object[]> findAllShops();
//
//	    		// lấy thông tin seller theo id
//	    		@Query(value = "SELECT " + "c.id, c.tenShop, c.hoVaTen, c.email, c.hinhAnh, c.ngayDangKy, "
//	    				+ "c.hinhChupThe, c.anhDangCam, c.maSoThue, c.giayPhepKinhDoanh, c.loaiHinhKinhDoanh, "
//	    				+ "c.xacMinh, c.trangThai, d.soDienThoai, d.toanBoDiaChi , c.tenCongTy " + "FROM cuahang c "
//	    				+ "JOIN diachi d ON c.id = d.cuaHangId "
//	    				+ "WHERE c.id = :shopId", nativeQuery = true)
//	    		Optional<Object[]> findShopDetailsById(@Param("shopId") Integer shopId);
//
//	    		// Lấy thông tin doanh thu, hóa đơn, sản phẩm, só sao
//	    		@Query(value = "SELECT " + "(SELECT COUNT(*) FROM donhang d WHERE d.shopId = :id) AS tongSoDonHang, "
//	    				+ "(SELECT SUM(d.tongTien) FROM donhang d JOIN trangthai t ON d.trangThaiId = t.id WHERE d.shopId = :id AND t.TenTrangThai = 'Giao hàng thành công') AS tongDoanhThu, "
//	    				+ "(SELECT COUNT(*) FROM sanpham s WHERE s.cuaHangId = :id) AS tongSoSanPham ", nativeQuery = true)
//	    		List<Object[]> findStatisticsByShopIdAndCuaHangId(@Param("id") Integer id);
//
//	    		// Cập nhật trạng thái shop
//	    		@Modifying
//	    		@Transactional
//	    		@Query(value = "UPDATE cuahang SET trangThai = :trangThai WHERE id = :id", nativeQuery = true)
//	    		int updateTrangThaiById(@Param("id") Integer id, @Param("trangThai") Integer trangThai);
//	    		
//	    		
//	    		
//	    		
//	    		// NAM 5/12
//	
//}
