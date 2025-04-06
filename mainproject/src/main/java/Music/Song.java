package music;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Song {
    public static void createNewSong(String artistName) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter song name: ");
        String songName = scanner.nextLine();

        System.out.println("Enter song lyrics (Type 'END' to finish):");
        StringBuilder lyrics = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equalsIgnoreCase("END")) {
            lyrics.append(line).append("\n");
        }

        saveSongToFile(artistName, songName, lyrics.toString());
    }

    public static void createNewAlbum(String artistName) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many songs do you want to add? ");
        int numberOfSongs = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfSongs; i++) {
            System.out.println("\nAdding song " + (i + 1) + ":");
            createNewSong(artistName);
        }

        System.out.println("Album created successfully!");
    }

    private static void saveSongToFile(String artistName, String songName, String lyrics) {
        String fileName = "songs/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Song Name: " + songName + "\n");
            writer.write("Artist: " + artistName + "\n");
            writer.write("Lyrics:\n" + lyrics);
            System.out.println("Song saved successfully: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving song: " + e.getMessage());
        }
    }

    public static void manageComments(String artistName, String songName) {
        Comment.showComments(artistName, songName);

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Add a Comment");
        System.out.println("2. Back to song menu");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            Comment.addComment(artistName, songName);
        }
    }

    public static void requestEditLyrics(String artistName, String songName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your new lyrics (Type 'END' to finish):");

        StringBuilder newLyrics = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equalsIgnoreCase("END")) {
            newLyrics.append(line).append("\n");
        }

        String filePath = "pending_lyrics/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + "_edit_request.txt";
        saveEditRequestToFile(filePath, songName, artistName, newLyrics.toString());

        System.out.println("Your edit request has been submitted successfully.");
    }

    private static void saveEditRequestToFile(String filePath, String songName, String artistName, String newLyrics) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Song: " + songName + "\n");
            writer.write("Artist: " + artistName + "\n");
            writer.write("New Lyrics:\n" + newLyrics);
            writer.write("------------------------\n");
        } catch (IOException e) {
            System.out.println("Error saving edit request: " + e.getMessage());
        }
    }
}
