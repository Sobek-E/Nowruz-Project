import java.util.Scanner;
import accounts.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        new File("data").mkdir();
        new File("data/songs").mkdir();
        new File("data/comments").mkdir();
        new File("data/views").mkdir();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n+-------------------------+");
            System.out.println("|     Welcome to Musicify |");
            System.out.println("+-------------------------+");
            System.out.println("| Choose your role:       |");
            System.out.println("| 1. Admin                |");
            System.out.println("| 2. Artist               |");
            System.out.println("| 3. User                 |");
            System.out.println("| 4. Exit                 |");
            System.out.println("+-------------------------+");

            int choice = scanner.nextInt();
            scanner.nextLine();

            String role = "";

            switch (choice) {
                case 1:
                    role = "Admin";
                    break;
                case 2:
                    role = "Artist";
                    break;
                case 3:
                    role = "User";
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
                    continue;
            }

            Accounts account = new Accounts(role);
            account.openPanel(role);
        }
    }
}