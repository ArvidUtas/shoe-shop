import java.util.Scanner;

public class ShoeShopMain {
    private Repository rep = new Repository();
    private Scanner sc = new Scanner(System.in);
    private String email = "";
    private String password = "";
    private int customerID = 0;

    public ShoeShopMain() {
        customerID = login();
        System.out.println(customerID);
    }

    private int login() {
        while (customerID==0) {
            System.out.println("Välkommen! Skriv in din email-adress: ");
            email = sc.nextLine();
            System.out.println("Skriv in ditt lösenord: ");
            password = sc.nextLine();
            customerID = rep.getCustomerID(email, password);
            if (customerID == 0)
                System.out.println("Felaktiga uppgifter. Försök igen.");
        }
        return customerID;
    }

    public static void main(String[] args) {
        new ShoeShopMain();
    }
}