package src;

import java.io.Console;
import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Print message when the program closes, even unexpectedly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearScreen();
            System.out.println("Thanks for playing!");
        }));

        clearScreen();
        // Welcome messages
        QuestionLoader loader = new QuestionLoader(new File("res/questions/"));
        String[] topics = loader.listTopics();
        // Login
        Login login = new Login(new File("GameData/users.txt"));
        String loginPrompt = "Are you a new or existing user?\n(0) New\n(1) Existing";
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
            int choice = chooseTopic(topics);
            if (choice == topics.length) {
                PromptInput Prompt = new PromptInput();
                int response = Prompt.display("Choose one to display:\n(0) Leaderboard\n(1) Statistics", 1);
                if (response == 0) {
                    String leaderboard = User.leaderboard();
                    DisplayTextWindow.launchWindow(leaderboard);
                    continue;
                } else if (response == 1) {
                    showStats(user);
                    continue;
                }
            }
            Quiz quiz = new Quiz(topics[choice], user, loader);
            PromptInput Prompt = new PromptInput();
            int mode = Prompt.display("Choose a gamemode:\n(0) Random\n(1) Escalation\n(2) Redemption", 2);
            switch (mode) {
                case 0:
                    quiz.askRandom();
                    break;
                case 1:
                    quiz.askEscalation();
                    break;
                case 2:
                    quiz.askRedemption();
                    break;
            }
        }
    }

    /**
     * Prompts the user to select a topic to be quizzed on
     *
     * @param topics The list of available topics
     * @return The user's choice
     */
    public static int chooseTopic(String[] topics) {
        int options = topics.length;
        StringBuilder builder = new StringBuilder("Select a topic to begin:\n");
        for (int i = 0; i < options; i++) {
            builder.append(String.format("(%d) %s%n", i, topics[i]));
        }
        builder.append(String.format("%n(%d) %s", options, "Show Stats/Leaderboard"));
        PromptInput prompt = new PromptInput();
        return prompt.display(builder.toString(), options);
    }

    /**
     * Clears the terminal
     */
    public static void clearScreen() {
        System.out.flush();
    }

    /**
     * Prompts the user to enter their username and password.
     * Will loop until valid details are entered.
     *
     * @param login The login object to use
     */
    public static User promptLogin(Login login) {
        String user;
        clearScreen();
        while (true) {
            promptLogin loginPage = new promptLogin();
            String[] credentials = loginPage.display();
            user = credentials[0];
            String password = credentials[1];
            if (login.checkCredentials(user, password)) break;
        }
        DisplayTextWindow.launchWindow("Login Success!");
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
        while (true) {
            CreateAccount ca = new CreateAccount();
            String[] accountDetails = ca.display();
            String username = accountDetails[0];
            char[] pass1 = accountDetails[1].toCharArray();
            char[] pass2 = accountDetails[2].toCharArray();
            // Assert the passwords are equal
            if (Arrays.equals(pass1, pass2)) {
                name = username;
                password = new String(pass1);
                // Loop if the user could not be created
                if (!login.createUser(name, password)) continue;
                break;
            }
            DisplayTextWindow.launchWindow("Passwords must be the same.");
        }
        DisplayTextWindow.launchWindow("Success! Welcome to Quizzer!");
        return new User(name);
    }

    public static void showStats(User user) {
        double tot_ans = user.getStatistic(Statistic.TOTAL_ANSWERED);
        double tot_cor = user.getStatistic(Statistic.TOTAL_CORRECT);
        double avg = user.getStatistic(Statistic.MEAN);
        double median = user.getStatistic(Statistic.MEDIAN);
        double std_dev = User.stdDev();
        String res = "Total Answered: " + tot_ans + "\nTotal Correct: " + tot_cor + "\nMean: " + avg + "\nMedian: " + median + "\nStandard Deviation: " + std_dev + "\n";
        DisplayTextWindow.launchWindow(res);
    }
}
