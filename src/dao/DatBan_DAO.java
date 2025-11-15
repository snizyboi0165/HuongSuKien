//Người làm: Nguyễn Thành Đạt
package dao;

import connectDB.DatabaseConnection;
import entity.Ban;
import entity.DatBan;
import entity.KhachHang;
import entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatBan_DAO {

	public DatBan_DAO() {
	}

	public String taoDatBan(String maBan, String maKhachHang, String maNhanVien, String sdtKhach, String thoiGianDat,
			int soLuongNguoi, String ghiChu) throws SQLException {

		String maDB = "DB" + System.currentTimeMillis() % 1000000;

		Timestamp thoiGianDatSQL = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
			Date parsedDate = dateFormat.parse(thoiGianDat);
			thoiGianDatSQL = new java.sql.Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			System.err.println("Lỗi định dạng ngày giờ: " + thoiGianDat);
			e.printStackTrace();
			return null;
		}

		String sql = """
				INSERT INTO DatBan (MaDatBan, MaBan, MaKhachHang, MaNhanVien, SoDienThoai, ThoiGianDat, SoLuongNguoi, TrangThai, GhiChu)
				VALUES (?, ?, ?, ?, ?, ?, ?, N'Đã xác nhận', ?)
				""";

		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maDB);
			pstmt.setString(2, maBan);

			if (maKhachHang != null && !maKhachHang.isEmpty()) {
				pstmt.setString(3, maKhachHang);
			} else {
				pstmt.setNull(3, java.sql.Types.VARCHAR);
			}

			pstmt.setString(4, maNhanVien);
			pstmt.setString(5, sdtKhach);
			pstmt.setTimestamp(6, thoiGianDatSQL);
			pstmt.setInt(7, soLuongNguoi);
			pstmt.setNString(8, ghiChu);

			n = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return (n > 0) ? maDB : null;
	}

	public DatBan getDatBanByMaBan(String maBan, String trangThai) throws SQLException {
		DatBan db = null;
		String sql = "SELECT TOP 1 * FROM DatBan WHERE MaBan = ? AND TrangThai = ? ORDER BY ThoiGianDat DESC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maBan);
			pstmt.setNString(2, trangThai);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				db = new DatBan();
				db.setMaDatBan(rs.getString("MaDatBan"));
				db.setBan(new Ban(rs.getString("MaBan")));
				db.setNhanVien(new NhanVien(rs.getString("MaNhanVien")));
				db.setSoDienThoai(rs.getString("SoDienThoai"));
				db.setThoiGianDat(rs.getTimestamp("ThoiGianDat").toLocalDateTime());
				db.setSoLuongNguoi(rs.getInt("SoLuongNguoi"));
				db.setTrangThai(rs.getNString("TrangThai"));

				String maKH = rs.getString("MaKhachHang");
				if (maKH != null) {
					db.setKhachHang(new KhachHang(maKH));
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
		return db;
	}
	
	public boolean updateTrangThaiDatBan(String maDatBan, String trangThaiMoi) throws SQLException {
		String sql = "UPDATE DatBan SET TrangThai = ? WHERE MaDatBan = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, trangThaiMoi);
			pstmt.setString(2, maDatBan);
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
}