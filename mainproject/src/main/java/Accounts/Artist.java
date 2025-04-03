package accounts;
import java.util.Scanner;
import music.Song;

public class Artist {
    private String artistName;
    Scanner scanner = new Scanner(System.in);

    public Artist(String artistName) {
        this.artistName = artistName;
    }

    public void artistPanel() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome " + artistName + " to the Artist Panel");
            System.out.println("Choose one of the options:");
            System.out.println("1. Create New Album");
            System.out.println("2. Create New Song");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Song.createNewAlbum(artistName);
                    break;
                case 2:
                    Song.createNewSong(artistName);
                    break;
                case 3:
                    System.out.println("Exiting Artist Panel...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
