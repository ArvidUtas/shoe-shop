public class Product {
    private int id;
    private String brand;
    private String model;
    private String description;
    private int size;
    private String colour;

    public Product(int id, String brand, String model, String description, int size, String colour) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.size = size;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-18s %-10d %-10s %-10s%n",brand, model, size, colour, description);
    }
}
