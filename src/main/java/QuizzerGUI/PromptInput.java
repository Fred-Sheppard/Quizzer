package QuizzerGUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
/*JavaFX Application that uses QuizPrompt.fxml to display a question and give 4 buttons, 0,1,2,3, which upon pressing returns the value on the button, so when "0" button is pressed, 0 is returned as an integer.
 *Call using the following code:
 * PromptInput prompt = new PromptInput();
   int choice = prompt.display("YOUR MESSAGE HERE");
 */
public class PromptInput {

    private int result = 0;

    public int display(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuizPrompt.fxml"));
            AnchorPane root = loader.load();

            QuizPromptController controller = loader.getController();
            controller.setMessage(message);
            controller.setPromptInput(this);

            Stage dialog = new Stage();
            dialog.setTitle("Quizzer");
            dialog.setScene(new Scene(root, 400, 300));
            dialog.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();//using try-catch to catch errors when loading .fxml files
            }

        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
