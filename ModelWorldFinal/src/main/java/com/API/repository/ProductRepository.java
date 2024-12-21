package com.API.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.ChiTietKhuyenMai;
import com.API.model.KhuyenMai;
import com.API.model.Product;
import com.API.model.ProductDetail;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	// MỚI

	// lấy các sabr ohaamr không thuộc live.

	@Query("SELECT new com.API.model.Product(p.id, p.tenSanPham, p.hinhAnh, p.trangThai) " + "FROM Product p "
			+ "WHERE p.shop.id = :id and p.id not in (select v.product.id from Live_Product v "
			+ "where v.live.startTime is not null and v.live.endTime is null ) "
			+ " and p.tenSanPham like :key and p.trangThai=1")
	org.springframework.data.domain.Page<Product> getProductNotInlive(@Param("id") Integer id, // tham số cần phải có
																								// khi gọi API
			Pageable p, @Param("key") String key);

	@Query(value = """
			    SELECT p.*, pd.giaBan
			    FROM sanpham p
			    LEFT JOIN chitietsanpham pd ON p.id = pd.sanphamid
			    WHERE p.trangThai = :trangThai
			      AND pd.GiaBan = (
			          SELECT MIN(pd2.GiaBan)
			          FROM chitietsanpham pd2
			          WHERE pd2.sanphamid = p.id
			      )
			    ORDER BY p.id DESC
			""", nativeQuery = true)
	Page<Object[]> findAllByTrangThaiOrderByIdDesc(@Param("trangThai") Integer trangThai, Pageable pageable);

	@Query(value = """
				    	 SELECT p.*, pd.GiaBan
			FROM sanpham p
			LEFT JOIN (
			    SELECT pd1.*
			    FROM chitietsanpham pd1
			    JOIN (
			        SELECT sanphamid, MIN(GiaBan) AS minPrice
			        FROM chitietsanpham
			        GROUP BY sanphamid
			    ) pd2 ON pd1.sanphamid = pd2.sanphamid
			    AND pd1.GiaBan = pd2.minPrice
			) pd ON p.id = pd.sanphamid
			LEFT JOIN theloai t ON p.theloaiid = t.id
			WHERE (p.trangThai = :trangThai)
			OR (:searchTerm IS NULL OR p.TenSanPham LIKE CONCAT('%', :searchTerm, '%'))
			OR (:categoryIds IS NULL OR (p.theloaiid IN :categoryIds AND t.TrangThai = 1))
			ORDER BY p.id DESC

				    	""", nativeQuery = true)
	Page<Object[]> productLoginIn(@Param("trangThai") Integer trangThai, @Param("searchTerm") List<String> searchTerms,
			@Param("categoryIds") List<Integer> categoryIds, Pageable pageable);

	@Modifying
	@Transactional
	@Query("delete from ProductImage p where p.id=:id and p.product.id=:productId")
	Integer deleteProductImageByIdAndProductId(@Param("id") Integer id, @Param("productId") Integer productId);

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
			    sp.trangThai = 'DangBan'
			    AND ctsp.soLuong > 0
			    AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW())
			    AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW())
			GROUP BY
			    sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan
			""", nativeQuery = true)
	List<Object[]> getProductHomes();

	@Query("SELECT COUNT(p.id) FROM Product p WHERE p.shop.shopId=:shopId")
	Integer countProductInShopByShopid(@Param("shopId") Integer shopId);

	@Query("SELECT COUNT(s.id) FROM DanhGiaSanPham s WHERE s.orderDetail.id IN (\r\n"
			+ "        		SELECT p.id FROM OrderDetail p join Order  d ON p.order.id=d.id WHERE d.shop.shopId=:shopId\r\n"
			+ "        		)")
	Integer countRatingInShopByShopid(@Param("shopId") Integer shopId);

	@Query(value = """
			     SELECT
			         sp.id AS sanPhamId,
			         sp.tenSanPham AS tenSanPham,
			         sp.hinhAnh AS hinhAnhSanPham,
			         MIN(ctsp.giaBan) AS giaSanPham,
			         ROUND(MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)), 0) AS giaSauKhuyenMai,
			         km.giaTriKhuyenMai AS phanTramKhuyenMai,
			         sp.soLuongDaBan AS soLuongDaBan,
			         sp.theLoaiId AS TheLoai
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
			         AND
			         (
			             -- Kiểm tra nếu không có category_id nào cho user_id, lấy tất cả sản phẩm
			             NOT EXISTS (SELECT 1 FROM viewedcategories WHERE user_id = :userId)
			             OR sp.theLoaiId IN (SELECT category_id FROM viewedcategories WHERE user_id = :userId)
			             OR sh.search_term IS NULL OR sp.tenSanPham LIKE CONCAT('%', sh.search_term, '%')
			         )

			     GROUP BY
			         sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan
			     ORDER BY RAND()
			     """, nativeQuery = true)
	List<Object[]> getProductHomeLogin(@Param("userId") Integer userId);

	@Query(value = """
			SELECT
			   sp.id AS sanPhamId,
			   sp.tenSanPham AS tenSanPham,
			   sp.hinhAnh AS hinhAnhSanPham,
			   MIN(ctsp.giaBan) AS giaSanPham,
			   MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai,
			   km.giaTriKhuyenMai AS phanTramKhuyenMai,
			   sp.soLuongDaBan AS soLuongDaBan,
			   sp.theLoaiId AS theLoai,
			   COUNT(spyt.sanPham_id) AS soLuongYeuThich
			FROM
			   sanphamyeuthich spyt
			JOIN
			   sanpham sp ON sp.id = spyt.sanPham_id
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
			GROUP BY
			   sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan, sp.theLoaiId
			ORDER BY
			   soLuongYeuThich DESC
			LIMIT 60
			""", nativeQuery = true)
	List<Object[]> get50FavoriteProduct();

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
			+ "  AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "  AND (km.giaTriKhuyenMai > 0) "
			+ "  AND (COALESCE(:theLoaiIds) IS NULL OR sp.theLoaiId IN (:theLoaiIds) OR sp.theLoaiId IN (SELECT id FROM subcategories)) "
			+ "  AND (COALESCE(:kichThuocIds) IS NULL OR ctsp.kichThuocId IN :kichThuocIds) "
			+ "  AND (COALESCE(:thuongHieuIds) IS NULL OR sp.thuongHieuId IN :thuongHieuIds) "
			+ "  AND (COALESCE(:mauSacIds) IS NULL OR ctsp.mauSacId IN :mauSacIds) "
			+ "  AND (COALESCE(:giaTu) IS NULL OR ctsp.giaBan >= :giaTu) "
			+ "  AND (COALESCE(:giaDen) IS NULL OR ctsp.giaBan <= :giaDen) "
			+ "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan "
			+ "ORDER BY sp.id DESC", countQuery = "WITH RECURSIVE subcategories AS ( " + "    SELECT id "
					+ "    FROM theloai " + "    WHERE (COALESCE(:theLoaiIds) IS NULL OR parent_Id IN (:theLoaiIds)) "
					+ "    UNION ALL " + "    SELECT t.id " + "    FROM theloai t "
					+ "    JOIN subcategories s ON s.id = t.parent_Id " + ") " + "SELECT " + "    sp.id AS sanPhamId, "
					+ "    sp.tenSanPham AS tenSanPham, " + "    sp.hinhAnh AS hinhAnhSanPham, "
					+ "    MIN(ctsp.giaBan) AS giaSanPham, "
					+ "    MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai, "
					+ "    km.giaTriKhuyenMai AS phanTramKhuyenMai, " + "    sp.soLuongDaBan AS soLuongDaBan "
					+ "FROM sanpham sp " + "JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId "
					+ "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId "
					+ "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id " + "WHERE sp.trangThai = 1 "
					+ "  AND ctsp.soLuong > 0 " + "  AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW()) "
					+ "  AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "  AND sp.tenSanPham LIKE %:name% "
					+ "  AND (COALESCE(:theLoaiIds) IS NULL OR sp.theLoaiId IN (:theLoaiIds) OR sp.theLoaiId IN (SELECT id FROM subcategories)) "
					+ "  AND (COALESCE(:kichThuocIds) IS NULL OR ctsp.kichThuocId IN :kichThuocIds) "
					+ "  AND (COALESCE(:thuongHieuIds) IS NULL OR sp.thuongHieuId IN :thuongHieuIds) "
					+ "  AND (COALESCE(:mauSacIds) IS NULL OR ctsp.mauSacId IN :mauSacIds) "
					+ "  AND (COALESCE(:giaTu) IS NULL OR ctsp.giaBan >= :giaTu) "
					+ "  AND (COALESCE(:giaDen) IS NULL OR ctsp.giaBan <= :giaDen) "
					+ "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan", nativeQuery = true)
	Page<Object[]> getAndFindPromotionalProducts(@Param("theLoaiIds") List<Integer> theLoaiIds,
			@Param("kichThuocIds") List<Integer> kichThuocIds, @Param("thuongHieuIds") List<Integer> thuongHieuIds,
			@Param("mauSacIds") List<Integer> mauSacIds, @Param("giaTu") Double giaTu, @Param("giaDen") Double giaDen,
			Pageable pageable);

	@Query(value = """
			SELECT
			    sp.id AS sanPhamId,
			    sp.tenSanPham AS tenSanPham,
			    sp.hinhAnh AS hinhAnhSanPham,
			    MIN(ctsp.giaBan) AS giaSanPham,
			    ROUND(MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)), 0) AS giaSauKhuyenMai,
			    km.giaTriKhuyenMai AS phanTramKhuyenMai,
			    sp.soLuongDaBan AS soLuongDaBan,
			    sp.theLoaiId AS TheLoai
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
			    AND km.giaTriKhuyenMai > 0 -- Chỉ lấy sản phẩm có khuyến mãi
			GROUP BY
			    sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan

			""", nativeQuery = true)
	Page<Object[]> getPromotionalProducts(Pageable pageable);

	// Khuyến mãi
//	(Integer id, Integer productId, String productName, Object  minPrice,Object maxPrice, Object soLuong)

	@Query(value = "SELECT p.sanPhamId,min(p.giaBan),max(p.giaBan),sum(soLuong) FROM chitietsanpham p "
			+ " WHERE p.sanPhamId IN:l group by(p.sanPhamId)", nativeQuery = true)
	List<Object[]> getProductDetailInListId(@Param("l") List<Integer> l);

//		Integer id,Integer soLuong,Float giaBan,String tenMau,String tenKichThuoc,Integer productId
	@Query(value = "SELECT new com.API.model.ProductDetail(p.id,p.soLuong,p.giaBan,p.mauSac.tenMau,p.kichThuoc.tenKichThuoc,p.product.id)"
			+ " FROM ProductDetail p " + " WHERE p.product.id IN:l")
	List<ProductDetail> getProductDetailInListIdVersion(@Param("l") List<Integer> l);
	// kết thuc skhuyeens ãi

	@Query("SELECT new com.API.model.Product(p.id) FROM Product p WHERE p.id = :id AND p.shop.id = :shopId")
	Optional<Product> getProductByIdAndShopId(@Param("shopId") int shopId, @Param("id") int id);

	@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productDetails")
	List<Product> findAllWithDetails();

	@Query("select p.trangThai from Product p where p.id=:productId and p.shop.shopId=:shopId")
	Integer getStateProductById(@Param("productId") Integer productId, @Param("shopId") Integer shopId);

//		 @Modifying
//		 @Query("update Product")
//		 Integer (@Param("productId") Integer productId,@Param("shopId") Integer shopId);

	@Query(value = "SELECT " + "    p.id, " + "    p.tenSanPham, " + "    p.hinhAnh, "
			+ "    COUNT(DISTINCT d.id) AS tongDon, " + "    COUNT(DISTINCT d.taiKhoanId) AS soNguoiMua, "
			+ "    SUM(c.soLuong) AS tongSoLuongDaBan, " + "    SUM(d.tongTien) AS tongTien " + "FROM "
			+ "    chitietdonhang c " + "JOIN " + "    donhang d ON d.id = c.donHangId " + "JOIN "
			+ "    chitietsanpham pd ON pd.id = c.productId " + "JOIN " + "    sanpham p ON p.id = pd.sanPhamId "
			+ "WHERE " + "    d.shopId = :shopId " + "GROUP BY " + "    p.id, p.tenSanPham, p.hinhAnh " + "HAVING "
			+ "    SUM(c.soLuong) > 9 " + "LIMIT 10", nativeQuery = true)
	List<Object[]> getProductTopSale(@Param("shopId") Integer shopId);

	@Query("SELECT COUNT(DISTINCT d.product.id) " + "FROM ProductDetail d where d.product.shop.shopId=:shopId "
			+ "GROUP BY d.product.id " + "HAVING SUM(d.soLuong) < 10")
	Integer getReportSaleProduct(@Param("shopId") Integer shopId);

	@Query("SELECT id FROM Product p WHERE p.id = :id AND p.shop.id = :shopId")
	Optional<Integer> findByIdInteger(@Param("shopId") int shopId, @Param("id") int id);

	@Query(value = "SELECT p.id FROM SanPham p WHERE p.cuaHang = :shopId", nativeQuery = true)
	Set<Integer> findProductIdsByShopId(@Param("shopId") int shopId);

	/// <------
//		@Query(value = "SELECT p.id FROM SanPham p WHERE p.cuaHang = :shopId AND p.id IN :ids", nativeQuery = true)
//		Set<Integer> SetfindByShopIdAndInIds(@Param("shopId") Integer shopId, @Param("ids") Set<Integer> ids);

	@Query(value = "SELECT p.id FROM SanPham p WHERE p.cuaHangId = :shopId AND p.id IN :ids", nativeQuery = true)
	List<Integer> findByShopIdAndInIds(@Param("shopId") Integer shopId, @Param("ids") Set<Integer> ids);

	@Query(value = "SELECT count(p.id) FROM SanPham p WHERE p.cuaHangId = :shopId AND p.id IN :ids", nativeQuery = true)
	int CountfindByShopIdAndInIds(@Param("shopId") Integer shopId, @Param("ids") Set<Integer> ids);

	@Query("SELECT new com.API.model.ChiTietKhuyenMai(km.id,km.tenKhuyenMai,km.ngayBatDau,km.ngayKetThuc,km.giaTriKhuyenMai,ck.product.id) "
			+ "FROM ChiTietKhuyenMai ck JOIN KhuyenMai km ON km.id=ck.khuyenMai.id\r\n"
			+ "	WHERE  ck.product IN:l AND km.ngayKetThuc>now()")
	List<ChiTietKhuyenMai> getCountKhuyenMaiInProductList(@Param("l") List<Product> l);

//	    public ChiTietKhuyenMai( Integer id ,String tenKhuyenMai,Date ngayBatDau,Date ngayKetThuc,Integer giatri,Integer productId) {
//			this.khuyenMai=new KhuyenMai(id,tenKhuyenMai, ngayBatDau, ngayKetThuc,giatri);
//			this.product=new Product(productId);
//		}

//		public KhuyenMai(int id, String tenKhuyenMai,
//				java.util.Date ngayBatDau, java.util.Date ngayKetThuc,
//				 int giaTriKhuyenMai) {
//			super();
//			this.id = id;
//			this.tenKhuyenMai = tenKhuyenMai;
//			this.ngayBatDau = ngayBatDau;
//			this.ngayKetThuc = ngayKetThuc;
//			this.giaTriKhuyenMai = giaTriKhuyenMai;
//		}

	/// ----->

	@Query("SELECT COUNT(p.id) FROM Product p WHERE  p.id IN :ids AND  p.shop.id = :shopId ")
	int countByIdInAndShopId(@Param("ids") Set<Integer> ids, @Param("shopId") Integer shopId);

	@Query("SELECT COUNT(p.id) FROM Product p WHERE  p.id IN :ids AND  p.shop.id = :shopId and p.trangThai=1 ")
	int countByIdInAndShopIdAndActive(@Param("ids") Set<Integer> ids, @Param("shopId") Integer shopId);

	@Query("SELECT p.id FROM Product p WHERE p.shop.id = :shopId AND p.id IN :ids ")
	List<Integer> countByIdInAndShopIds(@Param("ids") Set<Integer> ids, @Param("shopId") Integer shopId);

	@Query("SELECT km FROM KhuyenMai km WHERE km.shop.id = :shopId AND km.ngayBatDau < :ngayBatDau")
	List<KhuyenMai> findByShopIdAndNgayBatDau(@Param("shopId") int shopId,
			@Param("ngayBatDau") java.util.Date ngayBatDau);

	@Query(value = "SELECT p.id FROM sanPham p WHERE p.cuaHangId = :shopId", nativeQuery = true)
	List<Integer> findProductIdsByShopIdNotNavtive(@Param("shopId") int shopId);

	@Query(value = "SELECT p.id AS productId, \r\n" + "	       p.tenSanPham AS productName, \r\n"
			+ "	       COUNT(DISTINCT vsd.id) AS soLuongVoucher, \r\n"
			+ "	       COUNT(DISTINCT km.id) AS soLuongKhuyenMai\r\n" + "	FROM SanPham p\r\n"
			+ "	LEFT JOIN voucherShopDetails vsd ON p.id = vsd.productId\r\n"
			+ "	LEFT JOIN chitietkhuyenmai km ON p.id = km.productId   \r\n" + "	GROUP BY p.id", nativeQuery = true)
	List<Object[]> getProductInfoSale();

	@Query("SELECT p FROM Product p LEFT JOIN FETCH p.productDetails WHERE p.id = ?1")
	Product findProductWithDetailsById(int id);

//	    @Query("select")
//	    ProductDetail getProductDetailById(@Param("id")Integer id)

	@Query(value = "SELECT p.product.id,min(p.giaBan),max(p.giaBan),SUM(p.soLuong) FROM ProductDetail p WHERE \r\n"
			+ "	    p.product IN :l \r\n" + "	    GROUP BY (p.product.id)")
	List<Object[]> getIbject(@Param("l") List<Product> p);

//	    Integer id,String tenMau,String tenKichThuoc,String hinhAnh
	@Query("select new com.API.model.ProductDetail(p.id,p.mauSac.tenMau,p.kichThuoc.tenKichThuoc,p.hinhAnh,p.soLuong,p.giaBan) from ProductDetail p where p.product.id=:productId")
	List<ProductDetail> getProductDetailByProductId(@Param("productId") Integer id);

	@Modifying
	@Query("UPDATE Product p SET p.trangThai = :trangThaiId WHERE p.id IN :ids AND p.shop.shopId = :shopId")
	Integer updateStatusByIdsAndShopId(@Param("trangThaiId") Integer trangThaiId, @Param("ids") List<Integer> ids,
			@Param("shopId") Integer shopId);

	@Query("SELECT p FROM Product p WHERE p.id = :id AND p.shop.id = :shopId")
	Optional<Product> findByIdAndShopId(@Param("id") Integer id, @Param("shopId") Integer shopId);

	// saler ()
	@Query("SELECT new com.API.model.Product(p.id, p.tenSanPham, p.hinhAnh, p.trangThai) " + "FROM Product p "
			+ "WHERE p.shop.id = :id AND p.trangThai = :state and p.tenSanPham like :key " + "")
	org.springframework.data.domain.Page<Product> findAllProductsWithDetails(@Param("id") Integer id, Pageable p,
			@Param("state") Integer state, @Param("key") String key);

	@Query("SELECT new com.API.model.Product(p.id, p.tenSanPham, p.hinhAnh, p.trangThai) " + "FROM Product p "
			+ "WHERE p.shop.id = :id AND p.trangThai = :state and ( p.tenSanPham like :key and p.category.id in:ids ) "
			+ "")
	org.springframework.data.domain.Page<Product> findAllProductsWithDetailsAndIds(@Param("id") Integer id, Pageable p,
			@Param("state") Integer state, @Param("key") String key, @Param("ids") List<Integer> ids);

	@Query("SELECT new com.API.model.Product(p.id, p.tenSanPham,p.hinhAnh) FROM Product p where p.shop.id=:id and p.trangThai=:status")
	org.springframework.data.domain.Page<Product> findAllProductsWithDetails(@Param("id") Integer id,
			@Param("status") Integer status, Pageable p);

	/// THEM KOW == NAM

//	    @Query(value = """
//	            SELECT 
//	                sp.id AS sanPhamId,
//	                sp.tenSanPham AS tenSanPham,
//	                sp.hinhAnh AS hinhAnhSanPham,
//	                MIN(ctsp.giaBan) AS giaSanPham,
//	                MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai,
//	                km.giaTriKhuyenMai AS phanTramKhuyenMai,
//	                sp.soLuongDaBan AS soLuongDaBan
//	            FROM 
//	                sanpham sp
//	            JOIN 
//	                chitietsanpham ctsp ON sp.id = ctsp.sanPhamId
//	            LEFT JOIN 
//	                chitietkhuyenmai ctkm ON sp.id = ctkm.productId
//	            LEFT JOIN 
//	                khuyenmai km ON ctkm.khuyenMaiId = km.id
//	            WHERE 
//	                sp.trangThai = 1
//	                AND ctsp.soLuong > 0
//	                AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW())
//	                AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW())
//	            GROUP BY 
//	                sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan
//	            """, nativeQuery = true)
//	        List<Object[]> getProductHome();

