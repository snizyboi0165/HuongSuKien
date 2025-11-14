//Người làm: Nguyễn Cao Việt An
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.DatabaseConnection;

public class NhanVien_DAO {

	public NhanVien_DAO() {
	}

	public String getTenNhanVien(String maNhanVien) throws SQLException {
		String fullName = null;
		String sql = "SELECT HoTen FROM NhanVien WHERE MaNhanVien = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, maNhanVien);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				fullName = rs.getString("HoTen");

				if (fullName == null || fullName.trim().isEmpty()) {
					fullName = maNhanVien;
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

		return fullName;
	}
}