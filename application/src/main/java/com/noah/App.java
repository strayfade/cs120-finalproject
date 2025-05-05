package com.noah;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainForm.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 480);
        Font.loadFont(getClass().getResourceAsStream("Montserrat.ttf"), 10);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);

        // Set window title and icon
        stage.setTitle("Anonymous Messaging Application");
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
    
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}