package music;
import java.io.*;
import java.util.*;

public class Search {
    private static final String SONGS_DIRECTORY = "songs/";
    private static Scanner scanner = new Scanner(System.in);

    public static void searchMenu() {
        while (true) {
            System.out.println("\nSearch Menu:");
            System.out.println("1. Show Random Songs");
            System.out.println("2. Search for a Song");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showRandomSongs();
                    break;
                case 2:
                    searchSong();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showRandomSongs() {
        File folder = new File(SONGS_DIRECTORY);
        File[] songFiles = folder.listFiles();

        if (songFiles == null || songFiles.length == 0) {
            System.out.println("No songs available.");
            return;
        }

        List<File> songList = Arrays.asList(songFiles);
        Collections.shuffle(songList);

        for (int i = 0; i < Math.min(5, songList.size()); i++) {
            displaySongInfo(songList.get(i));
        }
    }

    private static void searchSong() {
        System.out.print("Enter song or artist name: ");
        String query = scanner.nextLine().toLowerCase();

        File folder = new File(SONGS_DIRECTORY);
        File[] songFiles = folder.listFiles();

        if (songFiles == null || songFiles.length == 0) {
            System.out.println("No songs found.");
            return;
        }

        for (File songFile : songFiles) {
            if (songFile.getName().toLowerCase().contains(query)) {
                displaySongInfo(songFile);
            }
        }
    }

    private static void displaySongInfo(File songFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(songFile))) {
            String artist = "", album = "", songName = "", lyrics = "";
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Artist: ")) {
                    artist = line.substring(8);
                } else if (line.startsWith("Album: ")) {
                    album = line.substring(7);
                } else if (line.startsWith("Song Name: ")) {
                    songName = line.substring(11);
                } else if (line.startsWith("Lyrics:")) {
                    lyrics = line.substring(7).trim();
                    while ((line = reader.readLine()) != null) {
                        lyrics += "\n" + line;
                    }
                }
            }

            System.out.println("\nArtist: " + artist);
            if (!album.isEmpty()) {
                System.out.println("Album: " + album);
            }
            System.out.println("Song: " + songName);
            System.out.println("Lyrics:\n" + lyrics);

            System.out.println("\nOptions:");
            System.out.println("1. Edit Lyrics");
            System.out.println("2. Add Comment");
            System.out.println("3. Return to Search Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    editLyrics(songFile);
                    break;
                case 2:
                    addComment(songFile);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (IOException e) {
            System.out.println("Error reading song file: " + e.getMessage());
        }
    }

    private static void editLyrics(File songFile) {
        System.out.println("Enter new lyrics (Type 'END' to finish):");
        StringBuilder newLyrics = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equalsIgnoreCase("END")) {
            newLyrics.append(line).append("\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile, true))) {
            writer.write("Lyrics:\n" + newLyrics.toString());
            System.out.println("Lyrics updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating lyrics: " + e.getMessage());
        }
    }

    private static void addComment(File songFile) {
        System.out.print("Enter your comment: ");
        String comment = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile, true))) {
            writer.write("\nComment: " + comment + "\n");
            System.out.println("Comment added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding comment: " + e.getMessage());
        }
    }
}
