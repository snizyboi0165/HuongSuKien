//Người làm: Ngô Văn Thành
package dao;

import connectDB.DatabaseConnection;
import entity.Ban;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.Thue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {

	public HoaDon_DAO() {
	}

	public boolean taoHoaDonMoi(String maBan, String maNhanVien, String maKhachHang) throws SQLException {
		String maHD = "HD" + System.currentTimeMillis() % 1000000;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;

		String sql = "INSERT INTO HoaDon (MaHoaDon, NgayTao, TrangThai, MaNhanVien, MaBan, MaKhachHang) "
				+ "VALUES (?, GETDATE(), N'Chưa thanh toán', ?, ?, ?)";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maHD);
			pstmt.setString(2, maNhanVien);
			pstmt.setString(3, maBan);

			if (maKhachHang != null && !maKhachHang.isEmpty()) {
				pstmt.setString(4, maKhachHang);
			} else {
				pstmt.setNull(4, java.sql.Types.VARCHAR);
			}

			n = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return n > 0;
	}

	public HoaDon findActiveByMaBan(String maBan) throws SQLException {
		HoaDon hd = null;
		String sql = "SELECT * FROM HoaDon WHERE MaBan = ? AND TrangThai = N'Chưa thanh toán'";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maBan);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				hd = new HoaDon();
				hd.setMaHoaDon(rs.getString("MaHoaDon"));
				hd.setNgayTao(rs.getTimestamp("NgayTao").toLocalDateTime());
				hd.setTrangThai(rs.getNString("TrangThai"));

				hd.setNhanVien(new NhanVien(rs.getString("MaNhanVien")));
				hd.setBan(new Ban(rs.getString("MaBan")));

				String maKH = rs.getString("MaKhachHang");
				if (maKH != null) {
					hd.setKhachHang(new KhachHang(maKH));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return hd;
	}

	public boolean updateTrangThai(String maHoaDon, String trangThaiMoi) throws SQLException {
		String sql = "UPDATE HoaDon SET TrangThai = ? WHERE MaHoaDon = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, trangThaiMoi);
			pstmt.setString(2, maHoaDon);
			n = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return n > 0;
	}
	
	public boolean updateMaThueAndMaKhuyenMai(String maHoaDon, String maThue, String maKhuyenMai) throws SQLException {
        String sql = "UPDATE HoaDon SET MaThue = ?, MaKhuyenMai = ? WHERE MaHoaDon = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int n = 0;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            
            if (maThue != null && !maThue.isEmpty()) {
                pstmt.setString(1, maThue);
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }

            if (maKhuyenMai != null && !maKhuyenMai.isEmpty()) {
                pstmt.setString(2, maKhuyenMai);
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            
            pstmt.setString(3, maHoaDon);
            
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (pstmt != null) pstmt.close();
        }
        return n > 0;
    }

	public List<Object[]> timKiemNangCao(String ma, String hoTen, String tenSP) throws SQLException {
		List<Object[]> dsHoaDon = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			StringBuilder sql = new StringBuilder("SELECT DISTINCT " 
				    + "  hd.MaHoaDon, hd.NgayTao, "
				    + "  ISNULL((SELECT SUM(cthd_sum.DonGia * cthd_sum.SoLuong) FROM ChiTietHoaDon cthd_sum WHERE cthd_sum.MaHoaDon = hd.MaHoaDon), 0.0) AS TongTienTinhToan, "
				    + "  ISNULL(nv.HoTen, N'N/A') AS TenNhanVien, "
				    + "  ISNULL(kh.TenKhachHang, N'Khách vãng lai') AS TenKhachHang, "
					+ "  (SELECT STRING_AGG(sp.TenSanPham, N', ') " + "   FROM ChiTietHoaDon cthd "
					+ "   JOIN SanPham sp ON cthd.MaSanPham = sp.MaSanPham "
					+ "   WHERE cthd.MaHoaDon = hd.MaHoaDon) AS DanhSachSanPham " + "FROM HoaDon hd "
					+ "LEFT JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien "
					+ "LEFT JOIN KhachHang kh ON hd.MaKhachHang = kh.MaKhachHang "
					+ "LEFT JOIN ChiTietHoaDon cthd_search ON hd.MaHoaDon = cthd_search.MaHoaDon "
					+ "LEFT JOIN SanPham sp_search ON cthd_search.MaSanPham = sp_search.MaSanPham " + "WHERE 1=1");

			if (ma != null && !ma.isEmpty())
				sql.append(" AND (hd.MaHoaDon LIKE ? OR hd.MaKhachHang LIKE ?)");
			if (hoTen != null && !hoTen.isEmpty())
				sql.append(" AND (kh.TenKhachHang LIKE ? OR nv.HoTen LIKE ?)");
			if (tenSP != null && !tenSP.isEmpty())
				sql.append(" AND sp_search.TenSanPham LIKE ?");

			sql.append(" ORDER BY hd.NgayTao DESC");

			pstmt = conn.prepareStatement(sql.toString());

			int paramIndex = 1;
			if (ma != null && !ma.isEmpty()) {
				pstmt.setString(paramIndex++, "%" + ma + "%");
				pstmt.setString(paramIndex++, "%" + ma + "%");
			}
			if (hoTen != null && !hoTen.isEmpty()) {
				pstmt.setNString(paramIndex++, "%" + hoTen + "%");
				pstmt.setNString(paramIndex++, "%" + hoTen + "%");
			}
			if (tenSP != null && !tenSP.isEmpty()) {
				pstmt.setNString(paramIndex++, "%" + tenSP + "%");
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				String maHD_rs = rs.getString("MaHoaDon");
				Timestamp ngayTao = rs.getTimestamp("NgayTao");
				String tenNV = rs.getNString("TenNhanVien");
				String tenKHang = rs.getNString("TenKhachHang");
				String dsSP = rs.getNString("DanhSachSanPham");
				double tongTien = rs.getDouble("TongTienTinhToan");

				dsHoaDon.add(new Object[] { maHD_rs, ngayTao, tenNV, tenKHang, dsSP, tongTien });
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return dsHoaDon;
	}
	public List<Object[]> layDoanhThuChiTietTheoKhoangThoiGian(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) throws SQLException {
	    List<Object[]> dsHoaDon = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    
	    String sql = "SELECT " +
	        "hd.MaHoaDon, hd.NgayTao, ISNULL(kh.MaKhachHang, 'VANG LAI') AS MaKhachHang, " +
	        "ISNULL(kh.TenKhachHang, N'Khách vãng lai') AS TenKhachHang, " +
	        "(SELECT SUM(cthd_sum.DonGia * cthd_sum.SoLuong) FROM ChiTietHoaDon cthd_sum WHERE cthd_sum.MaHoaDon = hd.MaHoaDon) AS TongThanhToan, " +
	        "ISNULL(nv.HoTen, N'N/A') AS TenNhanVien " +
	        "FROM HoaDon hd " +
	        "LEFT JOIN KhachHang kh ON hd.MaKhachHang = kh.MaKhachHang " +
	        "LEFT JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien " +
	        "WHERE hd.TrangThai = N'Đã thanh toán' AND hd.NgayTao BETWEEN ? AND ? " +
	        "ORDER BY hd.NgayTao ASC";

	    try {
	        conn = DatabaseConnection.getInstance().getConnection();
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setTimestamp(1, Timestamp.valueOf(ngayBatDau));
	        pstmt.setTimestamp(2, Timestamp.valueOf(ngayKetThuc));
	        
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            double tongTien = rs.getDouble("TongThanhToan");
	            
	            dsHoaDon.add(new Object[]{
	                rs.getString("MaHoaDon"),
	                rs.getTimestamp("NgayTao"),
	                rs.getString("MaKhachHang"),
	                rs.getNString("TenKhachHang"),
	                tongTien,
	                rs.getNString("TenNhanVien")
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        if (rs != null) rs.close();
	        if (pstmt != null) pstmt.close();
	    }
	    return dsHoaDon;
	}
	public HoaDon findHoaDonByID(String maHoaDon) throws SQLException {
	    HoaDon hd = null;
	    String sql = "SELECT * FROM HoaDon WHERE MaHoaDon = ?";
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        conn = DatabaseConnection.getInstance().getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, maHoaDon);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            hd = new HoaDon();
	            hd.setMaHoaDon(rs.getString("MaHoaDon"));
	            hd.setNgayTao(rs.getTimestamp("NgayTao").toLocalDateTime());
	            hd.setTrangThai(rs.getNString("TrangThai"));
	            
	            hd.setNhanVien(new NhanVien(rs.getString("MaNhanVien")));
	            hd.setBan(new Ban(rs.getString("MaBan")));
	            
	            String maKH = rs.getString("MaKhachHang");
	            if (maKH != null) hd.setKhachHang(new KhachHang(maKH));

	            String maThue = rs.getString("MaThue");
	            if (maThue != null) hd.setThue(new Thue(maThue));
	            
	            String maKM = rs.getString("MaKhuyenMai");
	            if (maKM != null) hd.setKhuyenMai(new KhuyenMai(maKM));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        if (rs != null) rs.close();
	        if (pstmt != null) pstmt.close();
	    }
	    return hd;
	}
}