import java.util.ArrayList;
import java.util.Scanner;

public class ShoeShopMain {
    private Repository rep = new Repository();
    private Scanner sc = new Scanner(System.in);
    private Customer customer;

    public ShoeShopMain() {
        customer = login();
        addProductsToCart();
        if (customer.getActiveOrder() != 0) {
            payOrder();
        }
        System.out.println("\nHej då!");
    }

    private Customer login() {
        String email;
        String password;
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

    private void addProductsToCart() {
        String userChoice;
        System.out.println("\nVälkommen " + customer.getName());
        System.out.println("Aktiv beställning: #" + customer.getActiveOrder());
        System.out.println("\nHär är alla varor i lager:\n");
        System.out.format("%-4s %-15s %-18s %-10s %-10s %-10s%n","ID:","Märke:","Modell:","Storlek:","Färg:","Beskrivning:");
        ArrayList<Product> prodList = rep.getProducts();
        for (Product product : prodList) {
            System.out.print((prodList.indexOf(product) + 1) + " \t " + product.toString());
        }

        System.out.print("\nVälj en vara att lägga i varukorgen, skriv dess ID (EXIT för att avbryta): ");
        while (sc.hasNext()) {
            userChoice = sc.nextLine();
            if (userChoice.trim().equalsIgnoreCase("exit")) {
                break;
            } else if (userChoice.trim().matches("[1-" + prodList.size() + "]")) {
                System.out.println(rep.addToCart(customer,
                        prodList.get(Integer.parseInt(userChoice) - 1).getId()));
                System.out.print("Välj en vara att lägga i varukorgen, skriv dess ID (EXIT för att gå vidare): ");
            } else {
                System.out.print("Felaktig inmatning. Skriv en siffra mellan 1 och " + prodList.size() +
                        ". Försök igen: ");
            }
        }
    }

    private void payOrder() {
        String userChoice;
        System.out.print("Vill du avsluta din beställning nu? Ja/Nej: ");
        while (sc.hasNext()) {
            userChoice = sc.nextLine();
            if (userChoice.trim().equalsIgnoreCase("ja")) {
                ArrayList<Product> receipt = rep.getReceipt(customer.getActiveOrder());
                int sum = 0;
                System.out.println("\nKvitto:");
                for (Product product : receipt) {
                    System.out.print(product.receiptToString());
                    sum += product.getPrice();
                }
                System.out.println("Summa: " + sum + "kr");
                boolean orderClosed = rep.closeOrder(customer.getActiveOrder());
                if (orderClosed)
                    customer.setActiveOrder(0);
                else
                    System.out.println("Beställningen kunde inte avslutas. Försök igen senare.");
                break;
            } else if (userChoice.trim().equalsIgnoreCase("nej")) {
                break;
            } else {
                System.out.println("Jag förstår inte. Skriv Ja eller Nej.");
            }
        }
    }

    public static void main(String[] args) {
        new ShoeShopMain();
    }
}