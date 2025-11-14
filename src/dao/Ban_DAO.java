//Người làm: Ngô Văn Thành
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.DatabaseConnection;
import entity.Ban;

public class Ban_DAO {

    public Ban_DAO() {
    }

    public List<Ban> getAllBan() throws SQLException {
        List<Ban> dsBan = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM Ban ORDER BY CAST(SUBSTRING(MaBan, 2, LEN(MaBan) - 1) AS INT)";

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String maBan = rs.getString("MaBan");
                String tenBan = rs.getString("TenBan");
                String trangThai = rs.getString("TrangThai");
                
                Ban ban = new Ban(maBan, tenBan, trangThai);
                dsBan.add(ban);
            }
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return dsBan;
    }

    public boolean updateTrangThaiBan(String maBan, String trangThaiMoi) throws SQLException {
        String sql = "UPDATE Ban SET TrangThai = ? WHERE MaBan = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int n = 0;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setNString(1, trangThaiMoi);
            pstmt.setString(2, maBan);

            n = pstmt.executeUpdate();
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
             if (pstmt != null) pstmt.close();
        }
        return n > 0;
    }
    public List<Ban> getBanTheoTrangThai(String trangThai) throws SQLException {
        List<Ban> dsBan = new ArrayList<>();
        String sql = "SELECT * FROM Ban WHERE TrangThai = ? ORDER BY CAST(SUBSTRING(MaBan, 2, LEN(MaBan) - 1) AS INT)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setNString(1, trangThai); 
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Ban ban = new Ban(
                    rs.getString("MaBan"),
                    rs.getNString("TenBan"),
                    rs.getNString("TrangThai")
                );
                dsBan.add(ban);
            }
        } catch (SQLException e) {
             e.printStackTrace();
             throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return dsBan;
    }
}