package Accounts;

import Music.Search;
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
            System.out.println("\n+---------------- User Panel ----------------+");
            System.out.println("Welcome, " + username + "!");
            System.out.println("1. View Songs");
            System.out.println("2. Follow an Artist");
            System.out.println("3. Your Following List");
            System.out.println("4. Exit");
            System.out.println("+--------------------------------------------+");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> Search.searchMenu();
                case 2 -> followArtist();
                case 3 -> showFollowingList();
                case 4 -> {
                    System.out.println("Exiting user panel...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void followArtist() {
        while (true) {
            System.out.print("Enter the artist's name to follow: ");
            String artistName = scanner.nextLine();

            if (!artistName.isEmpty()) {
                saveFollowing(artistName);
                System.out.println("You are now following " + artistName + "!");
            }

            System.out.println("Follow another artist? (y/n):");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) return;
        }
    }

    private void saveFollowing(String artistName) {
        String filename = "data/following_" + username + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(artistName + "\n");
        } catch (IOException e) {
            System.out.println("Error saving following: " + e.getMessage());
        }
    }

    private void showFollowingList() {
        String filename = "data/following_" + username + ".txt";
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("You are not following any artists.");
            return;
        }

        System.out.println("Your Following List:");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("- " + line);
            }
        } catch (IOException e) {
            System.out.println("Error reading following list.");
        }
    }
}
