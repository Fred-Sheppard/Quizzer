package QuizzerGUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class promptLogin extends Application {

    private String user;
    private String pass;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController(); // Establishing LoginController.java as the controller class

        Scene scene = new Scene(root, 640, 400);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.showAndWait();  // Wait for the login window to close

        // Retrieve user and pass from the controller
        user = controller.getUser();
        pass = controller.getPass();
    }

    // Getter method for username
    public String getUser() {
        return user;
    }

    // Getter method for password
    public String getPass() {
        return pass;
    }

    // Method to show the login window and retrieve user and pass
    public void displayLoginPrompt() {
        // Ensure UI-related code is executed on the JavaFX Application Thread
        Platform.runLater(() -> {
            launch();
        });    }

    public static void main(String[] args) {
        // You can still use this main method for testing the login prompt
        // launch(args);
    }
}
