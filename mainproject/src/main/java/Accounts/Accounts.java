package accounts;

import java.io.*;
import java.util.Scanner;

public class Accounts {
    private String role;
    private String artistName = null;
    private String username;
    private Scanner scanner = new Scanner(System.in);

    public Accounts(String role) {
        this.role = role;
        System.out.println("Choose one of the options:");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");

        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            login();
        } else if (option == 2) {
            signUp();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    public void login() {
        while (true) {
            System.out.println("Enter your Username:");
            String inputUsername = scanner.nextLine();
            System.out.println("Enter your Password:");
            String inputPassword = scanner.nextLine();

            if (checkLogin(inputUsername, inputPassword)) {
                this.username = inputUsername;
                if (role.equals("Artist")) {
                    artistName = getArtistName(username);
                    if (artistName == null) {
                        System.out.println("Your account has not been approved yet. Please wait for admin approval.");
                        return;
                    }
                }
                System.out.println("Login successful! Welcome, " + (artistName != null ? artistName : username));
                openPanel(role);
                break;
            } else {
                System.out.println("Error: Invalid username or password.");
                System.out.println("1. Try again");
                System.out.println("2. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 2) return;
            }
        }
    }

    public void signUp() {
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Enter your age: ");
        String age = scanner.nextLine();
        System.out.println("Enter your Username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your Password: ");
        String password = scanner.nextLine();

        if (role.equals("Artist")) {
            saveToFile("pending_artists.txt", role, name, age, username, password);
            System.out.println("Your artist request has been submitted for admin approval.");
        } else {
            saveToFile("users.txt", role, name, age, username, password);
            System.out.println("Your account has been created successfully!");
        }
    }

    private void saveToFile(String filename, String role, String name, String age, String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write("Role: " + role + "\n");
            writer.write("Name: " + name + "\n");
            writer.write("Age: " + age + "\n");
            writer.write("Username: " + username + "\n");
            writer.write("Password: " + password + "\n");
            writer.write("----------------------\n");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private boolean checkLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            String storedUsername = "";
            String storedPassword = "";
            String storedRole = "";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Role: ")) {
                    storedRole = line.substring(6);
                } else if (line.startsWith("Username: ")) {
                    storedUsername = line.substring(10);
                } else if (line.startsWith("Password: ")) {
                    storedPassword = line.substring(10);

                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        if (role.equals("Artist") && !storedRole.equals("Artist")) {
                            return false;
                        }
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public String getArtistName(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("approved_artists.txt"))) {
            String line;
            String artistName = "";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: ") && line.substring(10).equals(username)) {
                    while ((line = reader.readLine()) != null && !line.startsWith("----------------------")) {
                        if (line.startsWith("Name: ")) {
                            artistName = line.substring(6);
                            return artistName;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return null;
    }

    public void openPanel(String role) {
        if (role.equals("Admin")) {
            new Admin().adminPanel();
        } else if (role.equals("User")) {
            new User(username).userPanel();
        } else if (role.equals("Artist")) {
            if (artistName == null) {
                System.out.println("Your artist account is not approved yet.");
                return;
            }
            new Artist(artistName).artistPanel();
        }
    }

}
