package src;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
/* JavaFX Application that, when called, Displays a window with 3 fields, one for username, password, and re-entered password.
        CreateAccount ca=new CreateAccount();
        String[] accountDetails = ca.display();
        String pass=accountDetails[1];
        String re-enter_pass=accountDetails[2];
*/
public class CreateAccount {

    private String username;
    private String password;
    private String reEnteredPassword;

    public String[] display() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d1e7ff;");

        Text title = new Text("Create an Account:");
        title.setFont(new Font("Helvetica", 36));
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20, 0, 0, 0));

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        Text userLabel = new Text("Create Username:");
        userLabel.setFont(new Font("Helvetica", 18));
        grid.add(userLabel, 0, 0);

        TextField userField = new TextField();
        grid.add(userField, 1, 0);

        Text passLabel = new Text("Enter Password:");
        passLabel.setFont(new Font("Helvetica", 18));
        grid.add(passLabel, 0, 1);

        PasswordField passField = new PasswordField();
        grid.add(passField, 1, 1);

        Text reEnterLabel = new Text("Re-Enter Password:");
        reEnterLabel.setFont(new Font("Helvetica", 18));
        grid.add(reEnterLabel, 0, 2);

        PasswordField reEnterField = new PasswordField();
        grid.add(reEnterField, 1, 2);

        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            username = userField.getText();
            password = passField.getText();
            reEnteredPassword = reEnterField.getText();
            dialog.close();
        });

        VBox centerBox = new VBox(10, grid, submit);
        centerBox.setAlignment(Pos.CENTER);

        root.setCenter(centerBox);

        dialog.setScene(new Scene(root, 400, 300));
        dialog.showAndWait();

        return new String[] { username, password, reEnteredPassword };
    }
}
