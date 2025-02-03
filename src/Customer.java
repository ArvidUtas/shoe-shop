public class Customer {
    private int id;
    private String name;
    private String address;
    private int postcode;
    private String email;
    private int activeOrder;

    public Customer(int id, String name, String address, int postcode, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.email = email;
    }

    public Customer(int id, String name, String address, int postcode, String email, int activeOrder) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.email = email;
        this.activeOrder = activeOrder;
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

    public int getActiveOrder() {
        return activeOrder;
    }

    public void setActiveOrder(int activeOrder) {
        this.activeOrder = activeOrder;
    }
}
