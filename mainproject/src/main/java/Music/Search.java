package Music;

import java.io.*;
import java.util.*;

public class Search {
    private static final String SONGS_DIRECTORY = "data/songs/";
    private static final String ALBUMS_DIRECTORY = "data/albums/";
    private static final String LIKES_DIRECTORY = "data/likes/";
    private static final String VIEWS_DIRECTORY = "data/views/";
    private static Scanner scanner = new Scanner(System.in);

    public static void searchMenu() {
        while (true) {
            System.out.println("\n=====================");
            System.out.println("   Search Menu");
            System.out.println("=====================");
            System.out.println("1. Show Random Songs");
            System.out.println("2. Search for a Song");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showRandomSongs();
                case 2 -> searchSong();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showRandomSongs() {
        List<File> songFiles = getAllSongFiles();
        if (songFiles.isEmpty()) {
            System.out.println("\nNo songs available.");
            return;
        }

        Collections.shuffle(songFiles);
        System.out.println("\nHere are some random songs:");
        for (int i = 0; i < Math.min(5, songFiles.size()); i++) {
            displaySongInfo(songFiles.get(i));
        }
    }

    private static void searchSong() {
        System.out.print("\nEnter artist name, song name, or keyword: ");
        String query = scanner.nextLine().toLowerCase();

        List<File> songFiles = getAllSongFiles();
        boolean found = false;

        for (File songFile : songFiles) {
            if (songFile.getName().toLowerCase().contains(query)) {
                displaySongInfo(songFile);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo songs matched your search.");
        }
    }

    private static List<File> getAllSongFiles() {
        List<File> allSongs = new ArrayList<>();
        File songFolder = new File(SONGS_DIRECTORY);
        File albumFolder = new File(ALBUMS_DIRECTORY);

        if (songFolder.exists() && songFolder.isDirectory()) {
            allSongs.addAll(Arrays.asList(Objects.requireNonNull(songFolder.listFiles())));
        }

        if (albumFolder.exists() && albumFolder.isDirectory()) {
            allSongs.addAll(Arrays.asList(Objects.requireNonNull(albumFolder.listFiles())));
        }

        return allSongs;
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

            int likes = readCount(LIKES_DIRECTORY + songFile.getName() + "_like.txt");
            int dislikes = readCount(LIKES_DIRECTORY + songFile.getName() + "_dislike.txt");
            int views = incrementViewCount(songFile.getName());

            System.out.println("\n=====================");
            System.out.println("Song Details");
            System.out.println("=====================");
            System.out.println("Artist: " + artist);
            if (!album.isEmpty()) System.out.println("Album: " + album);
            System.out.println("Song: " + songName);
            System.out.println("\nLyrics:\n" + lyrics);
            System.out.println("\nLikes: " + likes + "   Dislikes: " + dislikes + "   Views: " + views);
            System.out.println("=====================");
            System.out.println("Options:");
            System.out.println("1. Like");
            System.out.println("2. Dislike");
            System.out.println("3. Add Comment");
            System.out.println("4. Q&A (Ask or Answer)");
            System.out.println("5. Suggest Lyrics Edit");
            System.out.println("6. Return to Search Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> saveVote(songFile.getName(), true);
                case 2 -> saveVote(songFile.getName(), false);
                case 3 -> addComment(songFile);
                case 4 -> QuestionAnswer.handleQnA(songFile);
                case 5 -> suggestLyricsEdit(songFile);
                case 6 -> {}
                default -> System.out.println("Invalid choice.");
            }
        } catch (IOException e) {
            System.out.println("Error reading song file: " + e.getMessage());
        }
    }

    private static void saveVote(String fileName, boolean like) {
        String path = LIKES_DIRECTORY;
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();

        String filePath = path + fileName + (like ? "_like.txt" : "_dislike.txt");
        int count = readCount(filePath);
        count++;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(count));
            System.out.println(like ? "Liked!" : "Disliked!");
        } catch (IOException e) {
            System.out.println("Error saving like/dislike.");
        }
    }

    private static int readCount(String path) {
        File file = new File(path);
        if (!file.exists()) return 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    private static int incrementViewCount(String songFileName) {
        File viewsFolder = new File(VIEWS_DIRECTORY);
        if (!viewsFolder.exists()) viewsFolder.mkdirs();

        File viewFile = new File(viewsFolder, songFileName + ".txt");
        int views = 0;

        if (viewFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(viewFile))) {
                String line = reader.readLine();
                if (line != null) views = Integer.parseInt(line.trim());
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error reading views.");
            }
        }

        views++;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(viewFile))) {
            writer.write(String.valueOf(views));
        } catch (IOException e) {
            System.out.println("Error writing views.");
        }

        return views;
    }

    private static void addComment(File songFile) {
        System.out.print("Enter your comment: ");
        String comment = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(songFile, true))) {
            writer.write("\nComment: " + comment);
            System.out.println("Comment added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding comment: " + e.getMessage());
        }
    }

    private static void suggestLyricsEdit(File songFile) {
        System.out.println("Enter your suggested lyrics (end with a blank line):");

        StringBuilder newLyrics = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.isBlank()) break;
            newLyrics.append(line).append("\n");
        }

        File editsFolder = new File("data/edits");
        if (!editsFolder.exists()) editsFolder.mkdirs();

        String editFileName = "data/edits/" + songFile.getName() + "_edit.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(editFileName, true))) {
            writer.write("Suggested Lyrics:\n");
            writer.write(newLyrics.toString());
            writer.write("---\n");
            System.out.println("Your suggestion has been submitted for review.");
        } catch (IOException e) {
            System.out.println("Error saving lyrics edit suggestion.");
        }
    }
}
