package org.example.game2048;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LogInController {
    protected GuiApplication application;
    @FXML
    public JFXButton buttonGuestLogIn;
    @FXML
    public JFXButton buttonLogIn;
    @FXML
    public JFXButton buttonFindPassword;
    @FXML
    public JFXButton buttonSignUp;
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    protected GameData gameData;

    public void setApplication(GuiApplication application) {
        this.application = application;
    }
    protected GameData getGameData(){
        return gameData;
    }

    @FXML
    private void initialize() {
        buttonLogIn.setOnAction(this::actionLogIn);
        buttonSignUp.setOnAction(this::actionSignUp);
        buttonFindPassword.setOnAction(this::actionFindPassword);
        buttonGuestLogIn.setOnKeyPressed(this::handleKeyPress);

        txtUsername.setOnKeyPressed(this::handleKeyPress);
        txtPassword.setOnKeyPressed(this::handleKeyPress);

    }

    public void handleKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case ENTER:
                if (event.getSource() == buttonLogIn) {
                    buttonLogIn.fire();
                } else if (event.getSource() == buttonGuestLogIn) {
                    buttonGuestLogIn.fire();
                } else if (event.getSource() == buttonFindPassword) {
                    buttonFindPassword.fire();
                } else if (event.getSource() == buttonSignUp) {
                    buttonLogIn.fire();
                }
                break;
            case UP:
                if (event.getSource() == buttonLogIn) {
                    buttonFindPassword.requestFocus();
                } else if (event.getSource() == buttonFindPassword || event.getSource() == buttonSignUp) {
                    txtPassword.requestFocus();
                } else if (event.getSource() == txtPassword) {
                    txtUsername.requestFocus();
                } else if (event.getSource() == buttonGuestLogIn) {
                    buttonLogIn.requestFocus();
                }
                break;
            case DOWN:
                if (event.getSource() == txtUsername) {
                    txtPassword.requestFocus();
                } else if (event.getSource() == txtPassword) {
                    buttonFindPassword.requestFocus();
                } else if (event.getSource() == buttonFindPassword) {
                    buttonSignUp.requestFocus();
                } else if (event.getSource() == buttonSignUp) {
                    buttonLogIn.requestFocus();
                } else if (event.getSource() == buttonLogIn) {
                    buttonGuestLogIn.requestFocus();
                }
                break;
            case LEFT:
                if (event.getSource() == buttonSignUp) {
                    buttonFindPassword.requestFocus();
                }
                break;
            case RIGHT:
                if (event.getSource() == buttonFindPassword) {
                    buttonSignUp.requestFocus();
                }
                break;
        }
    }

    @FXML
    private void actionLogIn(ActionEvent event) {
        if (ifCanLogIn(txtUsername.getText(), txtPassword.getText())) {
            application.getStageLogIn().hide();
            application.getStageGame().show();
            logInCheck();
        }
    }



    @FXML
    private void actionSignUp(ActionEvent event) {
        application.getStageLogIn().hide();
        application.getStageSignUp().show();
    }

    @FXML
    private void actionFindPassword(ActionEvent event) {
        application.getStageLogIn().hide();
        //application.getStageFindPassword().show();
    }

    @FXML
    public void actionGuestLogIn(ActionEvent event) {
        application.stageLogIn.hide();
        application.stageGame.show();
    }

    private boolean ifCanLogIn(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            logInEmptyError();
            return false;
        }
        if (!isUsernameExists(username)) {
            logInExistsError();
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length > 0 && userInfo[0].equals(username) && userInfo[1].equals(hashPassword(password))) {
                    return true; // 可以登录
                } else {
                    logInError();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 密码不正确
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

    protected void logInError() {
        showErrorNotification("Error", "Error: Username or password is incorrect");
    }

    protected void logInExistsError() {
        showErrorNotification("Error", "Error:Username is not exist");
    }

    protected void logInEmptyError() {
        showErrorNotification("Error", "Error: Username or password is not complete");
    }

    protected void showSuccessNotification(String title, String text) {
        Notifications notifications = Notifications.create();
        notifications.title(title);
        notifications.text(text);
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.BASELINE_RIGHT);
        notifications.show();
    }

    protected void logInCheck() {
        showSuccessNotification("Great log in", "Welcome! " + txtUsername.getText());
    }

}
