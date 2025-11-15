//Người làm: Nguyễn Cao Việt An
package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static DatabaseConnection instance = new DatabaseConnection();
  private Connection con;

 
  private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=quanly_Cafe;encrypt=true;trustServerCertificate=true";
  private static final String USER = "sa";
  private static final String PASSWORD = "sapassword165";

  public static DatabaseConnection getInstance(){
      return instance;
  }

  public Connection getConnection() throws SQLException {
      if (con == null || con.isClosed()) {
          try {
              con = DriverManager.getConnection(URL, USER, PASSWORD);
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
      return con;
  }

  public void closeConnection() {
      if (con != null) {
          try {
              con.close();
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }
}

