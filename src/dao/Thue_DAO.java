//Người làm: Nguyễn Cao Việt An
package dao;

import connectDB.DatabaseConnection;
import entity.Thue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Thue_DAO {
	
	public Thue_DAO() {
		
	}

    public Thue getThueMacDinh() throws SQLException {
        Thue thue = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT TOP 1 MaThue, TenThue, TyLeThue, MoTa FROM Thue WHERE TyLeThue > 0";

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                thue = new Thue(
                    rs.getString("MaThue"),
                    rs.getNString("TenThue"),
                    rs.getDouble("TyLeThue"),
                    rs.getNString("MoTa")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return thue;
    }
    public Thue getThueByID(String maThue) throws SQLException {
        Thue thue = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT MaThue, TenThue, TyLeThue, MoTa FROM Thue WHERE MaThue = ?";

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maThue);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                thue = new Thue(
                    rs.getString("MaThue"),
                    rs.getNString("TenThue"),
                    rs.getDouble("TyLeThue"),
                    rs.getNString("MoTa")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return thue;
    }
}