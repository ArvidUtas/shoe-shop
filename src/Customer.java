public class Customer {
    private int id;
    private String name;
    private String address;
    private int postcode;
    private String email;

    public Customer(int id, String name, String address, int postcode, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPostcode() {
        return postcode;
    }

    public String getEmail() {
        return email;
    }
}
