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

    public Customer getCustomer(String email, String password){
        Customer customer = null;
        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {

            PreparedStatement stm = con.prepareStatement(
                    "SELECT customer.id, firstname, lastname, address, postcode, " +
                            "(SELECT orders.id FROM shoeshop.orders " +
                            "WHERE orders.customer_id = customer.id AND orders.isActive = TRUE " +
                            "ORDER BY orders.order_time DESC LIMIT 1) AS activeOrder " +
                            "FROM shoeshop.customer WHERE email = ? AND password = ? LIMIT 1");
            stm.setString(1,email);
            stm.setString(2,password);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("firstname") + " " + rs.getString("lastname");
                String address = rs.getString("address");
                int postcode = rs.getInt("postcode");
                int activeOrder = rs.getInt("activeOrder");
                customer = new Customer(id,name,address,postcode,email,activeOrder);
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
