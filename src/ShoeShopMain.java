import java.util.ArrayList;
import java.util.Scanner;

public class ShoeShopMain {
    private Repository rep = new Repository();
    private Scanner sc = new Scanner(System.in);
    private Customer customer;

    public ShoeShopMain() {
        customer = login();
        System.out.println("Välkommen " + customer.getName());
        System.out.println("Aktiv beställning:  " + customer.getActiveOrder()); // ta bort denna sen
        System.out.println();
        System.out.println("Här är alla varor i lager:");
        System.out.println();
        System.out.println("ID:\tMärke:\t\t\tModell:\t\t\t\tStorlek:\tFärg:\t\tBeskrivning:"); //TODO: fixa formatering
        ArrayList<Product> prodList = rep.getProducts();
        for (Product product : prodList) {
            System.out.println(prodList.indexOf(product) + "\t" + product.toString());
        }
        System.out.println();

        System.out.print("Välj en vara att lägga i varukorgen, skriv dess ID (EXIT för att avbryta): ");
        while (sc.hasNext()) {
            if (sc.next().equalsIgnoreCase("exit"))
                break;
            if (sc.hasNextInt()) {
                int userChoice = sc.nextInt();
                if (userChoice < 0 || userChoice > prodList.size())
                    System.out.println("Felaktig inmatning. Skriv en siffra mellan 0 och " + (prodList.size() - 1) +
                            ". Försök igen.");
                else {
                    System.out.println(rep.addToCart(customer.getId(),
                            prodList.get(userChoice).getId(), customer.getActiveOrder()));
                }
            } else {
                System.out.println("Felaktig inmatning. Skriv en siffra mellan 0 och " + (prodList.size() - 1) +
                        ". Försök igen.");
                sc.nextLine();
            }
        }
    }

    private Customer login() {
        String email = "";
        String password = "";
        System.out.print("Välkommen! ");
                while (customer == null) {
            System.out.println("Skriv in din email-adress: ");
            email = sc.nextLine();
            System.out.println("Skriv in ditt lösenord: ");
            password = sc.nextLine();
            customer = rep.getCustomer(email, password);
            if (customer == null)
                System.out.println("Felaktiga uppgifter. Försök igen.");
        }
        return customer;
    }

    public static void main(String[] args) {
        new ShoeShopMain();
    }
}