//Người làm: Nguyễn Cao Việt An
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.DatabaseConnection;
import entity.TaiKhoan;

public class TaiKhoan_DAO {

	public TaiKhoan_DAO() {
	}

	public TaiKhoan kiemTraDangNhap(String tenDangNhap, String matKhau) throws SQLException {
		TaiKhoan taiKhoan = null;
		String sql = "SELECT MaTaiKhoan, MaNhanVien, MaChucVu FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, tenDangNhap);
			pstmt.setString(2, matKhau);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				String maTaiKhoan = rs.getString("MaTaiKhoan");
				String maNhanVien = rs.getString("MaNhanVien");
				String maChucVu = rs.getString("MaChucVu");

				taiKhoan = new TaiKhoan(maTaiKhoan, tenDangNhap, maNhanVien, maChucVu);
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
		return taiKhoan;
	}

	public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) throws SQLException {
		String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TenDangNhap = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, matKhauMoi);
			pstmt.setString(2, tenDangNhap);
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
	
	public boolean kiemTraTonTai(String tenDangNhap) throws SQLException {
		String sql = "SELECT 1 FROM TaiKhoan WHERE TenDangNhap = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean tonTai = false;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tenDangNhap);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				tonTai = true;
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
		return tonTai;
	}
}