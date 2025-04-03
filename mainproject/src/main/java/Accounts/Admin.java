package accounts;
import java.io.*;
import java.util.*;

public class Admin {
    Scanner scanner = new Scanner(System.in);

    public void adminPanel() {
        while (true) {
            System.out.println("\nWelcome to Admin Panel");
            System.out.println("Choose an option:");
            System.out.println("1. See artist requests");
            System.out.println("2. See user requests");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleArtistRequests();
                    break;
                case 2:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void handleArtistRequests() {
        List<String> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("pending_artists.txt"))) {
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("approved_artists.txt", true))) {
            writer.write(artistData + "\n");
            System.out.println("Artist has been approved and added to the approved list.");
        } catch (IOException e) {
            System.out.println("Error approving artist: " + e.getMessage());
        }
    }

    private void removeArtistRequest(String artistData) {
        List<String> allRequests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("pending_artists.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(artistData)) {
                    allRequests.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading pending requests.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pending_artists.txt"))) {
            for (String request : allRequests) {
                writer.write(request + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating pending requests.");
        }
    }
}
