package src;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*JavaFX Application that displays the text passed to it in Helvetica, size 48. Call with the following:
*DisplayTextWindow.launchWindow("YOUR STRING HERE");
*/

public class DisplayTextWindow extends Application {

    private static String displayText = "Default Text";
    private static Text textNode = new Text();
    private static Stage windowStage;
    
    public static void launchWindow(String text) {
        displayText = text;
        if (windowStage == null) {
            new Thread(() -> Application.launch(DisplayTextWindow.class)).start();
        } else {
            Platform.runLater(() -> {
                textNode.setText(displayText);
                if (!windowStage.isShowing()) {
                    windowStage.show();
                }
            });
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        windowStage = primaryStage;

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #d1e7ff;");
        
        textNode.setFont(Font.font("Helvetica", 48));
        textNode.setText(displayText);
        
        root.getChildren().add(textNode);
        primaryStage.setTitle("Display Text Window");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> windowStage = null);
    }
}
