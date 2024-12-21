package com.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.Product;
import com.API.model.ProductDetail;

import jakarta.transaction.Transactional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Integer> {
	
	
	// MỚI 
	
	@Query("SELECT new com.API.model.ProductDetail(pd.id,pd.soLuong,pd.giaBan,pd.mauSac.tenMau,pd.kichThuoc.tenKichThuoc,pd.product.id) FROM ProductDetail pd where pd.product.id in :ids")
    List<ProductDetail> findByProductIdsProductLive(@Param("ids") List<Integer> ids);
	
	
	
	
	
	
	
	
	
	// HẾT
	
	
	
	
	
	
	
	
	
	
	// CART
	@Query("SELECT new com.API.model.ProductDetail(1,pd.soLuong,p.trangThai) FROM ProductDetail pd JOIN "
			+ " Product p on p.id=pd.product.id WHERE pd.id = :id")
	Optional<ProductDetail> getProductDetailForCart(@Param("id") int id);


	@Query(value="SELECT pd.id,pd.soluong,p.trangThai FROM products p \r\n"
			+ "\r\n"
			+ "INNER JOIN ChiTietSanPham pd ON pd.sanPhamId=p.id\r\n"
			+ "WHERE pd.id=:id\r\n"
			+ "",nativeQuery = true)
	Object[] getO(@Param("id") int id);
	
	 //////////////////////////////////////////
	
	@Modifying
    @Query("UPDATE ProductDetail pd SET pd.soLuong = :soLuong, pd.giaBan = :giaBan, pd.hinhAnh = :hinhAnh WHERE pd.id = :id")
    void updateProductDetail(@Param("soLuong") int soLuong, 
                             @Param("giaBan") float giaBan, 
                             @Param("hinhAnh") String hinhAnh,
                             @Param("id") Integer id);
	
	// lấy test.
	@Query(value = "SELECT c.id,c.soLuong,c.donGia,p.tenSanPham,s.shopName,s.shopId,m.tenMau,k.tenKichThuoc FROM cart c\r\n"
			+ "JOIN ChiTietSanPham pd ON c.productId=pd.id\r\n"
			+ "JOIN mausac m ON m.id=pd.mauSacId \r\n"
			+ "JOIN kichthuoc k ON pd.kichThuocId=k.id\r\n"
			+ "JOIN products p ON p.id=pd.productId\r\n"
			+ "JOIN shop s ON p.shopId=s.shopId where c.accountId=1",nativeQuery = true)
	List<Object[]> getObject();
	
	
	// cập nhật trangj lại số lượng của đơn hàng.
	@Modifying
    @Transactional
    @Query(value = "CALL UpdateSoLuongProduct(:productDetailId, :quantity)", nativeQuery = true)
    void updateSoLuongProduct(@Param("productDetailId") int productDetailId, @Param("quantity") int quantity);
	
	
	// sale
	@Query("SELECT new com.API.model.ProductDetail(pd.id,pd.soLuong,pd.giaBan,pd.mauSac.tenMau,pd.kichThuoc.tenKichThuoc,pd.product.id) FROM ProductDetail pd where pd.product in :ids")
    List<ProductDetail> findByProductIds(@Param("ids") List<Product> ids);
	
	
	
	
	// lấy lần nựa
	
	
	
}
