package com.API.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.Account;
import com.google.common.base.Optional;

import jakarta.transaction.Transactional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	@Modifying
	@Transactional
	@Query("Update Account a set a.vaiTro='NguoiBan' where a.id=:id")
	Integer updateSeller(@Param("id") Integer userName);
	
	@Query(value = "SELECT a.id FROM Account a WHERE a.tenTaiKhoan LIKE :userName")
	public Integer getUserIdByTenTaiKhoan(@Param("userName") String userName);
	
	@Query(value = "SELECT * FROM taikhoan WHERE TenTaiKhoan LIKE :userName", nativeQuery = true)
	public Account findAccountByUserName(@Param("userName") String userName);
	
	@Query(value = "SELECT * FROM taikhoan WHERE soDienThoai LIKE :userName", nativeQuery = true)
	public Account findAccountBySoDienThoai(@Param("userName") String userName);
	
	@Query(value = "SELECT * FROM taikhoan WHERE SoDienThoai LIKE :phone", nativeQuery = true)
	public Account findAccountByPhone(@Param("phone") String phone);
	
	@Query(value = "SELECT * FROM taikhoan WHERE id LIKE :phone", nativeQuery = true)
	public Account findAccountById(@Param("phone") Integer  phone);
	
	@Query(value = "SELECT * FROM taikhoan WHERE id LIKE :phone and vaiTro like 'NguoiBan'", nativeQuery = true)
	public Account findAccountByIdS(@Param("phone") Integer  phone);
	
	@Query(value = "SELECT id FROM taikhoan WHERE soDienThoai LIKE :userName", nativeQuery = true)
	Optional<Integer> getSoDienThoai(@Param("userName") String userName);
	
	
	@Query(value = "SELECT p FROM Account p WHERE p.soDienThoai LIKE :userName or p.tenTaiKhoan like:userName")
	Optional<Account> getSoDienThoais(@Param("userName") String userName);
	
	
	@Modifying
    @Transactional
    @Query(value = "INSERT INTO taikhoan (matKhau, soDienThoai,tenTaiKhoan, trangThai) VALUES (:matKhau, :soDienThoai,:tenTaiKhoan, :HoatDong)", nativeQuery = true)
    void addAccount(String matKhau, String soDienThoai,String tenTaiKhoan);
	
	
	@Query(value = "SELECT k FROM Account k WHERE k.id=:id")
	public Account findAccountByPhone(@Param("id") Integer id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Account a SET a.tenTaiKhoan = :tenTaiKhoan, a.hoVaTen = :hoVaTen,a.gioiTinh=:gioiTinh, a.sinhNhat = :sinhNhat, a.hinhAnh = :hinhAnh WHERE a.id = :id")
	Integer updateProfile(@Param("tenTaiKhoan") String tenTaiKhoan,
	                      @Param("hoVaTen") String hoVaTen,
	                      @Param("gioiTinh") String gioiTinh,
	                      @Param("sinhNhat") Date sinhNhat,
	                      @Param("hinhAnh") String hinhAnh,
	                      @Param("id") Integer id);

	@Modifying
	@Transactional
	@Query("UPDATE Account a set a.matKhau=:matKhau where a.id=:id")
	void updateMatKhau(@Param("matKhau")String pass,@Param("id")Integer id);
	
	// mới
	@Query(value = "select p.soDienThoai from Account p where p.id=:id")
	public String getPhhoneByAccountId(@Param("id") Integer phone);
	
	@Query(value = "select p.id from Account p where p.email like :email")
	Optional<Integer> getUserIdByEmail(@Param("email") String phone);
	
	@Query(value = "select p.shopId from Shop p where p.email like :email")
	Optional<Integer> getShopIdByEmail(@Param("email") String phone);
	
	
	@Modifying
	@Transactional
	@Query("update Account a set a.email=:email where a.id=:id")
	Integer updateEmail(@Param("email")String pass,@Param("id")Integer id);
	
	
	
	// Admin
		// Lấy danh sách user
		 @Query(value = """
			        SELECT 
			            tk.id AS id,
			            tk.email AS email,
			            tk.tenTaiKhoan AS tenTaiKhoan,
			            tk.soDienThoai AS soDienThoai,
			            tk.hoVaTen AS hoVaTen,
			            tk.hinhAnh,
			            tk.trangThai,
			            COUNT(dh.id) AS soLuongDonHang,
			            COALESCE(SUM(dh.tongTien), 0) AS tongGiaTriDonHang
			        FROM taikhoan tk
			        LEFT JOIN donhang dh ON tk.id = dh.taiKhoanId
			        GROUP BY tk.id
			         ORDER BY tk.id DESC
			        """, 
			        countQuery = """
			        SELECT COUNT(DISTINCT tk.id)
			        FROM taikhoan tk
			        LEFT JOIN donhang dh ON tk.id = dh.taiKhoanId
			        """,
			        nativeQuery = true)
			    Page<Object[]> findUsersWithOrderStats(Pageable pageable);
			    
			    //Lấy user và địa chỉ theo id
			    @Query(value = """
			            SELECT 
			                tk.*,
			                dc.id AS diaChiId,
			                dc.soDienThoai AS diaChiSoDienThoai,
			                dc.toanBoDiaChi
			            FROM 
			                taikhoan tk
			            LEFT JOIN 
			                diachi dc ON tk.id = dc.taiKhoanId
			            WHERE 
			                tk.id = :id
			            """, nativeQuery = true)
			        List<Object[]> findAccountWithAddressesById(@Param("id") Integer id);
			        
			        @Modifying
			        @Transactional
			        @Query(value = "UPDATE taikhoan SET trangThai = :trangThai WHERE id = :id", nativeQuery = true)
			    	int updateTrangThaiById(@Param("id") Integer id, @Param("trangThai") String trangThai);
			        
			        @Query(value = "SELECT " +
				               "tk.id AS id, " +
				               "tk.email AS email, " +
				               "tk.tenTaiKhoan AS tenTaiKhoan, " +
				               "tk.soDienThoai AS soDienThoai, " +
				               "tk.hoVaTen AS hoVaTen, " +
				               "tk.hinhAnh, " +
				               "tk.trangThai, " +
				               "COUNT(dh.id) AS soLuongDonHang, " +
				               "COALESCE(SUM(dh.tongTien), 0) AS tongGiaTriDonHang " +
				               "FROM taikhoan tk " +
				               "LEFT JOIN donhang dh ON tk.id = dh.taiKhoanId " +
				               "WHERE (:trangThai IS NULL OR tk.trangThai LIKE :trangThai) " +
				               "AND (:sdt IS NULL OR tk.soDienThoai LIKE CONCAT('%', :sdt, '%')) " +
				               "GROUP BY tk.id, tk.email, tk.tenTaiKhoan, tk.soDienThoai, tk.hoVaTen, tk.hinhAnh, tk.trangThai " +
				               "ORDER BY tk.id DESC", 
				       countQuery = "SELECT COUNT(DISTINCT tk.id) " +
				                    "FROM taikhoan tk " +
				                    "LEFT JOIN donhang dh ON tk.id = dh.taiKhoanId " +
				                    "WHERE (:trangThai IS NULL OR tk.trangThai = :trangThai) " +
				                    "AND (:sdt IS NULL OR tk.soDienThoai LIKE CONCAT(%:sdt%))",
				       nativeQuery = true)
				Page<Object[]> FillterUsersWithOrderStats(@Param("trangThai") String trangThai,
				                                          @Param("sdt") String sdt,
				                                          Pageable pageable);
	
}
