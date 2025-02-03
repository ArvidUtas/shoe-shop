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
        return brand + "\t\t\t" + model + "\t\t\t\t" + size + "\t\t" + colour + "\t" + description;
    }
}
