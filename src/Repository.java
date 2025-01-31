import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Repository {
    Properties p = new Properties();

    public Repository() {
        try {
            p.load(new FileInputStream("src/database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCustomerID(String email, String password){
        int customerID = 0;
        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {

            PreparedStatement stm = con.prepareStatement(
                    "SELECT id FROM shoeshop.customer WHERE email = ? AND password = ?");
            stm.setString(1,email);
            stm.setString(2,password);

            ResultSet rs = stm.executeQuery();

            while (rs.next())
                customerID = rs.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerID;
    }
}
