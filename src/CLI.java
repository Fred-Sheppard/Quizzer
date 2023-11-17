import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class CLI implements UI {

    /**
     * ASCII font of the Quizzer logo
     */
    private final String QUIZZER = """
              ____  _    _ _____ __________________ _____
            /  __ \\| |  | |_   _|___  /___  /  ____|  __ \\
            | |  | | |  | | | |    / /   / /| |__  | |__) |
            | |  | | |  | | | |   / /   / / |  __| |  _  /
            | |__| | |__| |_| |_ / /__ / /__| |____| | \\ \\
             \\___\\_\\\\____/|_____/_____/_____|______|_|  \\_\\""";
    private Scanner scanner;

    public CLI() {
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new CLI().run();
    }

    public void run() {
        // TODO use from jar file, maybe encryption library?
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
        QuestionLoader loader = new QuestionLoader(new File("res/questions/"));
        scanner = new Scanner(System.in);
        String[] topics = loader.listTopics();

        // Login
        Login login = new Login(new File("GameData/users.txt"));
        User user;
        String loginPrompt = """
                Are you a new or existing user?
                (0) New
                (1) Existing""";
        Console console = System.console();
        // If the program is run from within an IDE
        if (console == null) {
            System.out.println("""
                    You are running Quizzer from with an IDE.
                    Debugging mode enabled.
                    If you want the full experience, run Quizzer from a terminal.""");
            promptEnter();
            user = new User("IDE");
        } else {
            boolean isExistingUser = promptInput(1, loginPrompt) == 1;
            if (isExistingUser) {
                user = promptLogin(login);
            } else {
                user = promptCreateUser(login);
            }
        }

        // Continuously ask questions
        //noinspection InfiniteLoopStatement
        while (true) {
            int choice = chooseTopic(topics);
            if (choice == topics.length) {
                showStats(user);
                continue;
            }
            int mode = promptInput(2, """
                    Choose a gamemode:
                    (0) Random
                    (1) Escalation
                    (2) Redemption""");
            final int RANDOM = 0;
            final int ESCALATION = 1;
            final int REDEMPTION = 2;
            Quiz quiz = switch (mode) {
                case RANDOM -> new RandomQuiz(topics[choice], user, loader, this);
                case ESCALATION -> new EscalationQuiz(topics[choice], user, loader, this);
                case REDEMPTION -> new RedemptionQuiz(topics[choice], user, loader, this);
                default -> null; //unreachable, since promptInput does not allow invalid inputs
            };
            // Todo fix this
            assert quiz != null;
            clearScreen();
            int numQuestions = quiz.questions.size();
            System.out.printf("You have selected the %s topic.%n", quiz.topic());
            System.out.printf("This topic contains %d questions.%n", numQuestions);
            promptEnter();
            int correct = quiz.askQuestions();
            double percentage = (double) correct / (double) numQuestions;
            System.out.printf("Quiz complete! You got %d out of %d questions correct! (%.0f%%)%n",
                    correct, numQuestions, percentage);
        }
    }

    /**
     * Prompts the user to select a topic to be quizzed on
     *
     * @param topics The list of available topics
     * @return The user's choice
     */
    public int chooseTopic(String[] topics) {
        int options = topics.length;
        StringBuilder builder = new StringBuilder("Select a topic to begin:\n");
        for (int i = 0; i < options; i++) {
            builder.append(String.format("(%d) %s%n", i, topics[i]));
        }
        builder.append(String.format("%n(%d) %s", options, "Show stats"));
        return promptInput(options, builder.toString());
    }

    /**
     * Repeatedly prompts the user for a valid numerical input.
     * Valid inputs include any integer from 0 to `maxValid`, inclusive.
     *
     * @param maxValid The largest number input that is valid
     * @param prompt   Prompt to display to the user displaying valid options
     */
    public int promptInput(int maxValid, String prompt) {
        while (true) {
            clearScreen();
            System.out.println(prompt);
            System.out.printf("%n(%d) Exit%n", maxValid + 1);
            System.out.print("Choice: ");
            try {
                // Parse this way to avoid infinite loops
                int choice = Integer.parseInt(scanner.next());
                // All prompts should include the option to exit
                if (choice == maxValid + 1) System.exit(0);
                // If the selection is valid
                if (0 <= choice && choice <= maxValid) return choice;
            } catch (NumberFormatException ignored) {
                // If the input was not a number at all, keep looping
            }
        }
    }

    /**
     * Clears the terminal
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Pauses the app until the user presses the enter key
     */
    public void promptEnter() {
        System.out.println("Press Enter to continue");
        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    /**
     * Prompts the user to enter their username and password.
     * Will loop until valid details are entered.
     *
     * @param login The login object to use
     */
    public User promptLogin(Login login) {
        String user;
        clearScreen();
        Console console = System.console();
        while (true) {
            clearScreen();
            user = console.readLine("Enter username: ");
            var password = new String(console.readPassword("Enter password: "));
            try {
                login.checkCredentials(user, password);
            } catch (Login.UserNotFoundError e) {
                System.out.println("Username not found");
                continue;
            } catch (Login.IncorrectPasswordError e) {
                System.out.println("Incorrect password for the given username");
                continue;
            }
            // If no errors, then the credentials were okay
            break;
        }
        System.out.println("Welcome back to Quizzer!");
        promptEnter();
        return new User(user);
    }

    /**
     * Prompts the user to create a new account.
     * Will loop until it succeeds.
     *
     * @param login The login object to use
     */
    public User promptCreateUser(Login login) {
        clearScreen();
        // Loop while the passwords don't match or the username is already taken
        String name;
        String password;
        Console console = System.console();
        while (true) {
            String username = console.readLine("Enter your new username: ");
            char[] pass1 = console.readPassword("Enter your new password: ");
            char[] pass2 = console.readPassword("Re-enter your new password: ");
            // Assert the passwords are equal
            if (Arrays.equals(pass1, pass2)) {
                name = username;
                password = new String(pass1);
                // Loop if the user could not be created
                if (login.createUser(name, password)) {
                    break;
                }
                System.out.println("User already exists");
                promptEnter();
            }
            clearScreen();
            System.out.println("Passwords must be the same.");
        }
        System.out.println("Success! Welcome to Quizzer!");
        promptEnter();
        return new User(name);
    }

    public void showStats(User user) {
        clearScreen();
        System.out.printf("Total Answered: %.0f%n", user.getStatistic(Statistic.TOTAL_ANSWERED));
        System.out.printf("Total Correct: %.0f%n", user.getStatistic(Statistic.TOTAL_CORRECT));
        System.out.printf("Mean: %.2f%n", user.getStatistic(Statistic.MEAN));
        System.out.printf("Median: %.2f%n", user.getStatistic(Statistic.MEDIAN));
        System.out.printf("StdDev: %.2f%n", User.stdDev());
        System.out.println();
        System.out.println(User.leaderboard());
        promptEnter();
    }

    public boolean askQuestion(Question question, int answer) {
        clearScreen();
        StringBuilder builder = new StringBuilder(question.question());
        builder.append("\n");
        // The number of available options for the user to select between
        for (int i = 0; i < question.possibilities().size(); i++) {
            builder.append(String.format("(%d) %s%n", i, question.possibilities().get(i)));
        }
        // Tell the user to select their answer
        int choice = promptInput(3, builder.toString());
        if (choice == answer) {
            System.out.println("Correct! Well done.");
            promptEnter();
            return true;
        } else {
            System.out.println("Sorry. The correct answer was " + answer);
            promptEnter();
            return false;
        }
    }
}