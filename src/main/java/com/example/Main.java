package com.example;

import com.example.util.ConfigLoader;
import com.example.util.DatabaseInitializer;
import com.example.util.ResourcePathHelper;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

// JavaFX App
public class Main extends Application {

    private static Scene scene;
    private static StackPane rootContainer;
    private static final double ORIGINAL_WIDTH = 950;
    private static final double ORIGINAL_HEIGHT = 626;

    @Override
    public void start(Stage stage) throws IOException {
        // Load configuration before anything else
        ConfigLoader.loadProperties();
        
        // Initialize database schema
        DatabaseInitializer.initializeDatabase();

        Parent content = loadFXML("LandingPage");
        rootContainer = new StackPane(content);
        scene = new Scene(rootContainer);

        bindSize(content);

        stage.setScene(scene);
        stage.setTitle("Passport Application System");
        stage.setFullScreen(true);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent content = loadFXML(fxml);
        bindSize(content);
        rootContainer.getChildren().setAll(content);
    }

    private static void bindSize(Parent content) {
        NumberBinding scaleBinding = Bindings.min(scene.widthProperty().divide(ORIGINAL_WIDTH), scene.heightProperty().divide(ORIGINAL_HEIGHT));
        content.scaleXProperty().bind(scaleBinding);
        content.scaleYProperty().bind(scaleBinding);
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