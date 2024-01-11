package com.example.StickHeroApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("homescreen.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Stick Hero");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/character.png"))));
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        stage.setOnCloseRequest(e -> {
            e.consume();
            confirmExitGame(stage);
        });
    }

    public void confirmExitGame(Stage stage) {
        Alert exitConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirmation.setTitle("Exit Confirmation");
        exitConfirmation.setHeaderText("Confirm Exit");
        exitConfirmation.setContentText("Are you sure you want to exit the game? Press OK to exit, or press Cancel to return to the game.");

        if (exitConfirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.out.println("Exiting the game...");
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}