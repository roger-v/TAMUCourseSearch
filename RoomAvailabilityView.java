//RoomAvailabilityView.java

package application;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RoomAvailabilityView implements Initializable {

    private String location = "";
    private String startTime = "";
    private String endTime = "";
    private String timeLength = "";
    private String days = "";
    private String seatCapacity = "";

    @FXML private VBox roomAvailabilityView;

    @FXML private TextField locationField, startTimeField, endTimeField, timeLengthField, daysField, seatCapacityField;

    @FXML private Button roomAvailabilitySearchButton;
    @FXML private Label roomAvailabilityTitle, returnToSearchView;

    public RoomAvailabilityView(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        executeFade();
        formatUserInput();
    }

    public void fadeIn(Node node, double millis){
        FadeTransition ft = new FadeTransition(Duration.millis(millis), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public void executeFade(){
        int i = 1;
        for (Node n : roomAvailabilityView.getChildren()){
            fadeIn(n, i*200);
            i++;
        }
    }

    public RoomAvailabilityView(String loc, String st, String et, String tl, String d, String sc){
        location = loc;
        startTime = st;
        endTime = et;
        timeLength = tl;
        days = d;
        seatCapacity = sc;
    }

    @FXML
    private void executeSearch(ActionEvent event) throws IOException{
        if (fieldsAreEmpty()){
            MessageWindow errMsg = new MessageWindow("Search Error", "The fields are empty.");
            return;
        }
        RoomAvailabilitySearch ras = Main.currentRASearch;
        ras.setLocation(getLocationText());
        ras.setStartTime(getStartTimeText());
        ras.setEndTime(getEndTimeText());
        ras.setTimeLength(getTimeLengthText());
        ras.setDays(getDaysText());
        ras.setSeatCapacity(getSeatCapacityText());

        openSearchResultsView(event);
        ras.generateResults();
    }

    public void openSearchResultsView(ActionEvent event) throws IOException {

        final FXMLLoader loadFXML = new FXMLLoader(getClass().getResource("ratableview.fxml"));
        loadFXML.setController(Main.raTableView);
        Parent root = loadFXML.load();
        Scene homePageScene = new Scene(root, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        homePageScene.getStylesheets().add("application/styles.css");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Texas A&M University - Room Search Results");
        root.requestFocus();
        currentStage.setScene(homePageScene);
        currentStage.show();

        //So their search could carry on into the results.
        Main.raTableView.locationField.setText(this.locationField.getText());
        Main.raTableView.startTimeField.setText(this.startTimeField.getText());
        Main.raTableView.endTimeField.setText(this.endTimeField.getText());
        Main.raTableView.timeLengthField.setText(this.timeLengthField.getText());
        Main.raTableView.daysField.setText(this.daysField.getText());
        Main.raTableView.seatCapacityField.setText(this.seatCapacityField.getText());
    }

    private void formatUserInput(){

        timeLengthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                if (!Character.isDigit(newValue.charAt(newValue.length() - 1))) {
                    timeLengthField.setText(oldValue);
                }
            }
        });
        locationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)){
                seatCapacityField.setText("");
            }
        });

        daysField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                char c = Character.toUpperCase(newValue.charAt(newValue.length() - 1));
                if (c != 'M' && c != 'T' && c != 'W' && c != 'R' && c != 'F') {
                    daysField.setText(oldValue);
                }
            }
            if (!Main.isUniqueChars(newValue)){
                daysField.setText(oldValue);
            }
        }));

        seatCapacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)){
                seatCapacityField.setText("");
            }
            else if (!Character.isDigit(newValue.charAt(newValue.length() - 1))){
                seatCapacityField.setText(oldValue);
            }

            //length restricted to 4 characters
            if (newValue.length() > 3){
                seatCapacityField.setText(oldValue);
            }
        });
    }

    //Checks the string within a text field.
    private boolean fieldIsEmpty(String text){
        for (int i = 0; i < text.length(); i++){
            boolean isEmpty = (text.charAt(i) == ' ' || text.charAt(i) == '\n' || text.charAt(i) == '\t');
            if (!isEmpty) return false;
        }
        return true;
    }

    private boolean fieldsAreEmpty(){
        String [] fieldTexts = {
                locationField.getText(),
                startTimeField.getText(),
                endTimeField.getText(),
                timeLengthField.getText(),
                daysField.getText(),
                seatCapacityField.getText(),
        };
        for (String text : fieldTexts){
            if (!fieldIsEmpty(text)) return false;
        }
        return true;
    }

    @FXML
    private void goToSearchView(MouseEvent event) throws IOException{
        System.out.println("Going back to Search View.");
        final FXMLLoader loadFXML = new FXMLLoader(getClass().getResource("searchview.fxml"));
        //NOTE - the line right below this is very important
        loadFXML.setController(Main.searchView);
        Parent root = loadFXML.load();
        Scene homePageScene = new Scene(root, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        homePageScene.getStylesheets().add("application/styles.css");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Texas A&M University Course Search");
        root.requestFocus();
        currentStage.setScene(homePageScene);
        currentStage.show();
    }



    @FXML
    private void showToolTip(MouseEvent event) throws IOException{
        Node node = (Node) event.getSource();
        if (node.getId() == locationField.getId()){
            locationField.setTooltip(new Tooltip("Type in the name of a building or a building and a room number to see when it's available."));
        }
        else if (node.getId() == startTimeField.getId() || node.getId() == endTimeField.getId()){
            String msg = "Between what two hours are you looking to see if a room is available?";
            startTimeField.setTooltip(new Tooltip(msg));
            endTimeField.setTooltip(new Tooltip(msg));
        }
        else if (node.getId() == timeLengthField.getId()){
            timeLengthField.setTooltip(new Tooltip("How long do you want to use this room?"));
        }
        else if (node.getId() == daysField.getId()){
            daysField.setTooltip(new Tooltip("What days of the week are you hoping to use this room?"));
        }
        else if (node.getId() == seatCapacityField.getId()){
            seatCapacityField.setTooltip(new Tooltip("What is the minimum number of seats you hope this room has?"));
        }
    }

    public String getLocationText() {
        return locationField.getText();
    }

    public String getStartTimeText(){
        return startTimeField.getText();
    }

    public String getEndTimeText(){
        return endTimeField.getText();
    }

    public String getTimeLengthText(){
        return timeLengthField.getText();
    }

    public String getDaysText(){
        return daysField.getText();
    }

    public String getSeatCapacityText(){
        return seatCapacityField.getText();
    }
}
