//Người làm: Nguyễn Thành Đạt
package dao;

import connectDB.DatabaseConnection; 
import entity.KhachHang; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {

	public KhachHang_DAO() {
	}
	
	public List<KhachHang> getAllKhachHang() throws SQLException {
		List<KhachHang> dsKhachHang = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM KhachHang";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String ma = rs.getString("MaKhachHang");
                String tenKH = rs.getNString("TenKhachHang");
                String phone = rs.getString("SoDienThoai");
                int diem = rs.getInt("DiemTichLuy");
                
                dsKhachHang.add(new KhachHang(ma, tenKH, diem, phone));
            }
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return dsKhachHang;
	}

    public List<KhachHang> timKiemNangCao(String maKH, String hoTen, String sdt) throws SQLException {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM KhachHang WHERE 1=1");
            
            if (maKH != null && !maKH.isEmpty()) 
                sql.append(" AND MaKhachHang LIKE ?");
            if (hoTen != null && !hoTen.isEmpty()) 
                sql.append(" AND TenKhachHang LIKE ?");
            if (sdt != null && !sdt.isEmpty()) 
                sql.append(" AND SoDienThoai LIKE ?");

            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (maKH != null && !maKH.isEmpty())
                pstmt.setString(paramIndex++, "%" + maKH + "%");
            if (hoTen != null && !hoTen.isEmpty())
                pstmt.setNString(paramIndex++, "%" + hoTen + "%");
            if (sdt != null && !sdt.isEmpty())
                pstmt.setString(paramIndex++, "%" + sdt + "%");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                String ma = rs.getString("MaKhachHang");
                String tenKH = rs.getNString("TenKhachHang");
                String phone = rs.getString("SoDienThoai");
                int diem = rs.getInt("DiemTichLuy");
                
                dsKhachHang.add(new KhachHang(ma, tenKH, diem, phone));
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            throw e; 
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return dsKhachHang;
    }
    
    public boolean capNhatKhachHang(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET TenKhachHang = ?, SoDienThoai = ?, DiemTichLuy = ? WHERE MaKhachHang = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int n = 0;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setNString(1, kh.getTenKhachHang());
            pstmt.setString(2, kh.getSoDienThoai());
            pstmt.setInt(3, kh.getDiemTichLuy());
            pstmt.setString(4, kh.getMaKhachHang());
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
             if (pstmt != null) pstmt.close();
        }
        return n > 0;
    }
    
    public boolean congDiemTichLuy(String maKhachHang, int diemCong) throws SQLException {
        String sql = "UPDATE KhachHang SET DiemTichLuy = DiemTichLuy + ? WHERE MaKhachHang = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int n = 0;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, diemCong);
            pstmt.setString(2, maKhachHang);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (pstmt != null) pstmt.close();
        }
        return n > 0;
    }
    
    public KhachHang findBySDT(String sdt) throws SQLException {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                kh = new KhachHang(
                    rs.getString("MaKhachHang"),
                    rs.getNString("TenKhachHang"),
                    rs.getInt("DiemTichLuy"),
                    rs.getString("SoDienThoai")
                );
            }
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return kh;
    }

    public KhachHang taoKhachHangMoi(String tenKH, String sdt) throws SQLException {
        String maKH = "KH" + (System.currentTimeMillis() % 100000);
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO KhachHang (MaKhachHang, TenKhachHang, SoDienThoai, DiemTichLuy) " +
                     "VALUES (?, ?, ?, 0)"; 
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            pstmt.setNString(2, tenKH);
            pstmt.setString(3, sdt);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return new KhachHang(maKH, tenKH, 0, sdt);
            }
            return null; 
            
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
    
     public boolean xoaKhachHang(String maKhachHang) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE MaKhachHang = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int n = 0;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKhachHang);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (pstmt != null) pstmt.close();
        }
        return n > 0;
    }
}