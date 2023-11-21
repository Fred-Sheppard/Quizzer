import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class CLI implements UI {

    /**
     * ASCII font of the Quizzer logo
     */
    private static final String QUIZZER = """
              ____  _    _ _____ __________________ _____
            /  __ \\| |  | |_   _|___  /___  /  ____|  __ \\
            | |  | | |  | | | |    / /   / /| |__  | |__) |
            | |  | | |  | | | |   / /   / / |  __| |  _  /
            | |__| | |__| |_| |_ / /__ / /__| |____| | \\ \\
             \\___\\_\\\\____/|_____/_____/_____|______|_|  \\_\\""";
    private String PATH;
    /**
     * Scanner to be used to take in user input
     */
    private Scanner scanner;

    public CLI() {
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new CLI().run();
    }

    /**
     * Run the CLI application
     */
    public void run() {
        // Print message when the program closes, even unexpectedly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearScreen();
            System.out.println("Thanks for playing!");
            System.out.println(QUIZZER);
        }));

        PATH = getPath();

        clearScreen();
        // Welcome messages
        System.out.println(QUIZZER);
        System.out.println("Welcome to Quizzer!");
        promptEnter();
        QuestionLoader loader = new QuestionLoader(new File(PATH + "questions/"));
        scanner = new Scanner(System.in);
        String[] topics = loader.listTopics();

        // Login
        User user = login();

        // Continuously ask questions
        //noinspection InfiniteLoopStatement
        while (true) {
            int topic = chooseTopic(topics);
            // For n topics, choosing n+1 will display user stats
            if (topic == topics.length) {
                showStats(user);
                continue;
            }
            Quiz quiz = chooseQuiz(topics[topic], user, loader);
            playQuiz(quiz);
        }
    }

    private String getPath() {
        String path;
        File jarPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        if (jarPath.toString().endsWith(".jar")) {
            // If being run from a jar file
            path = jarPath.getParentFile().getAbsolutePath() + "/";
        } else {
            // Run from inside IDE
            path = "";
        }
        return path;
    }

    private Quiz chooseQuiz(String topic, User user, QuestionLoader loader) {
        int mode = promptInput(2, """
                Choose a gamemode:
                (0) Random
                (1) Escalation
                (2) Redemption""");
        final int RANDOM = 0;
        final int ESCALATION = 1;
        final int REDEMPTION = 2;
        return switch (mode) {
            case RANDOM -> new RandomQuiz(topic, user, loader, this);
            case ESCALATION -> new EscalationQuiz(topic, user, loader, this);
            case REDEMPTION -> new RedemptionQuiz(topic, user, loader, this);
            default -> null; //unreachable, since promptInput does not allow invalid inputs
        };
    }

    private void playQuiz(Quiz quiz) {
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

    private User login() {
        Login login = new Login(new File(PATH + "GameData/users.txt"));
        User user;
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
            // Ask the user if they they are a new or existing user
            String loginPrompt = """
                    Are you a new or existing user?
                    (0) New
                    (1) Existing""";
            boolean isExistingUser = promptInput(1, loginPrompt) == 1;
            if (isExistingUser) {
                user = promptLogin(login);
            } else {
                user = promptCreateUser(login);
            }
        }
        return user;
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
     * @param maxValid The largest numerical input that is valid
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
        // Keep looping until valid credentials are entered
        while (true) {
            clearScreen();
            user = console.readLine("Enter username: ");
            var password = new String(console.readPassword("Enter password: "));
            try {
                login.checkCredentials(user, password);
            } catch (Login.UserNotFoundError e) {
                System.out.println("Username not found");
                promptEnter();
                continue;
            } catch (Login.IncorrectPasswordError e) {
                System.out.println("Incorrect password for the given username");
                promptEnter();
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
        // Keep looping until a valid username (one that doesn't exist) is provided
        while (true) {
            clearScreen();
            String username = console.readLine("Enter your new username: ");
            if (login.userExists(username)) {
                System.out.println("User already exists");
                promptEnter();
                continue;
            }
            name = username;
            password = getPassword(console);
            // If the passwords were not the same
            if (password == null) continue;
            login.createUser(username, password);
            break;
        }
        System.out.println("Success! Welcome to Quizzer!");
        promptEnter();
        return new User(name);
    }

    /**
     * Prompts the user to enter a password twice, asserting they are equal.
     *
     * @param console The console object to read the password from
     * @return The
     */
    private String getPassword(Console console) {
        char[] pass1 = console.readPassword("Enter your new password: ");
        char[] pass2 = console.readPassword("Re-enter your new password: ");
        // Assert the passwords are equal
        if (!Arrays.equals(pass1, pass2)) {
            clearScreen();
            System.out.println("Passwords must be the same.");
            promptEnter();
            return null;
        }
        return new String(pass1);
    }

    /**
     * Show the statistics for the current user, along with the leaderboard of all users.
     *
     * @param user The user to display stats for
     */
    public void showStats(User user) {
        clearScreen();
        System.out.printf("User: %s%n%n", user);
        System.out.printf("Answered:\t%.0f%n", user.getStatistic(Statistic.TOTAL_ANSWERED));
        System.out.printf("Correct:\t%.0f%n", user.getStatistic(Statistic.TOTAL_CORRECT));
        System.out.printf("Mean:\t\t%.2f%n", user.getStatistic(Statistic.MEAN));
        System.out.printf("Median:\t\t%.2f%n", user.getStatistic(Statistic.MEDIAN));
        System.out.printf("StdDev:\t\t%.2f%n", User.stdDev());
        System.out.println();
        System.out.println(User.leaderboard());
        promptEnter();
    }

    /**
     * Pose the given question to the user for answering.
     *
     * @param question The question to be asked
     * @return If the user answered the question correctly
     */
    public boolean askQuestion(Question question) {
        clearScreen();
        // We don't each question to appear with the same order each time
        question.shufflePossibilities();
        StringBuilder builder = new StringBuilder(question.question());
        builder.append("\n");
        /*
         Print in the following format:
         (0) Who invented...
         (1) What is...
        */
        // The number of available options for the user to select between
        for (int i = 0; i < question.possibilities().size(); i++) {
            String thisQuestion = question.possibilities().get(i);
            builder.append(String.format("(%d) %s%n", i, thisQuestion));
        }
        // Tell the user to select their answer
        int choice = promptInput(3, builder.toString());
        if (choice == question.correctIndex()) {
            System.out.println("Correct! Well done.");
            promptEnter();
            return true;
        } else {
            System.out.println("Sorry. The correct answer was " + question.correctIndex());
            promptEnter();
            return false;
        }
    }
}