import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*JavaFX Application to Show have two text boxes, which will take username and password, and return them to original class. Call with the following:
LoginPage loginPage = new LoginPage();
String[] credentials = loginPage.display();
String user=credentials[0];
String pass=credentials[1];
*/

public class promptLogin {

    private String user;
    private String pass;
    private int fontSize = 18;

    public String[] display() {
        Stage dialog = new Stage();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d1e7ff;");

        // "Welcome to Quizzer!" text
        Text welcomeText = new Text("Welcome to Quizzer!");
        welcomeText.setFont(new Font("Helvetica", 36));

        // Image under welcome text
        ImageView imageView = new ImageView(new Image("file:Quizzer.png"));

        VBox topBox = new VBox(welcomeText, imageView);
        topBox.setSpacing(20);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(5, 0, 0, 0));
        root.setTop(topBox);

        // Username and Password input in the center
        GridPane inputPane = new GridPane();
        inputPane.setVgap(10);

        // Setting the column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(75);
        col2.setHgrow(Priority.ALWAYS);
        inputPane.getColumnConstraints().addAll(col1, col2);

        Text userText = new Text("Username:");
        userText.setFont(new Font("Helvetica", fontSize));

        TextField userField = new TextField();
        userField.setPromptText("Enter your username");
        GridPane.setHgrow(userField, Priority.ALWAYS);
        GridPane.setMargin(userField, new Insets(0, 0, 0, -10));

        Text passText = new Text("Password:");
        passText.setFont(new Font("Helvetica", fontSize));

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        GridPane.setHgrow(passField, Priority.ALWAYS);
        GridPane.setMargin(passField, new Insets(0, 0, 0, -10));

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            user = userField.getText();
            pass = passField.getText();
            dialog.close();
        });

        inputPane.add(userText, 0, 0);
        inputPane.add(userField, 1, 0);
        inputPane.add(passText, 0, 1);
        inputPane.add(passField, 1, 1);
        inputPane.add(loginButton, 1, 2);

        root.setCenter(inputPane);

        dialog.setScene(new Scene(root, 600, 400));
        dialog.showAndWait();

        return new String[] { user, pass };
    }
}
