package src;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
/*
  JavaFX Application that opens a new window and displays a string passed to it, then asks the user to press any of a given number of buttons
  Call it in another class with the following code:
  PromptInput prompt = new PromptInput();
  int choice = prompt.display("Your question here", n);  
  choice is the given output, which can be 0,1,2,3...
  n is the number of buttons-1
 */

public class PromptInput {

    private int fontSize = 18;
    private int result = 0; 

    public int display(String message, int numChoices) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);  // Block other windows until this is closed
        
        // Background color
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d1e7ff;");

        // Set image at the top
        ImageView imageView = new ImageView(new Image("file:Quizzer.png"));
        root.setTop(imageView);
        BorderPane.setAlignment(imageView, Pos.CENTER);

        // Display the string message on the left
        Text labelText = new Text(message); 
        labelText.setFont(new Font("Helvetica", fontSize)); // Font type and size
        root.setLeft(labelText);
        BorderPane.setMargin(labelText, new Insets(10));

        // Create buttons in the center
        VBox buttonsBox = createButtons(numChoices, dialog);
        root.setCenter(buttonsBox);
        BorderPane.setMargin(buttonsBox, new Insets(10));

        dialog.setScene(new Scene(root, 400, 300));
        dialog.showAndWait();  // Block until closed
        
        return result;  // Return result
    }

    private VBox createButtons(int numChoices, Stage dialog) {
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);  // Align buttons in center

        for (int i = 0; i <= numChoices; i++) {
            Button button = new Button(String.valueOf(i));
            int choice = i; // Capture the value for the lambda
            button.setOnAction(e -> {
                result = choice;  // Store result
                dialog.close();  // Close dialog
            });
            vBox.getChildren().add(button);
        }
        return vBox;
    }
}
