package QuizzerGUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX application for creating a user account.
 * Call with CreateAccount ca = new CreateAccount();
 * String[] accountDetails = ca.display();
 * String username = accountDetails[0];
 * String password = accountDetails[1];
 * String reEnteredPassword = accountDetails[2];
 */
public class CreateAccount {

    // Instance variables to store user input
    private String username;
    private String password;
    private String reEnteredPassword;

    /**
     * Displays the user account creation window and captures user input.
     *
     * @return An array containing the username, password, and re-entered password.
     */
    public String[] display() {
        Stage dialog = createDialog();

        BorderPane root = createRootPane();

        Text title = createTitleText();

        GridPane grid = createGridPane();

        addTextFieldToGrid(grid, userField);
        addPasswordFieldToGrid(grid, "Enter Password:", 1, passField);
        addPasswordFieldToGrid(grid, "Re-Enter Password:", 2, reEnterField);

        Button submit = createSubmitButton(dialog);

        VBox centerBox = createCenterBox(grid, submit);

        setRootCenter(root, centerBox);

        setDialogScene(dialog, root);

        return waitForUserInput();
    }

    /**
     * Creates a new dialog stage for the user account creation window.
     *
     * @return A new Stage for the user account creation dialog.
     */
    private Stage createDialog() {
        return new Stage();
    }

    /**
     * Creates the root pane for the user account creation window.
     *
     * @return A new BorderPane as the root pane.
     */
    private BorderPane createRootPane() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d1e7ff;");
        return root;
    }

    /**
     * Creates a Text element for the title of the user account creation window.
     *
     * @return A Text element with the specified text and formatting.
     */
    private Text createTitleText() {
        Text title = new Text("Create an Account:");
        title.setFont(new Font("Helvetica", 36));
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20, 0, 0, 0));
        return title;
    }

    /**
     * Creates a GridPane for organizing input fields in a grid layout.
     *
     * @return A new GridPane for organizing components.
     */
    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    /**
     * Adds a text field to the specified grid row with the given label.
     *
     * @param grid      The GridPane to add the text field to.
     * @param textField The TextField to be added.
     */
    private void addTextFieldToGrid(GridPane grid, TextField textField) {
        Text label = createLabel("Create Username:");
        grid.add(label, 0, 0);
        grid.add(textField, 1, 0);
    }

    /**
     * Adds a password field to the specified grid row with the given label.
     *
     * @param grid           The GridPane to add the password field to.
     * @param labelText      The label text for the password field.
     * @param row            The row in which to add the password field.
     * @param passwordField  The PasswordField to be added.
     */
    private void addPasswordFieldToGrid(GridPane grid, String labelText, int row, PasswordField passwordField) {
        Text label = createLabel(labelText);
        grid.add(label, 0, row);
        grid.add(passwordField, 1, row);
    }

    /**
     * Creates a Text element with the specified text and font size.
     *
     * @param text The text to be displayed.
     * @return A Text element with the specified text and font size.
     */
    private Text createLabel(String text) {
        Text label = new Text(text);
        label.setFont(new Font("Helvetica", 18));
        return label;
    }

    /**
     * Creates a Submit button for the user account creation window.
     *
     * @param dialog The dialog stage associated with the button.
     * @return A new Button with the label "Submit" and associated action.
     */
    private Button createSubmitButton(Stage dialog) {
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            username = userField.getText();
            password = passField.getText();
            reEnteredPassword = reEnterField.getText();
            dialog.close();
        });
        return submit;
    }

    /**
     * Creates a VBox to organize components vertically.
     *
     * @param grid   The GridPane to be included in the VBox.
     * @param submit The Submit button to be included in the VBox.
     * @return A new VBox containing the specified components.
     */
    private VBox createCenterBox(GridPane grid, Button submit) {
        return new VBox(10, grid, submit);
    }

    /**
     * Sets the center of the root pane to the specified centerBox.
     *
     * @param root      The BorderPane representing the root pane.
     * @param centerBox The VBox to be set as the center of the root pane.
     */
    private void setRootCenter(BorderPane root, VBox centerBox) {
        root.setCenter(centerBox);
    }

    /**
     * Sets the scene of the dialog stage with the specified root pane.
     *
     * @param dialog The dialog stage to set the scene for.
     * @param root   The BorderPane to be set as the root of the scene.
     */
    private void setDialogScene(Stage dialog, BorderPane root) {
        dialog.setScene(new Scene(root, 400, 300));
        dialog.showAndWait();
    }

    /**
     * Waits for user input and returns the captured username, password, and re-entered password.
     *
     * @return An array containing the username, password, and re-entered password.
     */
    private String[] waitForUserInput() {
        return new String[]{username, password, reEnteredPassword};
    }

    // Instance variables for user input fields
    private final TextField userField = new TextField();
    private final PasswordField passField = new PasswordField();
    private final PasswordField reEnterField = new PasswordField();
}
