package QuizzerGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//Controller class for LoginPrompt.fxml, logic is handled in promptLogin.java

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private String user;
    private String pass;

    // Handler method for the login button
    @FXML
    private void loginButtonAction() {
        user = usernameField.getText();
        pass = passwordField.getText();
        // Close the window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    // Getter methods for the user and pass
    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
