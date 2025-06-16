module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;

    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.controller.components to javafx.fxml;
    opens com.example.model to javafx.base;
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.dao;
}
