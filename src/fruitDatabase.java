

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class fruitDatabase{

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

    void printDatabase(String[][] database){
        for (String[] x : database) {
            for (String y : x) {
                if (y == null){
                    break;
                } else {
                    System.out.print(y + " ");
                }
            }
            System.out.println();
        }
    }

    ArrayList<String> cleanJson() {
        String jsonString = null;
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
        }
        jsonString = jsonString.replaceFirst(":", "");
        String[] jsonBuffer = jsonString.split(",");

        return new ArrayList<>(Arrays.asList(jsonBuffer));
    }

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