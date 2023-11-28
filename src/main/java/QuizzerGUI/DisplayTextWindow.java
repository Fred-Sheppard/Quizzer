package QuizzerGUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX application to display text in a window.
 * Call with DisplayTextWindow.launchWindow("YOUR STRING HERE");
 */
public class DisplayTextWindow extends Application {

    private static String displayText = "Default Text";
    private static final Text textNode = new Text();
    private static Stage windowStage;

    public static void launchWindow(String text) {
        displayText = text;
        if (windowStage == null) {
            new Thread(() -> Application.launch(DisplayTextWindow.class)).start();
        } else {
            Platform.runLater(DisplayTextWindow::updateWindow);
        }
    }

    private static void updateWindow() {
        textNode.setText(displayText);
        if (!windowStage.isShowing()) {
            windowStage.show();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        windowStage = primaryStage;

        StackPane root = createRootPane();
        setupTextNode();

        primaryStage.setTitle("Display Text Window");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        setOnCloseRequest(primaryStage);
    }

    private StackPane createRootPane() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #d1e7ff;");
        return root;
    }

    private void setupTextNode() {
        textNode.setFont(Font.font("Helvetica", 48));
        textNode.setText(displayText);
        ((StackPane) windowStage.getScene().getRoot()).getChildren().add(textNode);
    }

    private void setOnCloseRequest(Stage primaryStage) {
        primaryStage.setOnCloseRequest(e -> windowStage = null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
