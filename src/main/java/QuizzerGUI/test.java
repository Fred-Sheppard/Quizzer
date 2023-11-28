package QuizzerGUI;
import java.util.*;

//Test class used to test JavaFX applicaitions.
public class test
{public static void main(String args[])
    {Scanner sc=new Scanner(System.in);
     System.out.println("Enter: \n(1) To test Login Page\n(2) To test Create Account\n(3) To test Quiz prompt\n(4) To test DisplayTextWindow");
     switch(sc.nextInt()){
      case 1:
       promptLogin LoginPage = new promptLogin();
       LoginPage.displayLoginPrompt();
       // Retrieve the entered username and password
       String user = LoginPage.getUser();
       String password = LoginPage.getPass();
       System.out.println("Username: " + user);
       System.out.println("Password: " + password);
       break;
      case 2:
       CreateAccount createAccountDialog = new CreateAccount();
       String[] accountDetails = createAccountDialog.display();
       System.out.println("Username: " + accountDetails[0]);
       System.out.println("Password: " + accountDetails[1]);
       System.out.println("Re-Entered Password: " + accountDetails[2]);
       break;
      case 3:
       PromptInput prompt = new PromptInput();
       int choice = prompt.display("Click a button, any button.");  // 3 for four choices: 0,1,2,3
       System.out.println("\nChoice is:"+choice);
      case 4:
       DisplayTextWindow.launchWindow("TEST");
     }
    }
}
