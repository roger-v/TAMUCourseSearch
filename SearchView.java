//SearchView.java

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchView implements Initializable{

    @FXML
    private VBox searchView;
    @FXML
    private TextField subjectField, courseNumberField, instructorField;

    @FXML private Button searchButton;
    @FXML private Label mainTitle, advancedLabel;

    //Advanced
    @FXML
    private HBox advancedFields;
    @FXML
    private TextField courseTitleField, daysField, startTimeField, endTimeField;

    public SearchView(){

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
    public void executeSearch(ActionEvent event) throws IOException{
        if (fieldsAreEmpty()){
            MessageWindow errMsg = new MessageWindow("Search Error", "The fields are empty.");
            return;
        }
        CourseSearch cs = Main.currentSearch;
        cs.setSubject(getSubjectText());
        cs.setInstructor(getInstructorText());
        cs.setCourseNumber(getCourseNumberText());
        cs.setCourseTitle(getCourseTitleText());
        cs.setStartTime(getStartTimeText());
        cs.setEndTime(getEndTimeText());
        cs.setDays(getDaysText());

        openSearchResultsView(event);
        try {
            cs.generateResults();
        }
        catch (SQLException e){
            System.out.println("SearchView.executeSearch(): " + e.getMessage());
        }
    }

    private void openSearchResultsView(ActionEvent event) throws IOException{
        final FXMLLoader loadFXML = new FXMLLoader(getClass().getResource("cstableview.fxml"));
        loadFXML.setController(Main.csTableView);
        Parent root = loadFXML.load();
        Scene homePageScene = new Scene(root, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        homePageScene.getStylesheets().add("application/styles.css");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Texas A&M University - Search Results");
        root.requestFocus();
        currentStage.setScene(homePageScene);
        currentStage.show();

        //So their search could carry on into the results.
        Main.csTableView.subjectField.setText(this.subjectField.getText());
        Main.csTableView.courseNumberField.setText(this.courseNumberField.getText());
        Main.csTableView.instructorField.setText(this.instructorField.getText());
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
            if (fieldIsEmpty(newValue)) {
                startTimeField.setText("");
            }
        }));

        endTimeField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (fieldIsEmpty(newValue)) {
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

    @FXML
    private void showToolTip(MouseEvent event) throws IOException {
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
            courseTitleField.setTooltip(new Tooltip("Type in words that may be part of the titles of courses."));
        }
        else if (node.getId() == startTimeField.getId() || node.getId() == endTimeField.getId()){
            startTimeField.setTooltip(new Tooltip("What's the earliest you want your class to start?"));
            endTimeField.setTooltip(new Tooltip("What's the latest you want the class to end?"));
        }
        else if (node.getId() == daysField.getId()){
            daysField.setTooltip(new Tooltip("Filter out lectures by typing M, T, W, R, or F, or any ORDERED combinations (such as MWF and TR)."));
        }
    }

    @FXML
    private void showAdvancedFields(MouseEvent event){
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
    private void showHelp(MouseEvent event){
        String str = "This application uses a filtering search\n" +
                "where you could leave some fields blank and\n" +
                "not others. For example, to search for all\n" +
                "CSCE courses, type \"CSCE\" into the subject\n" +
                "field and leave everything else blank. You could\n"+
                "even use wildcards - for example, type \"ACCT\"\n" +
                "in the subject field and \"4\" in the number field\n"+
                "for all 400-level ACCT courses.\n The same can be done"+
                " with other fields.\n";
        MessageWindow msg = new MessageWindow("Help", str);
    }

    @FXML
    private void roomAvailability(MouseEvent event) throws IOException {
        System.out.println("Now opening room availability view.");
        final FXMLLoader loadFXML = new FXMLLoader(getClass().getResource("roomavailabilityview.fxml"));
        loadFXML.setController(Main.roomAvailabilityView);
        Parent root = loadFXML.load();
        double width = ((Node) event.getSource()).getScene().getWidth();
        double height = ((Node) event.getSource()).getScene().getHeight();

        Scene homePageScene = new Scene(root, width, height);
        homePageScene.getStylesheets().add("application/styles.css");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Texas A&M University - Check for availability of rooms.");
        root.requestFocus();
        currentStage.setScene(homePageScene);
        currentStage.show();
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

    //Checks all the text field and make sure they have characters in them.
    public boolean hasAppropriateInput(){
        String [] texts = {
                subjectField.getText(),
                courseNumberField.getText(),
                instructorField.getText(),
                courseTitleField.getText(),
                startTimeField.getText(),
                endTimeField.getText(),
                daysField.getText()};
        boolean [] emptyField = {true, true, true, true};
        for (String text : texts){
            for (int i = 0; i < text.length(); i++){
                boolean isEmpty = (text.charAt(i) == ' ' || text.charAt(i) == '\n' || text.charAt(i) == '\t');
                if (!isEmpty) return true;
            }
        }
        return false;
    }

    public TextField getSubjectField() {
        return subjectField;
    }

    public TextField getCourseNumberField() {
        return courseNumberField;
    }

    public TextField getInstructorField() {
        return instructorField;
    }

    public TextField getCourseTitleField() {
        return courseTitleField;
    }

    public TextField getDaysField() {
        return daysField;
    }

    public TextField getStartTimeField() {
        return startTimeField;
    }

    public TextField getEndTimeField() {
        return endTimeField;
    }

    public String getSubjectText() {
        return subjectField.getText();
    }

    public String getCourseNumberText() {
        return courseNumberField.getText();
    }

    public String getInstructorText() {
        return instructorField.getText();
    }

    public String getCourseTitleText(){
        return courseTitleField.getText();
    }

    public String getStartTimeText(){
        return startTimeField.getText();
    }

    public String getEndTimeText(){
        return endTimeField.getText();
    }

    public String getDaysText(){
        return daysField.getText();
    }

}
