package com.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.API.model.BaoCao;

@Repository
public interface BaoCaoRepository extends JpaRepository<BaoCao, Integer>{
	// SƠN 25/11.
	// Truy vấn Admin
		// Trang dasboard
		// hàm lấy tổng doanh thu hôm nay
		@Query(value = "SELECT COALESCE(SUM(d.tongTien), 0) FROM DonHang d JOIN TrangThai t ON d.trangThaiId = t.id WHERE DATE(d.ngayTaoDon) = CURRENT_DATE AND t.TenTrangThai = 'Giao hàng thành công'", nativeQuery = true)
		Double getTodayRevenue();

		// hàm lấy tổng lợi nhuận hôm nay
		@Query(value = "SELECT COALESCE(SUM(d.tongTien * 0.04), 0) FROM DonHang d JOIN TrangThai t ON d.trangThaiId = t.id WHERE DATE(d.ngayTaoDon) = CURRENT_DATE AND t.TenTrangThai = 'Giao hàng thành công'", nativeQuery = true)
		Double getTodayRevenuePercentage();

		// Hàm đếm số đơn hàng hôm nay
		@Query(value = "SELECT COUNT(*) FROM donhang d JOIN trangthai t ON d.trangThaiId = t.id WHERE DATE(d.ngayTaoDon) = CURRENT_DATE AND t.TenTrangThai = 'Giao hàng thành công'", nativeQuery = true)
		Long countCompletedOrdersToday();

		// Hàm đếm tổng số user
		@Query(value = "SELECT COUNT(*) FROM taikhoan WHERE trangThai = '1' AND vaiTro = 'user'", nativeQuery = true)
		Long countActiveAccounts();

		// Hàm đếm tổng số seller
		@Query(value = "SELECT COUNT(*) FROM taikhoan WHERE trangThai = '1' AND vaiTro = 'seller'", nativeQuery = true)
		Long countActiveSeller();

		// Hàm đếm tổng sản pahmar
		@Query(value = "SELECT COUNT(*) FROM sanpham s JOIN cuahang c ON s.cuaHangId = c.id WHERE s.trangThai = 1 AND c.trangThai = 1", nativeQuery = true)
		Long countActiveProducts();
}