//	        @Query(value = "SELECT " +
//	                "sp.id AS sanPhamId, " +
//	                "sp.tenSanPham AS tenSanPham, " +
//	                "sp.hinhAnh AS hinhAnhSanPham, " +
//	                "MIN(ctsp.giaBan) AS giaSanPham, " +
//	                "MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)) AS giaSauKhuyenMai, " +
//	                "km.giaTriKhuyenMai AS phanTramKhuyenMai, " +
//	                "sp.soLuongDaBan AS soLuongDaBan " +
//	                "FROM sanpham sp " +
//	                "JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId " +
//	                "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId " +
//	                "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id " +
//	                "WHERE sp.trangThai = 1 " +
//	                "AND ctsp.soLuong > 0 " +  // Sản phẩm phải có biến thể
//	                "AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW()) " +  // Khuyến mãi đang có hiệu lực
//	                "AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " +
//	                "AND sp.tenSanPham LIKE %:name% " +  // Tên sản phẩm chứa từ :name
//	                "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan", 
//	        nativeQuery = true)
//	        Page<Object[]> findProductByName(String name, Pageable pageable);

	// sơn
	// lấy sản phẩm admin/seller
	@Query(value = """
			    SELECT p.*, pd.giaBan
			    FROM sanpham p
			    LEFT JOIN chitietsanpham pd ON p.id = pd.sanphamid
			    WHERE p.cuaHangId = :id
			      AND pd.GiaBan = (
			          SELECT MIN(pd2.GiaBan)
			          FROM chitietsanpham pd2
			          WHERE pd2.sanphamid = p.id
			      )
			    ORDER BY p.id DESC
			""", nativeQuery = true)
	List<Object[]> findAllByCuHangOrderByIdDesc(@Param("id") Integer id);

	// THÊM MỚI NHẤT
	@Query("select p.id from Product p where p.id=:id")
	Optional<Integer> getProuctIdById(@Param("id") Integer id);

	/// NAM

	@Query(value = """
			SELECT
			    sp.id AS sanPhamId,
			    sp.tenSanPham AS tenSanPham,
			    sp.hinhAnh AS hinhAnhSanPham,
			    MIN(ctsp.giaBan) AS giaSanPham,
			    ROUND(MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)), 0) AS giaSauKhuyenMai,
			    km.giaTriKhuyenMai AS phanTramKhuyenMai,
			    sp.soLuongDaBan AS soLuongDaBan,
			    sp.theLoaiId AS TheLoai
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
			GROUP BY
			    sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan
			ORDER BY
			    RAND()
			""", nativeQuery = true)
	List<Object[]> getProductHome();

	@Query(value = "SELECT " + "sp.id AS sanPhamId, " + "sp.tenSanPham AS tenSanPham, "
			+ "sp.hinhAnh AS hinhAnhSanPham, " + "MIN(ctsp.giaBan) AS giaSanPham, "
			+ "ROUND(MIN(ctsp.giaBan - (km.giaTriKhuyenMai / 100 * ctsp.giaBan)), 0) AS giaSauKhuyenMai, "
			+ "km.giaTriKhuyenMai AS phanTramKhuyenMai, " + "sp.soLuongDaBan AS soLuongDaBan, "
			+ "FLOOR(RAND() * 100000) AS random_order " + // Thêm cột ngẫu nhiên
			"FROM sanpham sp " + "JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId "
			+ "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId "
			+ "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id " + "WHERE sp.trangThai = 1 "
			+ "AND ctsp.soLuong > 0 " + "AND (km.thoiGianBD IS NULL OR km.thoiGianBD <= NOW()) "
			+ "AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "AND sp.tenSanPham LIKE %:name% "
			+ "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan ", nativeQuery = true)
	Page<Object[]> findProductByName(String name, Pageable pageable);

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
			+ "  AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "  AND sp.tenSanPham LIKE %:name% "
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
					+ "  AND (km.thoiGianKT IS NULL OR km.thoiGianKT >= NOW()) " + "  AND sp.tenSanPham LIKE %:name% "
					+ "  AND (COALESCE(:theLoaiIds) IS NULL OR sp.theLoaiId IN (:theLoaiIds) OR sp.theLoaiId IN (SELECT id FROM subcategories)) "
					+ "  AND (COALESCE(:kichThuocIds) IS NULL OR ctsp.kichThuocId IN :kichThuocIds) "
					+ "  AND (COALESCE(:thuongHieuIds) IS NULL OR sp.thuongHieuId IN :thuongHieuIds) "
					+ "  AND (COALESCE(:mauSacIds) IS NULL OR ctsp.mauSacId IN :mauSacIds) "
					+ "  AND (COALESCE(:giaTu) IS NULL OR ctsp.giaBan >= :giaTu) "
					+ "  AND (COALESCE(:giaDen) IS NULL OR ctsp.giaBan <= :giaDen) "
					+ "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, km.giaTriKhuyenMai, sp.soLuongDaBan", nativeQuery = true)
	Page<Object[]> findSanPhamWithFilters(@Param("name") String name, @Param("theLoaiIds") List<Integer> theLoaiIds,
			@Param("kichThuocIds") List<Integer> kichThuocIds, @Param("thuongHieuIds") List<Integer> thuongHieuIds,
			@Param("mauSacIds") List<Integer> mauSacIds, @Param("giaTu") Double giaTu, @Param("giaDen") Double giaDen,
			Pageable pageable);

