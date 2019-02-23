/**
 * @auther Petter Vang Brakalsvalet
 * @version 1.0 (23/02/2019)
 */

import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;


public class fruitDatabase {

    /**
     * This will make a 2D array of the fruit.
     *
     * @param data IS the JSON data loaded.
     * @return a 2D array of the with the data organised
     */
    String[][] buildDatabase(ArrayList<String> data) {
        String[][] database = new String[10][3];
        int row = 0;
        int column;
        for (String s : data) {
            String[] buffer = s.split(":");
            switch (buffer[0]) {
                case "type":
                    column = 0;
                    database[row][column] = buffer[1];
                    break;
                case "price":
                    column = 1;
                    database[row][column] = buffer[1];
                    break;
                case "weight":
                    column = 2;
                    database[row][column] = buffer[1];
                    row = row + 1;
            }
        }
        return database;
    }

    /**
     * This will print out the whole data base in a human readable format.
     *
     * @param database a 2D string array.
     */
    public void printDatabase(String[][] database) {
        for (String[] x : database) {
            for (String y : x) {
                if (y == null) {
                    break;
                } else {
                    System.out.print(y + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * This will clean the JSON file.
     *
     * @return an Array list with the data cleaned.
     */
    ArrayList<String> cleanJson() {
        String jsonString = null;
        String[] jsonBuffer;
        try {
            jsonString = getJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonString != null) {
            jsonString = jsonString.replace("fruit", "");
            jsonString = jsonString.replace(" ", "");
            jsonString = jsonString.replace("[", "");
            jsonString = jsonString.replace("{", "");
            jsonString = jsonString.replace("\"", "");
            jsonString = jsonString.replace("}", "");
            jsonString = jsonString.replace("]", "");
            jsonString = jsonString.replaceFirst(":", "");
            jsonBuffer = jsonString.split(",");
            return new ArrayList<>(Arrays.asList(jsonBuffer));
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * This will get the JSON file from the web-side.
     *
     * @return this will return a string with the whole JSON file
     * @throws IOException gets thrown if it can'c get contact with the URL.
     */
    private String getJson() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/data.json");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }
}