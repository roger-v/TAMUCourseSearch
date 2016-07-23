// Main.java
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main extends Application {

    public static Connection conn;

    public static CourseSearch currentSearch = new CourseSearch();
    public static RoomAvailabilitySearch currentRASearch = new RoomAvailabilitySearch();
    public static CSTableView csTableView = new CSTableView();  //  Main.csTableView.setCourses(courses);
    //Main.csTableView.addCourse(cour);
    public static RATableView raTableView = new RATableView();  // Main.raTableView.setRooms(rooms);

    public Scene scene;
    public static SearchView searchView = new SearchView();
    public static RoomAvailabilityView roomAvailabilityView = new RoomAvailabilityView();
    public static Stage stage;

    public static boolean canDisplayResults = true;

    double widthRatio = 16;
    double heightRatio = 9;

    public static void main(String[] args) {
        String DBLocation = "database-new.cse.tamu.edu"; //The host
        String DBname = "levin23"; //Generally your CS username or username-text like explained above
        String DBUser = "levin23"; //CS username
        String DBPass = "ebuks138902"; //password setup via CSNet for the MySQL database
        conn = null;
        try {
            String connectionString = "jdbc:mysql://"+DBLocation+"/"+DBname;
            Class.forName ("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(connectionString, DBUser, DBPass);
            System.out.println ("Database connection established");
        } catch (Exception e){
            System.out.println("Connection Issue: " + e.getMessage());
        }

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        if (conn != null) {
            stage = primaryStage;
            //LOADING IN THE FIRST VIEW
            //Load in the fxml file
            final FXMLLoader loadScene0 = new FXMLLoader(getClass().getResource("searchview.fxml"));
            loadScene0.setController(searchView);
            Parent root0 = loadScene0.load(); //Loads the object hierarchy, which is encased by a vbox.

            primaryStage.setTitle("Texas A&M University Course Search");

            float scale = 70;
            scene = new Scene(root0, widthRatio * scale, heightRatio * scale);
            scene.getStylesheets().add("application/styles.css");

            stage.setScene(scene);
            stage.show();


            root0.requestFocus();
        }
        else {
            MessageWindow connFailed = new MessageWindow("Connection Issue", "We failed to connect to the database.\nYou must be connected to the Internet and to the campus VPN.");
        }
    }


    //from: http://stackoverflow.com/questions/19484406/detecting-if-a-string-has-unique-characters-comparing-my-solution-to-cracking
    public static boolean isUniqueChars(String str) {
        if (str.length() > 256) {
            return false;
        }
        int checker = 0;
        for (int i = 0; i < str.length(); i++) {
            int val = str.charAt(i) - 'a';
            if ((checker & (1 << val)) > 0) return false;
            checker |= (1 << val);
        }
        return true;
    }


    public static void writeResultSet(String searchType, Statement statement, String query) throws SQLException{

        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        int lines = 0;
        try(FileWriter fw = new FileWriter("src/application/transactionLog.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            out.println(dateFormat.format(cal.getTime())); //2014/08/06 16:00:22
            out.println(searchType);
            out.println("Query: " + query);
            out.println("Results: " );
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) out.print(", ");
                    String columnValue = rs.getString(i);
                    out.print(columnValue);
                }
                out.println("");
                lines ++;
            }
            out.println("Total lines: " + lines + '\n');
        } catch (IOException e) {
            System.out.println("Main.writeResultSet() threw IOException - " + e.getMessage());
        }
    }


    //LEVI------------------------------------
    public static int timeMaker(String timing) {
        try {
            if (timing.equals("TBA")) return -1;
            int size = timing.length();
            int ntime;
            if (timing.charAt(1) == ':') {
                ntime = Integer.parseInt(timing.substring(0, 1));
                char key = timing.charAt(size - 2);
                ntime = ((key == 'P' || key == 'p') && ntime != 12) ? 240 + (ntime) * 60 : (ntime - 8) * 60;
                ntime += Integer.parseInt(timing.substring(2, 4));
            } else {
                ntime = Integer.parseInt(timing.substring(0, 2));
                ntime = (timing.charAt(size - 2) == 'P' && ntime != 12) ? 240 + (ntime) * 60 : (ntime - 8) * 60;
                ntime += Integer.parseInt(timing.substring(3, 5));
            }
            canDisplayResults = true;
            return ntime;
        } catch (Exception e){
            MessageWindow err = new MessageWindow("Time Format Error", "Please type in the time in the format HH:MM AM (or PM).");
            canDisplayResults = false;
        }
        return 0;
    }

    public static String timeMaker(int timing) {
        try {
            if (timing == -1) return "TBA";
            int minutes = timing % 60;
            if (timing < -1 && minutes != 0) minutes += 60;
            int hours = (timing - minutes) / 60;
            String emmy = "AM";
            if (hours > 4) {
                hours -= 4;
                emmy = "PM";
            } else hours += 8;
            if (hours == 12) emmy = "PM";
            if (hours == 0) hours = 12;
            String ntime;
            if (minutes <= 9)
                ntime = Integer.toString(hours) + ":" + "0" + Integer.toString(minutes);
            else
                ntime = Integer.toString(hours) + ":" + Integer.toString(minutes);
            ntime += " " + emmy;
            canDisplayResults = true;
            return ntime;
        } catch (Exception e){
            MessageWindow err = new MessageWindow("Time Format Error", "Please type in the time in the format HH:MM AM (or PM).");
            canDisplayResults = false;
        }
        return "";
    }
}