package music;
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

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Song Name: " + songName + "\n");
            writer.write("Artist: " + artistName + "\n");
            writer.write("Lyrics:\n" + lyrics);
            System.out.println("Song saved successfully: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving song: " + e.getMessage());
        }
    }

}

