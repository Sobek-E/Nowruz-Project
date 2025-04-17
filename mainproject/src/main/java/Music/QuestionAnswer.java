package Music;

import java.io.*;
import java.util.*;

public class QuestionAnswer {
    private static final Scanner scanner = new Scanner(System.in);

    public static void handleQnA(File songFile) {
        System.out.println("\n--- Questions and Answers ---");
        List<String> qnaList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(songFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Q:") || line.startsWith("A:")) {
                    qnaList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Q&A: " + e.getMessage());
        }

        if (qnaList.isEmpty()) {
            System.out.println("No questions have been asked yet.");
        } else {
            for (String entry : qnaList) {
                System.out.println(entry);
            }
        }

        System.out.println("\n---------------------------");
        System.out.println("1. Ask a Question");
        System.out.println("2. Answer a Question");
        System.out.println("3. Back");
        System.out.println("---------------------------");
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> askQuestion(songFile);
            case 2 -> answerQuestion(songFile);
            case 3 -> {}
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void askQuestion(File songFile) {
        System.out.print("Enter your question: ");
        String question = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile, true))) {
            writer.write("Q: " + question + "\n");
            System.out.println("✅ Question submitted successfully!");
        } catch (IOException e) {
            System.out.println("Error writing question: " + e.getMessage());
        }
    }

    private static void answerQuestion(File songFile) {
        System.out.print("Enter your answer: ");
        String answer = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile, true))) {
            writer.write("A: " + answer + "\n");
            System.out.println("✅ Answer submitted successfully!");
        } catch (IOException e) {
            System.out.println("Error writing answer: " + e.getMessage());
        }
    }
}
