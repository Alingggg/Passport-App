package com.example;

import com.example.util.ConfigLoader;
import com.example.util.dbUtil;
import com.example.util.DatabaseInitializer;
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

        scene = new Scene(loadFXML("UserViewDetails"), 950, 626);
        stage.setScene(scene);
        stage.setTitle("Passport Application System");
        stage.show();
    }

    @Override
    public void stop() {
        // Close database connection when application exits
        dbUtil.closeConnection();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Check if the path already contains a directory separator
        String path;
        if (fxml.contains("/")) {
            // If it already has a path separator, use as is
            path = "fxml/" + fxml + ".fxml";
        } else {
            // If it's just a filename, try to determine correct directory based on file name
            if (fxml.startsWith("User")) {
                path = "fxml/User/" + fxml + ".fxml";
            } else if (fxml.startsWith("Admin")) {
                path = "fxml/Admin/" + fxml + ".fxml";
            } else {
                // Default location for common files
                path = "fxml/" + fxml + ".fxml";
            }
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}