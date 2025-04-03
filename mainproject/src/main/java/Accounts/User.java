package accounts;

import music.Search;
import java.io.*;
import java.util.Scanner;

public class User {
    private String username;
    private Scanner scanner = new Scanner(System.in);

    public User(String username) {
        this.username = username;
    }

    public void userPanel() {
        while (true) {
            System.out.println("\nWelcome to User Panel");
            System.out.println("Choose one of the options:");
            System.out.println("1. View Songs");
            System.out.println("2. Follow an Artist");
            System.out.println("3. Your Following List");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Search.searchMenu();
                    break;
                case 2:
                    followArtist();
                    break;
                case 3:
                    showFollowingList();
                    break;
                case 4:
                    System.out.println("Exiting User Panel...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void followArtist() {
        while (true) {
            System.out.print("Enter the artist's name you want to follow: ");
            String artistName = scanner.nextLine();

            if (!artistName.isEmpty()) {
                saveFollowing(artistName);
                System.out.println("You are now following " + artistName + "!");
            }

            System.out.println("Do you want to follow another artist? (y/n)");
            String choice = scanner.nextLine();

            if (!choice.equalsIgnoreCase("y")) {
                return;
            }
        }
    }

    private void saveFollowing(String artistName) {
        String filename = "following_" + username + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(artistName + "\n");
        } catch (IOException e) {
            System.out.println("Error saving following list: " + e.getMessage());
        }
    }

    private void showFollowingList() {
        String filename = "following_" + username + ".txt";
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("You are not following any artists yet.");
            return;
        }

        System.out.println("Your Following List:");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("- " + line);
            }
        } catch (IOException e) {
            System.out.println("Error reading following list: " + e.getMessage());
        }
    }
}
