import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
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
            throw new RuntimeException(e); //TODO: fixa errorhantering
        }
        return customerID;
    }

    public Customer getCustomer(String email, String password){
        Customer customer = null;
        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {

            PreparedStatement stm = con.prepareStatement(
                    "SELECT id, firstname, lastname, address, postcode FROM shoeshop.customer " +
                            "WHERE email = ? AND password = ?");
            stm.setString(1,email);
            stm.setString(2,password);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("firstname") + " " + rs.getString("lastname");
                String address = rs.getString("address");
                int postcode = rs.getInt("postcode");
                customer = new Customer(id,name,address,postcode,email);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: fixa errorhantering
        }
        return customer;
    }

    public ArrayList<Product> getProducts(){
        ArrayList<Product> prodList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {

            PreparedStatement stm = con.prepareStatement(
                    "SELECT p.id, brand.name as brand, m.name as model, m.description, p.size, p.colour " +
                            "FROM shoeshop.brand " +
                            "INNER JOIN shoeshop.model m on brand.id = m.brand_id " +
                            "INNER JOIN shoeshop.product p on m.id = p.model_id WHERE p.stock != 0;");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String description = rs.getString("description");
                int size = rs.getInt("size");
                String colour = rs.getString("colour");
                Product product = new Product(id,brand,model,description,size,colour);
                prodList.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: fixa errorhantering
        }
        return prodList;
    }
}
