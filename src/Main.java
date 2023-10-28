import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    /**
     * ASCII font of the Quizzer logo
     */
    static final String QUIZZER = """
              ____  _    _ _____ __________________ _____
            /  __ \\| |  | |_   _|___  /___  /  ____|  __ \\
            | |  | | |  | | | |    / /   / /| |__  | |__) |
            | |  | | |  | | | |   / /   / / |  __| |  _  /
            | |__| | |__| |_| |_ / /__ / /__| |____| | \\ \\
             \\___\\_\\\\____/|_____/_____/_____|______|_|  \\_\\""";

    public static void main(String[] args) {
        // Print message when the program closes, even unexpectedly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearScreen();
            System.out.println("Thanks for playing!");
            System.out.println(QUIZZER);
        }));

        clearScreen();
        // Welcome messages
        System.out.println(QUIZZER);
        //System.out.println("Welcome to Quizzer!");
        promptEnter();
        QuestionLoader loader = new QuestionLoader(new File("res/questions/"));
        Scanner scanner = new Scanner(System.in);
        String[] topics = loader.listTopics();
        // Login
        Login login = new Login(new File("GameData/users.txt"));
        String loginPrompt = """
                Are you a new or existing user?
                (0) New
                (1) Existing""";
        PromptInput prompt = new PromptInput();
        boolean isExistingUser = prompt.display(loginPrompt, 1) == 1;
        User user;
        if (isExistingUser) {
            user = promptLogin(login);
        } else {
            user = promptCreateUser(login);
        }

        // Continuously ask questions
        //noinspection InfiniteLoopStatement
        while (true) {
            int choice = chooseTopic(topics, scanner);
            if (choice == topics.length) {
                showStats(user);
                continue;
            }
            Quiz quiz = new Quiz(topics[choice], user, loader, scanner);
            PromptInput prompt = new PromptInput();
            int mode = prompt.display("""
                    Choose a gamemode:
                    (0) Random
                    (1) Escalation
                    (2) Redemption""", 2);
            switch (mode) {
                case 0 -> quiz.askRandom();
                case 1 -> quiz.askEscalation();
                case 2 -> quiz.askRedemption();
                default -> {
                } //unreachable, since PromptInput does not allow invalid inputs
            }
        }
    }

    /**
     * Prompts the user to select a topic to be quizzed on
     *
     * @param topics  The list of available topics
     * @param scanner Scanner object to receive input
     * @return The user's choice
     */
    public static int chooseTopic(String[] topics, Scanner scanner) {
        int options = topics.length;
        StringBuilder builder = new StringBuilder("Select a topic to begin:\n");
        for (int i = 0; i < options; i++) {
            builder.append(String.format("(%d) %s%n", i, topics[i]));
        }
        builder.append(String.format("%n(%d) %s", options, "Show stats"));
        PromptInput prompt = new PromptInput();
        return prompt.display(builder.toString(), options);
    }

    /**
     * Repeatedly prompts the user for a valid numerical input.
     * Valid inputs include any integer from 0 to `maxValid`, inclusive.
     *
     * @param maxValid The largest number input that is valid
     * @param prompt   Prompt to display to the user displaying valid options
     * @param input    Scanner object to receive input
     */
   
    /* Use Code only for CLI
    public static int promptInput(int maxValid, String prompt, Scanner input) {
        while (true) {
            clearScreen();
            System.out.println(prompt);
            System.out.printf("%n(%d) Exit%n", maxValid + 1);
            System.out.print("Choice: ");
            try {
                // Parse this way to avoid infinite loops
                int choice = Integer.parseInt(input.next());
                // All prompts should include the option to exit
                if (choice == maxValid + 1) System.exit(0);
                // If the selection is valid
                if (0 <= choice && choice <= maxValid) return choice;
            } catch (NumberFormatException ignored) {
                // If the input was not a number at all, keep looping
            }
        }
    }
    */

    /**
     * Clears the terminal
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Pauses the app until the user presses the enter key
     * Obsolete code only for CLI
     */
    /*public static void promptEnter() {
        System.out.println("Press Enter to continue");
        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (Exception ignored) {
        }
    }*/

    /**
     * Prompts the user to enter their username and password.
     * Will loop until valid details are entered.
     *
     * @param login The login object to use
     */
    public static User promptLogin(Login login) {
        String user;
        clearScreen();
        Console console = System.console();
        while (true) {
            LoginPage loginPage = new LoginPage();
            String[] credentials = loginPage.display();
            String user=credentials[0];
            String password=credentials[1];
            //clearScreen();
            //user = console.readLine("Enter username: ");
            //var password = new String(console.readPassword("Enter password: "));
            if (login.checkCredentials(user, password)) break;
        }
        //System.out.println("Welcome back to Quizzer!");
       // promptEnter();
        return new User(user);
    }

    /**
     * Prompts the user to create a new account.
     * Will loop until it succeeds.
     *
     * @param login The login object to use
     */
    public static User promptCreateUser(Login login) {
        clearScreen();
        // Loop while the passwords don't match or the username is already taken
        String name;
        String password;
        Console console = System.console();
        while (true) {
            String[] accountDetails = createAccountDialog.display();
            String user=accountDetails[0];
            String pass1=accountDetails[1];
            String pass2=accountDetails[2];
            //String username = console.readLine("Enter your new username: ");
            //char[] pass1 = console.readPassword("Enter your new password: ");
            //char[] pass2 = console.readPassword("Re-enter your new password: ");
            //Commented out the parts of the code that use CLI
            // Assert the passwords are equal
            if (Arrays.equals(pass1, pass2)) {
                name = username;
                password = new String(pass1);
                // Loop if the user could not be created
                if (!login.createUser(name, password)) continue;
                break;
            }
            //clearScreen();
            //System.out.println("Passwords must be the same.");
        }
       // System.out.println("Success! Welcome to Quizzer!");
        //promptEnter();
        return new User(name);
    }

    public static void showStats(User user) {
        clearScreen();
        System.out.printf("Total Answered: %.0f%n", user.getStatistic(Statistic.TOTAL_ANSWERED));
        System.out.printf("Total Correct: %.0f%n", user.getStatistic(Statistic.TOTAL_CORRECT));
        System.out.printf("Mean: %.2f%n", user.getStatistic(Statistic.MEAN));
        System.out.printf("Median: %.2f%n", user.getStatistic(Statistic.MEDIAN));
        System.out.printf("StdDev: %.2f%n", User.stdDev());
        promptEnter();
    }
}
