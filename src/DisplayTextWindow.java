package src;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class DisplayTextWindow extends Application {
    private static String displayText = "Default Text";
    public static void launchWindow(String text) {
        displayText = text;
    }
    @Override
    public void start(Stage primaryStage) {
        Text textNode = new Text(displayText);
        textNode.setFont(Font.font("Helvetica", 48));

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #d1e7ff;");
        root.getChildren().add(textNode);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Display Text Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
