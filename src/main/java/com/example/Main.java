package com.example;

import com.example.util.ConfigLoader;
import com.example.util.dbUtil;
import com.example.util.DatabaseInitializer;
import com.example.util.ResourcePathHelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Load configuration before anything else
        ConfigLoader.loadProperties();
        
        // Initialize database schema
        DatabaseInitializer.initializeDatabase();

        scene = new Scene(loadFXML("LandingPage"), 950, 626);
        stage.setScene(scene);
        stage.setTitle("Passport Application System");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        String path = ResourcePathHelper.getFXMLPath(fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}