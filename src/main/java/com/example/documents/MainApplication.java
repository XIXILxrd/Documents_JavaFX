package com.example.documents;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends javafx.application.Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Documents");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setStage(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setMainScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main_view.fxml"));
        try {
            Scene mainScene = new Scene(fxmlLoader.load());
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}