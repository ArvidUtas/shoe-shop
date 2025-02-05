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
            System.err.println("Fel vid läsning av properties-fil. Avslutar programmet");
            System.exit(1);
        }
    }

    public Customer getCustomer(String email, String password) {
        Customer customer = null;
        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {

            PreparedStatement stm = con.prepareStatement(
                    "SELECT customer.id, firstname, lastname, address, postcode, " +
                            "(SELECT orders.id FROM shoeshop.orders " +
                            "WHERE orders.customer_id = customer.id AND orders.isActive = TRUE " +
                            "ORDER BY orders.order_time DESC LIMIT 1) AS activeOrder " +
                            "FROM shoeshop.customer WHERE email = ? AND password = ? LIMIT 1");
            stm.setString(1, email);
            stm.setString(2, password);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("firstname") + " " + rs.getString("lastname");
                String address = rs.getString("address");
                int postcode = rs.getInt("postcode");
                int activeOrder = rs.getInt("activeOrder");
                customer = new Customer(id, name, address, postcode, email, activeOrder);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid läsning från databas. Error kod: " + e.getErrorCode() + "Försök igen.");
        }
        return customer;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> prodList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {

            PreparedStatement stm = con.prepareStatement(
                    "SELECT p.id, brand.name as brand, m.name as model, m.description, p.size, p.colour, p.price " +
                            "FROM shoeshop.brand " +
                            "INNER JOIN shoeshop.model m ON brand.id = m.brand_id " +
                            "INNER JOIN shoeshop.product p ON m.id = p.model_id WHERE p.stock != 0;");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String description = rs.getString("description");
                int size = rs.getInt("size");
                String colour = rs.getString("colour");
                int price = rs.getInt("price");
                Product product = new Product(id, brand, model, description, size, colour, price);
                prodList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Fel vid läsning från databas. Error kod: " + e.getErrorCode());
        }
        return prodList;
    }

    public String addToCart(Customer customer, int productID) {
        String outcome = "";
        CallableStatement stm;
        try (Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"),
                p.getProperty("password"))) {
            if (customer.getActiveOrder() != 0) {
                stm = con.prepareCall("CALL shoeshop.addToCart(?,?,?,?)");
                stm.setInt(1, customer.getId());
                stm.setInt(2, productID);
                stm.setInt(3, customer.getActiveOrder());
                stm.registerOutParameter(4, Types.INTEGER);
            } else {
                stm = con.prepareCall("CALL shoeshop.addToCart(?,?,null,?)");
                stm.setInt(1, customer.getId());
                stm.setInt(2, productID);
                stm.registerOutParameter(3, Types.INTEGER);
            }
            ResultSet rs = stm.executeQuery();
            int affectedRows = stm.getInt("affectedRows");
            if (affectedRows > 0)
                outcome = "Produkten har lagts till i din beställning.";
            else
                outcome = "Produkten kunde inte läggas till i din beställning. Försök igen.";
            while (rs.next()) {
                customer.setActiveOrder(rs.getInt("orders_id"));
            }
        } catch (SQLException e) {
            outcome = e.getMessage();
            if (outcome.startsWith("Data truncation: ")) {
                outcome = outcome.replace("Data truncation: ", "");
            }
        }
        return outcome;
    }

    public ArrayList<Product> getReceipt(int orderID) {
        ArrayList<Product> receipt = new ArrayList<>();

        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {
            PreparedStatement stm = con.prepareStatement(
                    "SELECT p.id, brand.name as brand, m.name as model, p.size, p.colour, p.price " +
                            "FROM shoeshop.orders_contains_product ocp " +
                            "INNER JOIN shoeshop.product p ON ocp.product_id = p.id " +
                            "INNER JOIN shoeshop.model m ON p.model_id = m.id " +
                            "INNER JOIN shoeshop.brand ON m.brand_id = brand.id " +
                            "WHERE ocp.orders_id = ?;");
            stm.setInt(1, orderID);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int size = rs.getInt("size");
                String colour = rs.getString("colour");
                int price = rs.getInt("price");
                Product product = new Product(id, brand, model, null, size, colour, price);
                receipt.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Fel vid läsning från databas. Error kod: " + e.getErrorCode());
        }
        return receipt;
    }

    public boolean closeOrder(int orderID) {
        try (Connection con = DriverManager.getConnection
                (p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));) {

            PreparedStatement stm = con.prepareStatement("UPDATE shoeshop.orders SET isActive = FALSE WHERE id = ?");
            stm.setInt(1, orderID);

            int updatedRows = stm.executeUpdate();
            if (updatedRows > 0)
                return true;
        } catch (SQLException e) {
            System.err.println("Fel vid läsning från databas. Error kod: " + e.getErrorCode());
        }
    return false;
    }
}