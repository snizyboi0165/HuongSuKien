//Người làm: Nguyễn Thành Đạt
package dao;

import connectDB.DatabaseConnection;
import entity.KhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_DAO {

	public KhuyenMai_DAO() {
	}

	private KhuyenMai mapRowToKhuyenMai(ResultSet rs) throws SQLException {
		return new KhuyenMai(rs.getString("MaKhuyenMai"), rs.getNString("TenKhuyenMai"),
				rs.getNString("DieuKienApDung"), rs.getDouble("PhanTramGiam"),
				rs.getTimestamp("NgayBatDau").toLocalDateTime(), rs.getTimestamp("NgayKetThuc").toLocalDateTime());
	}

	public List<KhuyenMai> getAll() throws SQLException {
		List<KhuyenMai> dsKM = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM KhuyenMai";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				dsKM.add(mapRowToKhuyenMai(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return dsKM;
	}

	public boolean themKhuyenMai(KhuyenMai km) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		String sql = "INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, PhanTramGiam, NgayBatDau, NgayKetThuc, DieuKienApDung) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, km.getMaKhuyenMai());
			pstmt.setNString(2, km.getTenKhuyenMai());
			pstmt.setDouble(3, km.getPhanTramGiam());
			pstmt.setTimestamp(4, Timestamp.valueOf(km.getNgayBatDau()));
			pstmt.setTimestamp(5, Timestamp.valueOf(km.getNgayKetThuc()));
			pstmt.setNString(6, km.getDieuKienApDung());

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

	public boolean capNhatKhuyenMai(KhuyenMai km) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		String sql = "UPDATE KhuyenMai SET TenKhuyenMai = ?, PhanTramGiam = ?, NgayBatDau = ?, NgayKetThuc = ?, DieuKienApDung = ? WHERE MaKhuyenMai = ?";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, km.getTenKhuyenMai());
			pstmt.setDouble(2, km.getPhanTramGiam());
			pstmt.setTimestamp(3, Timestamp.valueOf(km.getNgayBatDau()));
			pstmt.setTimestamp(4, Timestamp.valueOf(km.getNgayKetThuc()));
			pstmt.setNString(5, km.getDieuKienApDung());
			pstmt.setString(6, km.getMaKhuyenMai());

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

	public boolean xoaKhuyenMai(String maKM) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int n = 0;
		String sql = "DELETE FROM KhuyenMai WHERE MaKhuyenMai = ?";

		try {
			conn = DatabaseConnection.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, maKM);
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
	
	public KhuyenMai findKhuyenMaiTuDong(int diemTichLuy) throws SQLException {
        KhuyenMai km = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT TOP 1 * FROM KhuyenMai " +
                     "WHERE NgayBatDau <= GETDATE() AND NgayKetThuc >= GETDATE() " +
                     "AND DieuKienApDung LIKE N'Khách hàng trên % điểm tích lũy' " +
                     "ORDER BY PhanTramGiam DESC"; 
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String dieuKien = rs.getNString("DieuKienApDung");
                try {
                    String[] parts = dieuKien.split(" ");
                    int diemYeuCau = 0;
                    for(int i = 0; i < parts.length; i++) {
                        if (parts[i].equalsIgnoreCase("trên") && i + 1 < parts.length) {
                             diemYeuCau = Integer.parseInt(parts[i+1].replace("điểm", "").replace("DTL", "").replace("BTL", ""));
                             break;
                        }
                    }
                    if (diemTichLuy >= diemYeuCau) {
                        return mapRowToKhuyenMai(rs);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Lỗi phân tích điều kiện khuyến mãi: " + dieuKien);
                    continue;
                }
            }
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
             if(rs != null) rs.close();
             if(pstmt != null) pstmt.close();
        }
        return null;
    }
	public KhuyenMai getKhuyenMaiByID(String maKM) throws SQLException {
	    KhuyenMai km = null;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = "SELECT * FROM KhuyenMai WHERE MaKhuyenMai = ?";
	    
	    try {
	        conn = DatabaseConnection.getInstance().getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, maKM);
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	             km = mapRowToKhuyenMai(rs);
	        }
	    } catch (SQLException e) {
	         e.printStackTrace();
	         throw e;
	    } finally {
	         if(rs != null) rs.close();
	         if(pstmt != null) pstmt.close();
	    }
	    return km;
	}
}