//son 5/12
	@Query(value = """
			SELECT
			    sp.id AS sanPhamId,
			    sp.tenSanPham,
			    sp.hinhAnh,
			    sp.trangThai AS trangThaiSanPham,
			    sp.video,
			    sp.soLuongDaBan,
			    ch.tenShop,
			    tl.tenLoai AS theLoai,
			    -- Trả về giá trị lớn nhất và nhỏ nhất của sản phẩm
			    MAX(ctsp.giaBan) AS giaLonNhat,
			    MIN(ctsp.giaBan) AS giaNhoNhat,
			    -- Lấy giá trị khuyến mãi của sản phẩm nếu có
			    MAX(CASE
			        WHEN km.giaTriKhuyenMai IS NOT NULL THEN
			            km.giaTriKhuyenMai
			        ELSE
			            0
			    END) AS giaTriKhuyenMai
			FROM
			    sanpham sp
			JOIN
			    cuahang ch ON sp.cuaHangId = ch.id
			LEFT JOIN
			    theloai tl ON sp.theLoaiId = tl.id
			LEFT JOIN
			    chitietsanpham ctsp ON sp.id = ctsp.sanPhamId
			LEFT JOIN
			    chitietkhuyenmai ctkm ON sp.id = ctkm.productId
			LEFT JOIN
			    khuyenmai km ON ctkm.khuyenMaiId = km.id
			    AND km.thoiGianBD <= NOW()
			    AND km.thoiGianKT >= NOW()
			    AND km.trangThai = 1
			GROUP BY
			    sp.id, sp.tenSanPham, sp.hinhAnh, sp.trangThai, sp.video, sp.soLuongDaBan, ch.tenShop, tl.tenLoai
			ORDER BY
			    sp.id DESC
			""", countQuery = """
			SELECT COUNT(*)
			FROM (
			    SELECT sp.id
			    FROM
			        sanpham sp
			    JOIN
			        cuahang ch ON sp.cuaHangId = ch.id
			    LEFT JOIN
			        theloai tl ON sp.theLoaiId = tl.id
			    LEFT JOIN
			        chitietsanpham ctsp ON sp.id = ctsp.sanPhamId
			    LEFT JOIN
			        chitietkhuyenmai ctkm ON sp.id = ctkm.productId
			    LEFT JOIN
			        khuyenmai km ON ctkm.khuyenMaiId = km.id
			        AND km.thoiGianBD <= NOW()
			        AND km.thoiGianKT >= NOW()
			        AND km.trangThai = 1
			    GROUP BY
			        sp.id
			) AS groupedResult
			""", nativeQuery = true)
	Page<Object[]> findProductsWithMaxAndMinPrice(Pageable pageable);

	@Query(value = """
			SELECT
			    sp.id AS sanPhamId,
			    sp.tenSanPham,
			    sp.soLuong,
			    sp.moTa,
			    sp.hinhAnh,
			    sp.trangThai AS trangThaiSanPham,
			    sp.video,
			    sp.soLuongDaBan,
			    ch.id AS cuaHangId,
			    ch.tenShop,
			    ch.trangThai AS trangThaiCuaHang,
			    ch.hoVaTen AS chuCuaHang,
			    ch.email AS emailCuaHang,
			    ch.hinhAnh AS hinhAnhCuaHang,
			    ch.ngayDangKy AS ngayDangKyCuaHang,
			    ch.xacMinh,
			    tl.tenLoai AS tenTheLoai,
			    th.tenThuongHieu,
			    dc.toanBoDiaChi AS diaChiCuaHang,
			    tk.soDienThoai AS soDienThoaiCuaHang
			FROM
			    sanpham sp
			LEFT JOIN
			    cuahang ch ON sp.cuaHangId = ch.id
			LEFT JOIN
			    theloai tl ON sp.theLoaiId = tl.id
			LEFT JOIN
			    thuonghieu th ON sp.thuongHieuId = th.id
			LEFT JOIN
			    diachi dc ON dc.cuaHangId = ch.id
			LEFT JOIN
			    taikhoan tk ON ch.taiKhoan_Id = tk.id
			WHERE
			    sp.id = :id
			""", nativeQuery = true)
	Object[] findProductDetailsById(@Param("id") Integer id);

	// Lấy sản phẩm biến thể admin

	@Query(value = """
			SELECT
			    ctsp.id AS chiTietSanPhamId,
			    ctsp.soLuong,
			    ctsp.giaBan,
			    ctsp.hinhAnhBienThe,
			    ms.tenMau AS tenMauSac,
			    kt.tenKichThuoc AS tenKichThuoc
			FROM
			    chitietsanpham ctsp
			LEFT JOIN
			    sanpham sp ON ctsp.sanPhamId = sp.id
			LEFT JOIN
			    mausac ms ON ctsp.mauSacId = ms.id
			LEFT JOIN
			    kichthuoc kt ON ctsp.kichThuocId = kt.id
			WHERE
			    ctsp.sanPhamId = :id
			""", countQuery = """
			SELECT
			    COUNT(*)
			FROM
			    chitietsanpham ctsp
			LEFT JOIN
			    sanpham sp ON ctsp.sanPhamId = sp.id
			WHERE
			    ctsp.sanPhamId = :id
			""", nativeQuery = true)
	Page<Object[]> findChiTietSanPhamBySanPhamId(@Param("id") Integer id, Pageable pageable);

	// lấy danh sách màu sắc và kích thước
	@Query(value = """
			SELECT
			    ctsp.id AS chiTietSanPhamId,
			    ms.tenMau AS tenMauSac,
			    kt.tenKichThuoc AS tenKichThuoc,
			    ctsp.giaBan,
			    ctsp.hinhAnhBienThe
			FROM
			    chitietsanpham ctsp
			LEFT JOIN
			    sanpham sp ON ctsp.sanPhamId = sp.id
			LEFT JOIN
			    mausac ms ON ctsp.mauSacId = ms.id
			LEFT JOIN
			    kichthuoc kt ON ctsp.kichThuocId = kt.id
			WHERE
			    ctsp.sanPhamId = :id
			""", nativeQuery = true)
	List<Object[]> findMauVaKichThuocBySanPhamId(@Param("id") Integer id);

	// lấy thông tin khuyến mãi của sản phẩm
	@Query(value = """
			SELECT
			    ctkm.id AS chiTietKhuyenMaiId,
			    ctkm.khuyenMaiId,
			    km.giaTriKhuyenMai,
			    km.trangThai
			FROM
			    chitietkhuyenmai ctkm
			INNER JOIN
			    khuyenmai km ON ctkm.khuyenMaiId = km.id
			WHERE
			    ctkm.productId = :id
			""", nativeQuery = true)
	List<Object[]> findPromotionByProductId(@Param("id") int productId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE sanpham SET trangThai = :trangThai WHERE id = :id", nativeQuery = true)
	int updateTrangThaiById(@Param("id") Integer id, @Param("trangThai") Integer trangThai);

	@Query(value = "SELECT " + "sp.id AS sanPhamId, " + "sp.tenSanPham, " + "sp.hinhAnh, "
			+ "sp.trangThai AS trangThaiSanPham, " + "sp.video, " + "sp.soLuongDaBan, " + "ch.tenShop, "
			+ "tl.tenLoai AS theLoai, " + "MAX(ctsp.giaBan) AS giaLonNhat, " + "MIN(ctsp.giaBan) AS giaNhoNhat, "
			+ "MAX(CASE WHEN km.giaTriKhuyenMai IS NOT NULL THEN km.giaTriKhuyenMai ELSE 0 END) AS giaTriKhuyenMai "
			+ "FROM sanpham sp " + "JOIN cuahang ch ON sp.cuaHangId = ch.id "
			+ "LEFT JOIN theloai tl ON sp.theLoaiId = tl.id "
			+ "LEFT JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId "
			+ "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId "
			+ "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id AND km.thoiGianBD <= NOW() AND km.thoiGianKT >= NOW() AND km.trangThai = 1 "
			+ "WHERE (:name IS NULL OR sp.tenSanPham LIKE CONCAT('%', :name, '%')) "
			+ "AND (:trangThai IS NULL OR sp.trangThai = :trangThai) "
			+ "AND (:shopName IS NULL OR ch.tenShop LIKE CONCAT('%', :shopName, '%')) "
			+ "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, sp.trangThai, sp.video, sp.soLuongDaBan, ch.tenShop, tl.tenLoai "
			+ "ORDER BY sp.id DESC", countQuery = "SELECT COUNT(DISTINCT sp.id) " + "FROM sanpham sp "
					+ "JOIN cuahang ch ON sp.cuaHangId = ch.id " + "LEFT JOIN theloai tl ON sp.theLoaiId = tl.id "
					+ "LEFT JOIN chitietsanpham ctsp ON sp.id = ctsp.sanPhamId "
					+ "LEFT JOIN chitietkhuyenmai ctkm ON sp.id = ctkm.productId "
					+ "LEFT JOIN khuyenmai km ON ctkm.khuyenMaiId = km.id AND km.thoiGianBD <= NOW() AND km.thoiGianKT >= NOW() AND km.trangThai = 1 "
					+ "WHERE (:name IS NULL OR sp.tenSanPham LIKE CONCAT('%', :name, '%')) "
					+ "AND (:trangThai IS NULL OR sp.trangThai = :trangThai) "
					+ "AND (:shopName IS NULL OR ch.tenShop LIKE CONCAT('%', :shopName, '%')) ", nativeQuery = true)
	Page<Object[]> FillterProductsWithMaxAndMinPrice(@Param("name") String name, @Param("trangThai") Integer trangThai,
			@Param("shopName") String shopName,

			Pageable pageable);
	
	
	@Query(value = "SELECT " +
	        "sp.id AS SanPhamId, " +
	        "sp.tenSanPham AS TenSanPham, " +
	        "sp.hinhAnh AS AnhSanPham, " +
	        "ch.tenShop AS TenCuaHang, " +
	        "ch.hinhAnh AS AnhCuaHang, " +
	        "SUM(sp.soLuongDaBan) AS SoLuongDaBan " +
	        "FROM sanpham sp " +
	        "INNER JOIN cuahang ch ON sp.cuaHangId = ch.id " +
	        "INNER JOIN donhang dh ON dh.shopId = ch.id " +
	        "WHERE sp.trangThai = 1 " +
	        "AND ( " +
	        "   (:filterType = 'DAY' AND DATE(dh.ngayTaoDon) = CURDATE()) " +
	        "   OR (:filterType = 'WEEK' AND YEARWEEK(dh.ngayTaoDon, 1) = YEARWEEK(CURDATE(), 1)) " +
	        "   OR (:filterType = 'MONTH' AND MONTH(dh.ngayTaoDon) = MONTH(CURDATE()) AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE())) " +
	        "   OR (:filterType = 'YEAR' AND YEAR(dh.ngayTaoDon) = YEAR(CURDATE())) " +
	        ") " +
	        "GROUP BY sp.id, sp.tenSanPham, sp.hinhAnh, ch.tenShop, ch.hinhAnh " +
	        "ORDER BY SoLuongDaBan DESC", nativeQuery = true)
	Page<Object[]> getTop30ProductBestSell(@Param("filterType") String filterType, Pageable pageable);
}
