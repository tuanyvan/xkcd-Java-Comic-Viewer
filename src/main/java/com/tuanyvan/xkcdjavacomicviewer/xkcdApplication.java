package com.tuanyvan.xkcdjavacomicviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class xkcdApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(xkcdApplication.class.getResource("xkcd-repository-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("xkcd Comic Viewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}