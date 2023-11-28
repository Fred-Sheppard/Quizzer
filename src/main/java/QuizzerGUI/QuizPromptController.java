package QuizzerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

//Controller class for QuizPrompt.fxml, logic is handled in PromptInput

public class QuizPromptController {

    @FXML
    private Label messageLabel;

    private PromptInput promptInput;

    // Setter method for the message
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setPromptInput(PromptInput promptInput) {
        this.promptInput = promptInput;
    }

    // Handler method for button actions
    @FXML
    private void handleButtonAction(ActionEvent event) {
        String buttonText = ((Button) event.getSource()).getText();
        int choice = Integer.parseInt(buttonText);

        if (choice >= 0 && choice <= 3) {
            promptInput.setResult(choice);
            ((Button) event.getSource()).getScene().getWindow().hide();
        }
    }
}
