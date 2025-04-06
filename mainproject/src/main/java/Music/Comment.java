package music;

import java.io.*;
import java.util.Scanner;

public class Comment {
    public static void showComments(String artistName, String songName) {
        String filePath = "comments/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + "_comments.txt";
        File file = new File(filePath);

        System.out.println("\n--- Comments for " + songName + " by " + artistName + " ---");
        if (!file.exists()) {
            System.out.println("No comments yet.");
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("- " + line);
                }
            } catch (IOException e) {
                System.out.println("Error reading comments: " + e.getMessage());
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n1. Add a Comment");
        System.out.println("2. Back to Song Info");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            addComment(artistName, songName);
        }
    }

    public static void addComment(String artistName, String songName) {
        String filePath = "comments/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + "_comments.txt";
        addComment(filePath);
    }

    private static void addComment(String filePath) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your comment: ");
        String comment = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(comment + "\n");
            System.out.println("Comment added successfully!");
        } catch (IOException e) {
            System.out.println("Error saving comment: " + e.getMessage());
        }
    }
}
