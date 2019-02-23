/**
 * @auther Petter Vang Brakalsvalet
 * @version 1.0 (23/02/2019)
 */

import javafx.application.Application;
import java.net.URLConnection;
import java.util.ArrayList;
import java.io.IOException;
import javafx.stage.Stage;
import java.util.Date;
import javax.swing.*;
import java.net.URL;
import java.awt.*;

public class Main extends Application {
    private final long startRequest = System.currentTimeMillis();
    private fruitDatabase fruit = new fruitDatabase();
    private String statDisplay = "display";
    private String[][] fruitData;

    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        ArrayList<String> jsonData = fruit.cleanJson();
        String statTime = "load";
        sendStats(startRequest, statTime);
        fruitData = fruit.buildDatabase(jsonData);
        makeWindow();
    }

    /**
     * makeWindow function is for making a widow and and displaying the the fruit.
     */
    private void makeWindow() {
        sendStats(0, statDisplay);
        JFrame frame = new JFrame("Fruit list");
        frame.setSize(250, 500);
        frame.setLocation(300,200);
        frame.setLayout(new GridLayout(fruitData.length,0));

        for (String[] thisFruit : fruitData) {
            if (thisFruit[0] != null) {
                String type = thisFruit[0].substring(0,1).toUpperCase() +
                        thisFruit[0].substring(1); // This wil make the capitalize the word.
                JButton typeFruit = new JButton(type);
                int price = Integer.parseInt(thisFruit[1]);
                int weight = Integer.parseInt(thisFruit[2]);
                frame.add(typeFruit);
                /*
                 * This will make buttons for all of the fruits.
                 * When you push one of the buttons you will be taken to the next page,
                 * and show info about the fruit you want.
                 */
                typeFruit.addActionListener(a -> {
                    String fruitInfo = type + " Cost " +
                            price / 100 + "," + price % 100 +
                            "£ and weight " + weight / 1000 + "," +
                            weight % 1000 + "kg";
                    JFrame infoFrame = new JFrame("Fruit info");
                    infoFrame.setSize(250, 500);
                    infoFrame.setLocation(300,200);
                    infoFrame.setLayout(new GridLayout(fruitData.length,0));
                    JTextArea info = new JTextArea();
                    info.append(fruitInfo);
                    infoFrame.add(info, Component.CENTER_ALIGNMENT);
                    JButton backButton = new JButton("Back");
                    infoFrame.add(backButton, Component.BOTTOM_ALIGNMENT);
                    /*
                      This will make a back button so that you can go
                      to the main list and chose a new item.
                     */
                    backButton.addActionListener(b -> {
                        frame.setVisible(true);
                        sendStats(startRequest, statDisplay);
                        infoFrame.setVisible(false);
                    });
                    infoFrame.setVisible(true);
                    sendStats(startRequest, statDisplay);
                    frame.setVisible(false);
                    infoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                });
            }
        }

        frame.setVisible(true);
        sendStats(startRequest, statDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * This function is for sending statistics to BBC about when the app
     * when it loads a file form a server and the timestamp from when it
     * page is shown.
     *
     * @param startTime The time( in ms) when the http request was sent.
     * @param type what type of statistics that will be sent.
     */
    private void sendStats(long startTime, String type) {
        long requestTime = System.currentTimeMillis() - startTime;
        long epochTime = new Date().getTime() / 1000L;
        long time = 0;
        switch (type) {
            case "display":
                time = epochTime;
                break;
            case "load":
                time = requestTime;
                break;
            default:
                System.err.println("Stats Error Type of stats is not defined.");
                break;
        }
        try {
            String url = "https://raw.githubusercontent.com/fmtvp/" +
                    "recruit-test-data/master/stats";

            StringBuilder query = new StringBuilder();
            query.append("event=");
            query.append(type);
            query.append("&data=");
            query.append(time);
            //String query = String.format("event=%s&data=%s",
            //        URLEncoder.encode(type),
            //        URLEncoder.encode(String.valueOf(time)));
            URLConnection connection = new URL(url + "?" + query).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
