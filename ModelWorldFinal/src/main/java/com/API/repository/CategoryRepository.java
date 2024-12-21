package com.API.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.QueryCacheLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.API.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query(value = "SELECT trangThai FROM theLoai WHERE id= :parentId", nativeQuery = true)
	Optional<Integer> findByIdCategoryId(@Param("parentId") int parentId);

	@Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
	List<Category> findAllParents();

	@Query(value = "SELECT id, tenLoai, anhLoai FROM theloai WHERE trangthai = 1 AND parent_Id IS NULL", nativeQuery = true)
	List<Object[]> getLoaiSanPhamHome();

	// NAM
	@Query(value = "SELECT id, tenLoai FROM theloai WHERE trangthai = 1 AND parent_Id IS NULL", nativeQuery = true)
	List<Object[]> getFillterLoaiSanPham();

	Optional<Category> findByTenLoai(String tenLoai);

	@Query(value = """
			    WITH RECURSIVE DanhMucCTE AS (
			        -- Lấy các danh mục gốc (không có danh mục cha)
			        SELECT
			            t.id AS idDanhMuc,
			            t.tenLoai AS tenDanhMuc,
			            t.AnhLoai AS anhDanhMuc,
			            t.trangThai AS trangThaiDanhMuc,
			            CAST('Doanh mục gốc' AS CHAR(255)) AS tenDanhMucCha,
			            0 AS CapDo
			        FROM theloai t
			        WHERE t.parent_Id IS NULL

			        UNION ALL

			        -- Lấy các danh mục con
			        SELECT
			            c.id AS idDanhMuc,
			            c.tenLoai AS tenDanhMuc,
			            c.AnhLoai AS anhDanhMuc,
			            c.trangThai AS trangThaiDanhMuc,
			            p.tenLoai AS tenDanhMucCha,
			            d.CapDo + 1 AS CapDo
			        FROM theloai c
			        INNER JOIN DanhMucCTE d ON c.parent_Id = d.idDanhMuc
			        LEFT JOIN theloai p ON c.parent_Id = p.id
			    )

			    -- Truy vấn kết quả từ CTE
			    SELECT
			        idDanhMuc,
			        tenDanhMuc,
			        anhDanhMuc,
			        trangThaiDanhMuc,
			        tenDanhMucCha,
			        CapDo
			    FROM DanhMucCTE
			    ORDER BY CapDo, idDanhMuc
			    LIMIT :pageSize OFFSET :offset
			""", nativeQuery = true)
	List<Object[]> findDanhMucTheoCayWithPaging(@Param("pageSize") int pageSize, @Param("offset") int offset);

	@Query(value = """
			SELECT COUNT(*)
			FROM theloai
			""", nativeQuery = true)
	long countAllDanhMuc();

	@Query(value = """
			    SELECT id, tenLoai, trangThai, parent_Id
			    FROM theloai
			    WHERE (parent_Id IS NULL OR parent_Id = 0) AND trangthai = 1
			""", nativeQuery = true)
	List<Object[]> findRootCategories();

	@Query(value = "SELECT id, tenLoai, trangThai, parent_Id FROM theloai WHERE parent_Id = :id AND trangthai = 1", nativeQuery = true)
	List<Object[]> findSubCategories(@Param("id") Integer parentId);

	@Query(value = """
			WITH RECURSIVE DanhMucCTE AS (
			    -- Lấy các danh mục gốc (không có danh mục cha)
			    SELECT
			        t.id AS idDanhMuc,
			        t.tenLoai AS tenDanhMuc,
			        t.AnhLoai AS anhDanhMuc,
			        t.trangThai AS trangThaiDanhMuc,
			       CAST('Doanh mục gốc' AS CHAR(255)) AS tenDanhMucCha,
			        0 AS CapDo
			    FROM theloai t
			    WHERE t.parent_Id IS NULL

			    UNION ALL

			    -- Lấy các danh mục con
			    SELECT
			        c.id AS idDanhMuc,
			        c.tenLoai AS tenDanhMuc,
			        c.AnhLoai AS anhDanhMuc,
			        c.trangThai AS trangThaiDanhMuc,
			        p.tenLoai AS tenDanhMucCha,
			        d.CapDo + 1 AS CapDo
			    FROM theloai c
			    INNER JOIN DanhMucCTE d ON c.parent_Id = d.idDanhMuc
			    LEFT JOIN theloai p ON c.parent_Id = p.id
			)

			-- Truy vấn kết quả từ CTE
			SELECT
			    idDanhMuc,
			    tenDanhMuc,
			    anhDanhMuc,
			    trangThaiDanhMuc,
			    tenDanhMucCha,
			    CapDo
			FROM DanhMucCTE
			WHERE tenDanhMuc LIKE CONCAT('%', :name, '%')
			ORDER BY CapDo, idDanhMuc
			LIMIT :pageSize OFFSET :offset
			""", nativeQuery = true)
	List<Object[]> fillterDanhMucTheoCayWithPaging(@Param("name") String name, @Param("pageSize") int pageSize,
			@Param("offset") int offset);

	@Query(value = """
			WITH RECURSIVE DanhMucCTE AS (
			    SELECT
			        t.id AS idDanhMuc,
			        t.tenLoai AS tenDanhMuc,
			        t.AnhLoai AS anhDanhMuc,
			        t.trangThai AS trangThaiDanhMuc,
			         CAST('Doanh mục gốc' AS CHAR(255)) AS tenDanhMucCha,
			        0 AS CapDo
			    FROM theloai t
			    WHERE t.parent_Id IS NULL

			    UNION ALL

			    SELECT
			        c.id AS idDanhMuc,
			        c.tenLoai AS tenDanhMuc,
			        c.AnhLoai AS anhDanhMuc,
			        c.trangThai AS trangThaiDanhMuc,
			        p.tenLoai AS tenDanhMucCha,
			        d.CapDo + 1 AS CapDo
			    FROM theloai c
			    INNER JOIN DanhMucCTE d ON c.parent_Id = d.idDanhMuc
			    LEFT JOIN theloai p ON c.parent_Id = p.id
			)
			SELECT COUNT(*)
			FROM DanhMucCTE
			WHERE tenDanhMuc LIKE CONCAT('%', :name, '%')
			""", nativeQuery = true)
	int countDanhMucTheoCay(@Param("name") String name);
}
