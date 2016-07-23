package application;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RATableView implements Initializable{
    @FXML
    private VBox roomAvailabilityView;
    @FXML
    public TextField locationField, startTimeField, endTimeField, timeLengthField, daysField, seatCapacityField;
    @FXML
    public TableView<Room> roomSearchResults;

    public RATableView(){

    }

    public RATableView(Room [] rooms){
        for (Room room : rooms){
            this.roomSearchResults.getItems().add(room);
        }
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

    @FXML
    public void executeSearch(){
        if (fieldsAreEmpty()){
            MessageWindow errMsg = new MessageWindow("Search Error", "The fields are empty.");
            return;
        }
        RoomAvailabilitySearch ras = Main.currentRASearch;
        ras.setLocation(locationField.getText());
        ras.setStartTime(startTimeField.getText());
        ras.setEndTime(endTimeField.getText());
        ras.setTimeLength(timeLengthField.getText());
        ras.setDays(daysField.getText());
        ras.setSeatCapacity(seatCapacityField.getText());

        ras.generateResults();
    }

    public void clearTable(){
        roomSearchResults.getItems().clear();
    }

    public void addRoom(Room room){
        roomSearchResults.getItems().add(room);
    }

    public void setRooms(Room [] rooms){
        for (Room room : rooms){
            roomSearchResults.getItems().add(room);
        }
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
                seatCapacityField.getText()};
        for (String text : fieldTexts){
            if (!fieldIsEmpty(text)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void showToolTip(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        if (node.getId() == locationField.getId()){
            locationField.setTooltip(new Tooltip("Search for available rooms by building or building and room number."));
        }
        if (node.getId() == startTimeField.getId() || node.getId() == endTimeField.getId()){
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

    @FXML
    public void courseSearch(MouseEvent event) throws IOException {
        System.out.println("Now opening course search view.");
        final FXMLLoader loadFXML = new FXMLLoader(getClass().getResource("cstableview.fxml"));
        loadFXML.setController(Main.csTableView);
        Parent root = loadFXML.load();
        Scene homePageScene = new Scene(root, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        homePageScene.getStylesheets().add("application/styles.css");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Texas A&M University - Course Search");
        root.requestFocus();
        currentStage.setScene(homePageScene);
        currentStage.show();
    }
}
