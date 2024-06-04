package org.example.game2048;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignUpController {
    protected GuiApplication application;
    @FXML
    public TextField signUpUsername;
    @FXML
    public PasswordField signUpPassword;
    @FXML
    public PasswordField signUpConfPs;
    @FXML
    public JFXButton buttonSignUp2;
    @FXML
    public JFXButton buttonReturn;
    protected GameData gameData;
    protected int[][] grid = new int[4][4];

    public void setApplication(GuiApplication application) {
        this.application = application;
    }

    @FXML
    protected void initialize() {
        buttonSignUp2.setOnAction(this::actionSignUp2);
        buttonReturn.setOnAction(this::actionReturn);
        buttonSignUp2.setOnKeyPressed(this::handleKeyPress);
        buttonReturn.setOnKeyPressed(this::handleKeyPress);
        signUpUsername.setOnKeyPressed(this::handleKeyPress);
        signUpPassword.setOnKeyPressed(this::handleKeyPress);
        signUpConfPs.setOnKeyPressed(this::handleKeyPress);
    }

    public void handleKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case ENTER:
                if (event.getSource() == buttonReturn) {
                    buttonReturn.fire();
                }

                break;
            case UP:
                if (event.getSource() == signUpPassword) {
                    signUpUsername.requestFocus();
                } else if (event.getSource() == signUpConfPs) {
                    signUpPassword.requestFocus();
                } else if (event.getSource() == buttonSignUp2) {
                    signUpConfPs.requestFocus();
                }
                break;
            case DOWN:
                if (event.getSource() == signUpUsername) {
                    signUpPassword.requestFocus();
                } else if (event.getSource() == signUpPassword) {
                    signUpConfPs.requestFocus();
                } else if (event.getSource() == signUpConfPs) {
                    buttonSignUp2.requestFocus();
                }
                break;
            case LEFT:
                buttonReturn.requestFocus();
        }
    }

    @FXML
    protected void actionSignUp2(ActionEvent event) {
        if (ifCanSignUp(signUpUsername.getText(), signUpPassword.getText(), signUpConfPs.getText())) {

            createGameDataFolder();
            createUserFolder(signUpUsername.getText());
            gameData = new GameData(new Label[4][4], 0, 0, signUpUsername.getText());
            saveGameData(signUpUsername.getText(), gameData);

            application.getStageSignUp().hide();
            application.getStageGame().show();
            signUpCheck();
        }
    }

    protected GameData getGameData() {
        return gameData;
    }

    @FXML
    protected void actionReturn(ActionEvent event) {
        application.stageSignUp.hide();
        application.stageLogIn.show();
    }

    protected Boolean ifCanSignUp(String username, String password, String confirmedPassword) {
        if (username.isEmpty() || password.isEmpty()) {
            signUpBlankError();
            return false;
        }
        if (username.length() < 6 || password.length() < 6) {
            signUpLengthError();
            return false;
        }
        if (isUsernameExists(username)) {
            signUpUsernameError();
            return false;
        }
        if (!password.equals(confirmedPassword)) {
            signUpPasswordError();
            return false;
        } else {
            saveUserToFile(username, hashPassword(password));
            return true;
        }
    }

    protected boolean isUsernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length > 0 && userInfo[0].equals(username)) {
                    return true; // 用户名已存在
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 用户名不存在
    }

    protected void saveUserToFile(String username, String hashedPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + hashedPassword);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void showErrorNotification(String title, String text) {
        Notifications notifications = Notifications.create();
        notifications.title(title);
        notifications.text(text);
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.BASELINE_RIGHT);
        notifications.show();
    }

    protected void signUpLengthError() {
        showErrorNotification("Error", "Error:The length of Username or password should bu lager than 6");
    }

    protected void signUpUsernameError() {
        showErrorNotification("Error", "Error:The Username has been used");
    }

    protected void signUpPasswordError() {
        showErrorNotification("Error", "Error:Your password is not the same with confirmed password");
    }

    protected void signUpBlankError() {
        showErrorNotification("Error", "Error:Please enter your username");
    }

    protected void showSuccessNotification(String title, String text) {
        Notifications notifications = Notifications.create();
        notifications.title(title);
        notifications.text(text);
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.BASELINE_RIGHT);
        notifications.show();
    }

    protected void signUpCheck() {
        showSuccessNotification("Success", "Welcome! " + signUpUsername.getText());
    }

    protected void createGameDataFolder() {
        // 构建 gameData 文件夹的路径
        String gameDataFolderPath = "gameData";
        // 创建 gameData 文件夹
        File gameDataFolder = new File(gameDataFolderPath);
        if (!gameDataFolder.exists()) {
            if (gameDataFolder.mkdirs()) {
                System.out.println("gameData 文件夹已创建：" + gameDataFolderPath);
            } else {
                System.err.println("无法创建 gameData 文件夹：" + gameDataFolderPath);
                //return;
            }
        } else {
            System.out.println("gameData 文件夹已存在：" + gameDataFolderPath);
        }
    }

    public static void createUserFolder(String username) {
        // 构建用户文件夹的路径
        String userFolderPath = "gameData/" + username;
        // 创建用户文件夹
        File userFolder = new File(userFolderPath);
        if (!userFolder.exists()) {
            if (userFolder.mkdirs()) {
                System.out.println("用户文件夹已创建：" + userFolderPath);
            } else {
                System.err.println("无法创建用户文件夹：" + userFolderPath);
                return;
            }
        } else {
            System.out.println("用户文件夹已存在：" + userFolderPath);
        }
    }

    public static void saveGameData(String username, GameData gameData) {
        // 构建存档文件的路径
        String filePath = "gameData/" + username + "/gameData.ser";

        // 保存游戏数据到存档文件
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(gameData);
            System.out.println("游戏数据已保存到存档文件：" + filePath);
        } catch (IOException e) {
            System.err.println("无法保存游戏数据到存档文件：" + filePath);
            e.printStackTrace();
        }
    }

}