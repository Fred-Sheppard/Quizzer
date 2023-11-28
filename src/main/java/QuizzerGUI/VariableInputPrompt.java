package QuizzerGUI;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX utility for displaying a prompt with buttons and retrieving user input.
 */
public class VariableInputPrompt {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final int FONT_SIZE = 18;
    private int result = 0;

    /**
     * Displays the input prompt and returns the user's choice.
     *
     * @param message     The message to be displayed in the prompt.
     * @param numChoices  The number of choices (buttons) in the prompt.
     * @return The user's choice.
     */
    public int showInputPrompt(String message, int numChoices) {
        Stage dialog = new Stage();
        BorderPane root = createDialogLayout(message, numChoices, dialog);
        dialog.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));

        showDialog(dialog);

        return result;
    }

    /**
     * Shows the dialog either directly or using Platform.runLater() based on the current thread.
     *
     * @param dialog The dialog stage to be shown.
     */
    private void showDialog(Stage dialog) {
        if (Platform.isFxApplicationThread()) {
            dialog.showAndWait();
        } else {
            Platform.runLater(dialog::showAndWait);
        }
    }

    /**
     * Creates the layout for the dialog, including the message, buttons, and styling.
     *
     * @param message    The message to be displayed.
     * @param numChoices The number of choices (buttons) in the dialog.
     * @param dialog     The dialog stage.
     * @return The BorderPane layout for the dialog.
     */
    private BorderPane createDialogLayout(String message, int numChoices, Stage dialog) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d1e7ff;");

        ImageView imageView = new ImageView(new Image("file:Quizzer.png"));
        root.setTop(imageView);
        BorderPane.setAlignment(imageView, Pos.CENTER);

        Text labelText = new Text(message);
        labelText.setFont(new Font("Helvetica", FONT_SIZE));
        root.setLeft(labelText);
        BorderPane.setMargin(labelText, new Insets(10));

        VBox buttonsBox = createButtons(numChoices, dialog);
        root.setCenter(buttonsBox);
        BorderPane.setMargin(buttonsBox, new Insets(10));

        return root;
    }

    /**
     * Creates and configures the VBox containing buttons for user choices.
     *
     * @param numChoices The number of choices (buttons) in the dialog.
     * @param dialog     The dialog stage.
     * @return The VBox containing buttons for user choices.
     */
    private VBox createButtons(int numChoices, Stage dialog) {
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        for (int i = 0; i <= numChoices; i++) {
            Button button = createButton(i, dialog);
            vBox.getChildren().add(button);
        }

        return vBox;
    }

    /**
     * Creates and configures a button for a specific choice.
     *
     * @param choice The choice associated with the button.
     * @param dialog The dialog stage.
     * @return The configured Button.
     */
    private Button createButton(int choice, Stage dialog) {
        Button button = new Button(String.valueOf(choice));
        button.setOnAction(event -> {
            result = choice;
            closeDialog(dialog);
        });
        return button;
    }

    /**
     * Closes the dialog either directly or using Platform.runLater() based on the current thread.
     *
     * @param dialog The dialog stage to be closed.
     */
    private void closeDialog(Stage dialog) {
        if (Platform.isFxApplicationThread()) {
            dialog.close();
        } else {
            Platform.runLater(dialog::close);
        }
    }
}