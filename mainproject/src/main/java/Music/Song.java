package music;

import java.io.*;
import java.util.*;

public class Song {

    public static void createNewSong(String artistName) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the song name: ");
        String songName = scanner.nextLine();

        System.out.println("Enter the lyrics (end with a single '.' on a line):");
        StringBuilder lyrics = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals(".")) {
            lyrics.append(line).append("\n");
        }

        File songFile = new File("data/songs/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile))) {
            writer.write("Song Name: " + songName + "\n");
            writer.write("Artist: " + artistName + "\n");
            writer.write("Lyrics:\n" + lyrics);
            System.out.println("Song created successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the song.");
        }
    }

    public static void createNewAlbum(String artistName) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter album name: ");
        String albumName = scanner.nextLine();
        System.out.print("How many songs in the album? ");
        int numberOfSongs = scanner.nextInt();
        scanner.nextLine();

        File albumFile = new File("data/albums/" + artistName.replaceAll(" ", "_") + "_" + albumName.replaceAll(" ", "_") + ".txt");

        try (BufferedWriter albumWriter = new BufferedWriter(new FileWriter(albumFile))) {
            albumWriter.write("Album Name: " + albumName + "\n");
            albumWriter.write("Artist: " + artistName + "\n");
            albumWriter.write("Songs:\n");

            for (int i = 0; i < numberOfSongs; i++) {
                System.out.print("Enter the name of song " + (i + 1) + ": ");
                String songName = scanner.nextLine();

                System.out.println("Enter lyrics for " + songName + " (end with a single '.' on a line):");
                StringBuilder lyrics = new StringBuilder();
                String line;
                while (!(line = scanner.nextLine()).equals(".")) {
                    lyrics.append(line).append("\n");
                }

                File songFile = new File("data/songs/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + ".txt");

                try (BufferedWriter songWriter = new BufferedWriter(new FileWriter(songFile))) {
                    songWriter.write("Song Name: " + songName + "\n");
                    songWriter.write("Artist: " + artistName + "\n");
                    songWriter.write("Lyrics:\n" + lyrics);
                } catch (IOException e) {
                    System.out.println("Error saving song: " + songName);
                }

                albumWriter.write(songName + "\n");
            }

            System.out.println("Album created successfully.");
        } catch (IOException e) {
            System.out.println("Error saving album.");
        }
    }

    public static void requestEditLyrics(String artistName, String songName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the new lyrics (end with a single '.' on a line):");
        StringBuilder newLyrics = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals(".")) {
            newLyrics.append(line).append("\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/pending_lyrics_requests.txt", true))) {
            writer.write(artistName + "," + songName + "," + newLyrics.toString().replaceAll("\n", "<br>") + "\n");
            System.out.println("Your lyrics edit request has been submitted for review.");
        } catch (IOException e) {
            System.out.println("Error submitting lyrics edit request.");
        }
    }

    public static void handleArtistEditRequests(String artistName) {
        List<String> requests = new ArrayList<>();
        File requestFile = new File("data/pending_lyrics_requests.txt");

        if (!requestFile.exists()) {
            System.out.println("No lyric edit requests found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(requestFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(artistName + ",")) {
                    requests.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading edit requests.");
            return;
        }

        if (requests.isEmpty()) {
            System.out.println("You have no lyric edit requests.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        for (String request : requests) {
            System.out.println("Edit request: " + request);
            System.out.print("Do you approve this edit? (y/n): ");
            String decision = scanner.nextLine();

            if (decision.equalsIgnoreCase("y")) {
                approveLyricEdit(request);
            }
            removeLyricEditRequest(request);
        }
    }

    private static void approveLyricEdit(String request) {
        String[] requestData = request.split(",");
        if (requestData.length < 3) return;

        String artistName = requestData[0].trim();
        String songName = requestData[1].trim();
        String newLyrics = requestData[2].trim().replaceAll("<br>", "\n");

        File songFile = new File("data/songs/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile))) {
            writer.write("Song Name: " + songName + "\n");
            writer.write("Artist: " + artistName + "\n");
            writer.write("Lyrics:\n" + newLyrics);
            System.out.println("Lyrics updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating lyrics.");
        }
    }

    private static void removeLyricEditRequest(String request) {
        File file = new File("data/pending_lyrics_requests.txt");
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(request)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading pending requests.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating pending requests.");
        }
    }
}
