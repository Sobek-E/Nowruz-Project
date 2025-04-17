package Accounts;

import java.util.Scanner;
import Music.Song;

public class Artist {
    private String artistName;
    Scanner scanner = new Scanner(System.in);

    public Artist(String artistName) {
        this.artistName = artistName;
    }

    public void artistPanel() {
        while (true) {
            System.out.println("\n+---------------- Artist Panel ----------------+");
            System.out.println("Welcome, " + artistName + "!");
            System.out.println("1. Create New Album");
            System.out.println("2. Create New Song");
            System.out.println("3. See/Edit Lyrics Edit Requests");
            System.out.println("4. Exit");
            System.out.println("+---------------------------------------------+");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> Song.createNewAlbum(artistName);
                case 2 -> Song.createNewSong(artistName);
                case 3 -> Song.handleArtistEditRequests(artistName);
                case 4 -> {
                    System.out.println("Exiting artist panel...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
