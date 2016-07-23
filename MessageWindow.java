package application;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MessageWindow {

    public MessageWindow(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        FadeTransition ft = new FadeTransition(Duration.millis(300), layout);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("application/styles.css");
        window.setResizable(false);
        window.setScene(scene);
        window.showAndWait();
    }

}
