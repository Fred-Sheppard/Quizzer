package src;

import java.io.*;
import java.util.*;
//Test class used to test JavaFX applicaitions. DO NOT SHIP WITH FINAL EDIITION
public class test
{public static void main(String args[])
    {DisplayTextWindow.launchWindow("TEST");
     promptLogin loginPage = new promptLogin();
     String[] credentials = loginPage.display();
     System.out.println("Username: " + credentials[0]);
     System.out.println("Password: " + credentials[1]);
     PromptInput prompt = new PromptInput();
     int choice = prompt.display("What does CPU stand for?\n1)Central Processing Unit\n2)Control and Processing Utility\n3)Computer Performance Usage\n4)Core Power Usage\nEASY", 3);  // 3 for four choices: 0,1,2,3
     System.out.println("\nChoice is:"+choice);
     CreateAccount createAccountDialog = new CreateAccount();
     String[] accountDetails = createAccountDialog.display();
     System.out.println("Username: " + accountDetails[0]);
     System.out.println("Password: " + accountDetails[1]);
     System.out.println("Re-Entered Password: " + accountDetails[2]);
    }
}
