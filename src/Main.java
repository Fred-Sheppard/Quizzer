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
        // TODO Discuss how the users file will be laid out,
        //  as this will influence how the 3rd quiz mode is implemented

        // Print message when the program closes, even unexpectedly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearScreen();
            System.out.println("Thanks for playing!");
            System.out.println(QUIZZER);
        }));

        clearScreen();
        // Welcome messages
        System.out.println(QUIZZER);
        System.out.println("Welcome to Quizzer!");
        promptEnter();
        QuestionLoader loader = new QuestionLoader(new File("/home/fred/Code/Java/Quizzer/res/questions"));
        Scanner scanner = new Scanner(System.in);
        String[] topics = loader.listTopics();


        // Login
        Login login = new Login(new File("GameData/users.txt"));
        clearScreen();
        String loginPrompt = """
                Are you a new or existing user?
                (0) New
                (1) Existing""";
        boolean isExistingUser = promptInput(scanner, 1, loginPrompt) == 1;
        if (isExistingUser) {
            promptLogin(login);
        } else {
            promptCreateUser(login);
        }

        // Continuously ask questions
        //noinspection InfiniteLoopStatement
        while (true) {
            int choice = chooseTopic(topics, scanner);
            Quiz quiz = new Quiz(topics[choice], loader, scanner);
            quiz.randomOrderQuestions();
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
        clearScreen();
        int options = topics.length;
        StringBuilder builder = new StringBuilder("Select a topic to begin:\n");
        for (int i = 0; i < options; i++) {
            builder.append(String.format("(%d) %s%n", i, topics[i]));
        }
        builder.append(String.format("%n(%d) %s%n", options, "Exit"));
        int choice = promptInput(scanner, options, builder.toString());
        if (choice == options) System.exit(0);
        return choice;
    }

    /**
     * Repeatedly prompts the user for a valid numerical input.
     * Valid inputs include any integer from 0 to `maxValid`, inclusive.
     *
     * @param input    Scanner object to receive input
     * @param maxValid The largest number input that is valid
     * @param prompt   Prompt to display to the user displaying valid options
     */
    public static int promptInput(Scanner input, int maxValid, String prompt) {
        while (true) {
            System.out.println(prompt);
            System.out.print("Choice: ");
            try {
                // Parse this way to avoid infinite loops
                int choice = Integer.parseInt(input.next());
                // If the selection is valid
                if (0 <= choice && choice <= maxValid) return choice;
            } catch (NumberFormatException ignored) {
                // If the input was not a number at all, keep looping
            }
            clearScreen();
        }
    }

    /**
     * Clears the terminal
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Pauses the app until the user presses the enter key
     */
    public static void promptEnter() {
        System.out.println("Press Enter to continue");
        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    public static void promptLogin(Login login) {
        clearScreen();
        Console console = System.console();
        while (true) {
            clearScreen();
            String user = console.readLine("Enter username: ");
            var password = new String(console.readPassword("Enter password: "));
            if (login.checkLogin(user, password)) break;
        }
        System.out.println("Welcome back to Quizzer!");
        promptEnter();
    }

    public static void promptCreateUser(Login login) {
        clearScreen();
        // Loop while the passwords don't match or the username is already taken
        String name;
        String password;
        Console console = System.console();
        while (true) {
            String username = console.readLine("Enter your new username: ");
            char[] pass1 = console.readPassword("Enter your new password: ");
            char[] pass2 = console.readPassword("Re-enter your new password: ");
            if (Arrays.equals(pass1, pass2)) {
                name = username;
                password = new String(pass1);
                // Loop if the user could not be created
                if (!login.createUser(name, password)) continue;
                break;
            }
            clearScreen();
            System.out.println("Passwords must be the same.");
        }
        System.out.println("Success! Welcome to Quizzer!");
        promptEnter();
    }
}