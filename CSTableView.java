package application;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by PASTACORK on 6/12/2016.
 */

//Course Search View
public class CSTableView implements Initializable {

    //THIS CLASS IS NOT FUNCTIONING!
    //TODO - MAKE A SAMPLE TABLE AND DISPLAY IT (ROGER)

    @FXML
    private VBox searchView, advancedFields;
    @FXML
    public TextField subjectField, courseNumberField, instructorField, courseTitleField, startTimeField, endTimeField, daysField;
    @FXML
    public TableView <Course> courseSearchResults;

    public CSTableView(){

    }

    public CSTableView(Course [] courses){
        for (Course course : courses){
            this.courseSearchResults.getItems().add(course);
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
        for (Node n : searchView.getChildren()){
            if (n.getId() != advancedFields.getId())
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
        CourseSearch cs = Main.currentSearch;
        cs.setSubject(subjectField.getText());
        cs.setInstructor(instructorField.getText());
        cs.setCourseNumber(courseNumberField.getText());
        cs.setCourseTitle(courseTitleField.getText());
        cs.setStartTime(startTimeField.getText());
        cs.setEndTime(endTimeField.getText());
        cs.setDays(daysField.getText());

        try {
            cs.generateResults();
        } catch (SQLException e){
            System.out.println("CSTableView.executeSearch(): " + e.getMessage());
        }
    }

    public void clearTable(){
        courseSearchResults.getItems().clear();
    }

    public void addCourse(Course course){
        courseSearchResults.getItems().add(course);
    }

    public void setCourses(Course [] courses){
        for (Course course : courses){
            courseSearchResults.getItems().add(course);
        }
    }

    private void formatUserInput(){
        //The code within the lambda statement below prevents the user from typing anything nonalphabetic and exceeding four characters.
        subjectField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)){
                subjectField.setText("");
            }
            else if (!Character.isAlphabetic(newValue.charAt(newValue.length() - 1))){
                subjectField.setText(oldValue);
            }

            //length restricted to 4 characters
            if (newValue.length() > 4){
                subjectField.setText(oldValue);
            }
        });

        //Allows only up to three numbers to be placed within the text field.
        courseNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)){
                courseNumberField.setText("");
            }
            else if (!Character.isDigit(newValue.charAt(newValue.length() - 1))){
                courseNumberField.setText(oldValue);
            }

            //length restricted to 4 characters
            if (newValue.length() > 3){
                courseNumberField.setText(oldValue);
            }
        });
        instructorField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)){
                instructorField.setText("");
            }
        });

        startTimeField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)){
                startTimeField.setText("");
            }


        }));

        endTimeField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)){
                endTimeField.setText("");
            }


        }));

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
                subjectField.getText(),
                courseNumberField.getText(),
                instructorField.getText(),
                courseTitleField.getText(),
                startTimeField.getText(),
                endTimeField.getText(),
                daysField.getText()};
        for (String text : fieldTexts){
            if (!fieldIsEmpty(text)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    public void showAdvancedFields(MouseEvent event){
        FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(500), advancedFields);
        if (!advancedFields.isVisible()) {
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
            advancedFields.setVisible(true);
        }
        else {
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();
            ft.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    advancedFields.setVisible(false);
                }
            });
        }
    }

    @FXML
    public void showToolTip(MouseEvent event){
        Node node = (Node) event.getSource();
        if (node.getId() == subjectField.getId()){
            subjectField.setTooltip(new Tooltip("Type in a four-letter subject."));
        }
        else if (node.getId() == courseNumberField.getId()){
            courseNumberField.setTooltip(new Tooltip("Type in a three-digit course number."));
        }
        else if (node.getId() == instructorField.getId()){
            instructorField.setTooltip(new Tooltip("Type in the name of an instructor."));
        }
        else if (node.getId() == courseTitleField.getId()){
            courseTitleField.setTooltip(new Tooltip("Type in something that may be part of course titles."));
        }
        else if (node.getId() == startTimeField.getId()&& node.getId() == endTimeField.getId()){
            startTimeField.setTooltip(new Tooltip("What's the earliest you want your class to start?"));
            endTimeField.setTooltip(new Tooltip("What's the latest you want your class to end?"));
        }
        else if (node.getId() == daysField.getId()){
            daysField.setTooltip(new Tooltip("Filter out class days by typing M, T, W, R, or F, or any ORDERED combinations (such as MWF and TR)."));
        }
    }

    @FXML
    public void roomAvailability(MouseEvent event) throws IOException{
        System.out.println("Now opening room availability view.");
        final FXMLLoader loadFXML = new FXMLLoader(getClass().getResource("ratableview.fxml"));
        loadFXML.setController(Main.raTableView);
        Parent root = loadFXML.load();
        Scene homePageScene = new Scene(root, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        homePageScene.getStylesheets().add("application/styles.css");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Texas A&M University - Check for availability of rooms.");
        root.requestFocus();
        currentStage.setScene(homePageScene);
        currentStage.show();
    }
}
