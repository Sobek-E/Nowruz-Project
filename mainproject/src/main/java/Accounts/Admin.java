package accounts;

import java.io.*;
import java.util.*;

public class Admin {
    Scanner scanner = new Scanner(System.in);

    public void adminPanel() {
        while (true) {
            System.out.println("\n--- Welcome to Admin Panel ---");
            System.out.println("Choose an option:");
            System.out.println("1. See artist requests");
            System.out.println("2. See lyric edit requests");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> handleArtistRequests();
                case 2 -> handleLyricEditRequests();
                case 3 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void handleArtistRequests() {
        List<String> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/pending_artists.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requests.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading artist requests.");
        }

        if (requests.isEmpty()) {
            System.out.println("No artist requests.");
            return;
        }

        for (String request : requests) {
            System.out.println("Approve artist: " + request + " ? (y/n)");
            String decision = scanner.nextLine();
            if (decision.equalsIgnoreCase("y")) {
                approveArtist(request);
                removeArtistRequest(request);
            }
        }
    }

    private void approveArtist(String artistData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/approved_artists.txt", true))) {
            writer.write(artistData + "\n");
            System.out.println("Artist has been approved and added to the approved list.");
        } catch (IOException e) {
            System.out.println("Error approving artist: " + e.getMessage());
        }
    }

    private void removeArtistRequest(String artistData) {
        List<String> allRequests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/pending_artists.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(artistData)) {
                    allRequests.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading pending requests.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/pending_artists.txt"))) {
            for (String request : allRequests) {
                writer.write(request + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating pending requests.");
        }
    }

    private void handleLyricEditRequests() {
        File editFolder = new File("data/edits");
        File[] editFiles = editFolder.listFiles((dir, name) -> name.endsWith("_edit.txt"));

        if (editFiles == null || editFiles.length == 0) {
            System.out.println("No lyric edit requests.");
            return;
        }

        for (File editFile : editFiles) {
            System.out.println("Reviewing: " + editFile.getName());

            try (BufferedReader reader = new BufferedReader(new FileReader(editFile))) {
                String line;
                StringBuilder lyrics = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    lyrics.append(line).append("\n");
                }
                System.out.println(lyrics);
                System.out.println("Approve this edit? (y/n):");
                String decision = scanner.nextLine();

                if (decision.equalsIgnoreCase("y")) {
                    applyEditToSong(editFile.getName(), lyrics.toString());
                    editFile.delete();
                    System.out.println("Edit approved and applied.");
                } else {
                    editFile.delete();
                    System.out.println("Edit rejected and deleted.");
                }

            } catch (IOException e) {
                System.out.println("Error reading edit file: " + editFile.getName());
            }
        }
    }

    private void applyEditToSong(String editFileName, String lyricsContent) {
        String songFileName = editFileName.replace("_edit.txt", "");
        File songFile = new File("data/songs/" + songFileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile))) {
            String[] lines = lyricsContent.split("\n", 4);
            for (String line : lines) {
                if (line.startsWith("Song Name:") || line.startsWith("Artist:")) {
                    writer.write(line + "\n");
                }
            }
            writer.write("Lyrics:\n");
            if (lyricsContent.contains("Lyrics:\n")) {
                writer.write(lyricsContent.split("Lyrics:\\n", 2)[1]);
            }
        } catch (IOException e) {
            System.out.println("Error updating song lyrics.");
        }
    }
}