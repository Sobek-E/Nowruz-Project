package Music;

import java.io.*;

public class View {

    public static void addView(String artistName, String songName) {
        String fileName = "data/views/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + "_views.txt";
        int views = getViewCount(artistName, songName) + 1;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(views));
        } catch (IOException e) {
            System.out.println("Error updating views: " + e.getMessage());
        }
    }

    public static int getViewCount(String artistName, String songName) {
        String fileName = "data/views/" + artistName.replaceAll(" ", "_") + "_" + songName.replaceAll(" ", "_") + "_views.txt";
        File file = new File(fileName);

        if (!file.exists()) return 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }
}