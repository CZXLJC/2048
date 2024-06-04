package org.example.game2048;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class ShowNotifications {
    protected TextField signUpUsername;

    public void showNotification(String title, String text) {
        Notifications notifications = Notifications.create();
        notifications.title(title);
        notifications.text(text);
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.BASELINE_RIGHT);
        if (title=="Sorry"){
            notifications.text("Sorry! The name has been used.");
        }
        notifications.show();
    }

    private void logInError() {
        showNotification("Error", "Error: Username or password is incorrect");
    }

    private void logInExistsError() {
        showNotification("Error", "Error:Username is not exist");
    }

    private void logInEmptyError() {
        showNotification("Error", "Error: Username or password is not complete");
    }

    private void logInCheck() {
        showNotification("Success", "Welcome");
    }

    private void signUpLengthError() {
        showNotification("Error", "Error:The length of Username or password should bu lager than 6");
    }

    private void signUpUsernameError() {
        showNotification("Error", "Error:The Username has been used");
    }

    private void signUpCheck() {
        showNotification("Success", "Welcome" + signUpUsername.getText());
    }

    private void signUpPasswordError() {
        showNotification("Error", "Error:Your password is not the same with confirmed password");
    }

    private void signUpBlankError() {
        showNotification("Error", "Error:Please enter your username");
    }
}
