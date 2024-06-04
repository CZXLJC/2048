package org.example.game2048;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class StageManager {
    public static Stage createStage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(StageManager.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            return stage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Stage createGameStage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(StageManager.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(GuiApplication.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);
            return stage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
