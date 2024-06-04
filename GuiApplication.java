package org.example.game2048;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static org.example.game2048.StageManager.*;

public class GuiApplication extends Application {
    protected Stage stageLogIn;
    protected Stage stageSignUp;
    //protected Stage stageFindPassword;
    protected Stage stageGame;
    protected GameData gameData;

    @Override
    public void start(Stage stage) throws Exception {
        stageLogIn = createStage("LogIn.fxml", "Game2048-LogIn");
        stageSignUp = createStage("SignUp.fxml", "Game2048-SignUp");
        //stageFindPassword = createStage("FindPassword", "Game2048-FindPassword");
        stageGame = createGameStage("Game.fxml", "Game2048-Play");

        initSignUpController();
        initGameController();
        initLogInController();
        stageLogIn.show();
    }

    public void initSignUpController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        try {
            Parent root = loader.load();
            SignUpController signUpController = loader.getController();
            signUpController.setApplication(this);
            stageSignUp = new Stage();
            stageSignUp.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initLogInController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
        try {
            Parent root = loader.load();
            LogInController logInController = loader.getController();
            logInController.setApplication(this);
            stageLogIn = new Stage();
            stageLogIn.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initGameController() {
        // 加载用户的游戏数据

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        try {
            Parent root = loader.load();
            GameController gameController = loader.getController();
            gameController.setApplication(this);
            stageGame = new Stage();
            stageGame.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameData() {
        // 这里加载游戏数据的代码，可以从文件或其他存储位置加载
        // 例如：
        gameData = GameData.loadData("gameData.ser");
    }

    public Stage getStageLogIn() {
        return stageLogIn;
    }

    public Stage getStageSignUp() {
        return stageSignUp;
    }

    //public Stage getStageFindPassword() {
    //  return stageFindPassword;
    //}
    public Stage getStageGame() {
        return stageGame;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
