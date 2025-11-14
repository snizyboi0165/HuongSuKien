//Người làm: Ngô Văn Thành
package dao;

import connectDB.DatabaseConnection;
import entity.SanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SanPham_DAO {

	public SanPham_DAO() {
	}

	private SanPham mapRowToSanPham(ResultSet rs) throws SQLException {
		return new SanPham(rs.getString("MaSanPham"), rs.getNString("TenSanPham"), rs.getDouble("GiaBan"),
				rs.getNString("Loai"), rs.getNString("MoTa"), rs.getNString("TrangThai"));
	}

	public List<SanPham> getAllSanPham() throws SQLException {
		List<SanPham> dsSP = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM SanPham";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dsSP.add(mapRowToSanPham(rs));
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
		return dsSP;
	}

	public List<SanPham> getSanPhamConHang() throws SQLException {
		List<SanPham> dsSP = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM SanPham WHERE TrangThai = N'Còn hàng'";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dsSP.add(mapRowToSanPham(rs));
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
		return dsSP;
	}

	public List<SanPham> getSanPhamTheoLoai(String loai) throws SQLException {
		if (loai == null || loai.equalsIgnoreCase("Tất cả")) {
			return getSanPhamConHang();
		}

		List<SanPham> dsSP = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM SanPham WHERE TrangThai = N'Còn hàng' AND Loai = ?";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, loai);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dsSP.add(mapRowToSanPham(rs));
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
		return dsSP;
	}

	public List<SanPham> findSanPhamByMaOrTen(String keyword) throws SQLException {
		List<SanPham> dsSP = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM SanPham WHERE TrangThai = N'Còn hàng' AND (MaSanPham LIKE ? OR TenSanPham LIKE ?)";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setNString(2, "%" + keyword + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dsSP.add(mapRowToSanPham(rs));
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
		return dsSP;
	}

	public boolean themSanPham(SanPham sp) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		String sql = "INSERT INTO SanPham (MaSanPham, TenSanPham, GiaBan, Loai, MoTa, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sp.getMaSanPham());
			pstmt.setNString(2, sp.getTenSanPham());
			pstmt.setDouble(3, sp.getGiaBan());
			pstmt.setNString(4, sp.getLoai());
			pstmt.setNString(5, sp.getMoTa());
			pstmt.setNString(6, sp.getTrangThai());

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

	public boolean capNhatSanPham(SanPham sp) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		String sql = "UPDATE SanPham SET TenSanPham = ?, GiaBan = ?, Loai = ?, MoTa = ?, TrangThai = ? WHERE MaSanPham = ?";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, sp.getTenSanPham());
			pstmt.setDouble(2, sp.getGiaBan());
			pstmt.setNString(3, sp.getLoai());
			pstmt.setNString(4, sp.getMoTa());
			pstmt.setNString(5, sp.getTrangThai());
			pstmt.setString(6, sp.getMaSanPham());

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

	public boolean xoaSanPham(String maSP) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		String sql = "DELETE FROM SanPham WHERE MaSanPham = ?";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maSP);
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
	public List<SanPham> timKiemNangCao(String keyword, String loai) throws SQLException {
        List<SanPham> dsSP = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        StringBuilder sql = new StringBuilder("SELECT * FROM SanPham WHERE 1=1");
        
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (MaSanPham LIKE ? OR TenSanPham LIKE ?)");
        }
        if (loai != null && !loai.isEmpty() && !loai.equalsIgnoreCase("Tất cả")) {
            sql.append(" AND Loai = ?");
        }
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (keyword != null && !keyword.isEmpty()) {
                pstmt.setNString(paramIndex++, "%" + keyword + "%");
                pstmt.setNString(paramIndex++, "%" + keyword + "%");
            }
            if (loai != null && !loai.isEmpty() && !loai.equalsIgnoreCase("Tất cả")) {
                pstmt.setNString(paramIndex++, loai);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                dsSP.add(mapRowToSanPham(rs));
            }
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return dsSP;
    }
}