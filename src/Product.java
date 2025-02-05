public class Product {
    private int id;
    private String brand;
    private String model;
    private String description;
    private int size;
    private String colour;
    private int price;

    public Product(int id, String brand, String model, String description, int size, String colour, int price) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.size = size;
        this.colour = colour;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-18s %-10d %-10s %-10s%n",brand, model, size, colour, description);
    }
    public String receiptToString() {
        return String.format("%-5d %-15s %-18s %-10d %-10s %-10s%n",id, brand, model, size, colour, price);
    }
}
