//Người làm: Ngô Văn Thành
package dao;

import connectDB.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_DAO {

	public ChiTietHoaDon_DAO() {
	}

	public List<Object[]> findAllChiTietByMaHoaDon(String maHoaDon) throws SQLException {
		List<Object[]> chiTietList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = """
				SELECT sp.TenSanPham, cthd.SoLuong, cthd.DonGia
				FROM ChiTietHoaDon AS cthd
				JOIN SanPham AS sp ON cthd.MaSanPham = sp.MaSanPham
				WHERE cthd.MaHoaDon = ?
				""";
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maHoaDon);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String tenSP = rs.getNString("TenSanPham");
				int soLuong = rs.getInt("SoLuong");
				double donGia = rs.getDouble("DonGia");
				chiTietList.add(new Object[] { tenSP, soLuong, donGia });
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
		return chiTietList;
	}

	public boolean themHoacCapNhatChiTiet(String maHoaDon, String maSanPham, int soLuong, double donGia)
			throws SQLException {
		String sqlCheck = "SELECT SoLuong FROM ChiTietHoaDon WHERE MaHoaDon = ? AND MaSanPham = ?";
		String sqlUpdate = "UPDATE ChiTietHoaDon SET SoLuong = SoLuong + ?, DonGia = ? WHERE MaHoaDon = ? AND MaSanPham = ?";
		String sqlInsert = "INSERT INTO ChiTietHoaDon (MaHoaDon, MaSanPham, SoLuong, DonGia) VALUES (?, ?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmtCheck = null;
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtInsert = null;
		ResultSet rsCheck = null;
		boolean success = false;

		try {
			conn = DatabaseConnection.getInstance().getConnection();

			pstmtCheck = conn.prepareStatement(sqlCheck);
			pstmtCheck.setString(1, maHoaDon);
			pstmtCheck.setString(2, maSanPham);
			rsCheck = pstmtCheck.executeQuery();

			if (rsCheck.next()) {

				pstmtUpdate = conn.prepareStatement(sqlUpdate);
				pstmtUpdate.setInt(1, soLuong);
				pstmtUpdate.setDouble(2, donGia);
				pstmtUpdate.setString(3, maHoaDon);
				pstmtUpdate.setString(4, maSanPham);
				success = pstmtUpdate.executeUpdate() > 0;
			} else {

				pstmtInsert = conn.prepareStatement(sqlInsert);
				pstmtInsert.setString(1, maHoaDon);
				pstmtInsert.setString(2, maSanPham);
				pstmtInsert.setInt(3, soLuong);
				pstmtInsert.setDouble(4, donGia);
				success = pstmtInsert.executeUpdate() > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rsCheck != null)
				rsCheck.close();
			if (pstmtCheck != null)
				pstmtCheck.close();
			if (pstmtUpdate != null)
				pstmtUpdate.close();
			if (pstmtInsert != null)
				pstmtInsert.close();
		}
		return success;
	}
}