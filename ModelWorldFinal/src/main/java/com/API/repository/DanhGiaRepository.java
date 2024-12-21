package com.API.repository;

import java.lang.foreign.Linker.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.ChiTietDanhGia;
import com.API.model.DanhGiaSanPham;

import jakarta.transaction.Transactional;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGiaSanPham, Integer> {
	
	@Query("select new com.API.model.DanhGiaSanPham(p.account.hinhAnh,p.id,p.soSao,p.noiDungDanhGia,p.account.hoVaTen,p.account.soDienThoai,p.ngayDanhGia,p.shopPhanHoi,d.product.id) "
			+ "from DanhGiaSanPham p join OrderDetail d on d.id=p.orderDetail.id where d.product.id in:ids and (p.account.hoVaTen like:key or p.noiDungDanhGia like :key)")
	Page<DanhGiaSanPham> getDanhGiaByProductIds(@Param("ids")List<Integer>ids,Pageable p,@Param("key")String key);
	
//	Integer id,Integer danhGiaId,String link,String type
	@Query("select new com.API.model.ChiTietDanhGia(p.id,p.danhGiaSanPham.id,p.link,p.type)  from ChiTietDanhGia p where p.danhGiaSanPham.id in:ids")
	List<ChiTietDanhGia> getChiTietDanhGiaInIds(@Param("ids")List<Integer>ids);
	
	@Query("SELECT p.soSao,COUNT(p.id) FROM DanhGiaSanPham p JOIN OrderDetail  d \r\n"
			+ "ON p.orderDetail.id=d.id and d.product.id in :ids\r\n"
			+ "GROUP BY p.soSao ")
	List<Integer[]> getCountStar(@Param("ids")List<Integer>ids);
	
	
	@Query("SELECT distinct new com.API.model.DanhGiaSanPham(p.id,p.soSao,p.noiDungDanhGia,tk.hoVaTen,tk.soDienThoai,p.ngayDanhGia,p.shopPhanHoi,cd.id) "
			+ " FROM DanhGiaSanPham p \r\n"
			+ "	JOIN Account tk ON tk.id= p.account.id\r\n"
			+ "	JOIN OrderDetail cd ON cd.id= p.orderDetail.id\r\n"
			+ "	JOIN Order dh on dh.id= cd.order.id WHERE dh.shop.id=:shopId and p.soSao in:ids and (tk.hoVaTen like :key or p.noiDungDanhGia like :key ) and p.ngayDanhGia >=:date"
			+ "  AND (( :condition = 0 ) OR ( :condition = 1 AND p.shopPhanHoi IS NULL ) OR ( :condition = 2 AND p.shopPhanHoi IS NOT NULL ))"
			+ " ")
//			+ "and (p.account.hoVaTen like:key )")
	//or p.noiDungDanhGia like :key)
	Page<DanhGiaSanPham> getDanhGiaInShop(@Param("shopId")Integer shopId,@Param("ids")List<Integer>ids,Pageable p,@Param("condition")Integer co,
						@Param("key")String key,@Param("date")LocalDateTime l
			);
	

	@Query("select p.id from DanhGiaSanPham p join OrderDetail cd "
			+ " on p.id=:id and p.orderDetail.id=cd.id join Order d on d.id=cd.order.id where d.shop.id=:shopId")
	Optional<Integer> getIdDanhGiaById(@Param("shopId")Integer id,@Param("id")Integer idDanhGia);
	
	
	@Modifying
	@Transactional
	@Query("update DanhGiaSanPham d set d.shopPhanHoi=:comment where d.id =:id")
	Integer updateComment(@Param("comment")String comment,@Param("id")Integer id);

	
}
//(Integer id,Integer soSao,String noiDungDanhGia,String taiKhoanId,String tenTaiKhoan)