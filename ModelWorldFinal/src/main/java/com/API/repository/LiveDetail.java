package com.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.DTO.ProductLiveDTO;
import com.API.model.Live_Product;
import com.API.model.Product;

@Repository
public interface LiveDetail extends JpaRepository<Live_Product,Integer> {
	List<Live_Product> findByLiveId(Integer liveId);
	
	@Query("select p.product from  Live_Product p ")
	public List<Product> getListByIdLive(@Param("liveId")Integer liveId);
	
	// LẤY DANH SÁCH SẢN PHẨM CỦA LIVE
	@Query("SELECT new com.API.DTO.ProductLiveDTO(lp.id,lp.soLuongGioiHan,new com.API.model.Product(p.id,p.tenSanPham,p.hinhAnh,p.soLuongDaBan,1),ck.khuyenMai.giaTriKhuyenMai,lp.giaGiam)  \r\n"
			+ "FROM Live_Product lp\r\n"
			+ "JOIN LiveSession ls ON lp.live.id = ls.id and ls.id=:liveId\r\n"
			+ "JOIN Product p ON p.id = lp.product.id\r\n"
			+ "LEFT JOIN ChiTietKhuyenMai ck ON ck.product.id = p.id and now between ck.khuyenMai.ngayBatDau and ck.khuyenMai.ngayKetThuc \r\n"
			+ " ")
	public List<ProductLiveDTO> getProductInlive(@Param("liveId")Integer liveId);
}

