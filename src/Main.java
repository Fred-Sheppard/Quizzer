import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearScreen();
            System.out.println("Thanks for playing!");
        }));
        clearScreen();
        System.out.println("""
                  ____  _    _ _____ __________________ _____
                /  __ \\| |  | |_   _|___  /___  /  ____|  __ \\
                | |  | | |  | | | |    / /   / /| |__  | |__) |
                | |  | | |  | | | |   / /   / / |  __| |  _  /
                | |__| | |__| |_| |_ / /__ / /__| |____| | \\ \\
                 \\___\\_\\\\____/|_____/_____/_____|______|_|  \\_\\""");
        System.out.println("Welcome to Quizzer!");
        promptEnter();
        QuestionLoader loader = new QuestionLoader(new File("/home/fred/Code/Java/Quizzer/res/questions"));
        Scanner scanner = new Scanner(System.in);
        String[] topics = loader.listTopics();

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
        int choice = awaitInput(scanner, options, builder.toString());
        if (choice == options) System.exit(0);
        return choice;
    }

    /**
     * Repeatedly prompts the user for a valid numerical input.
     *
     * @param input    Scanner object to receive input
     * @param maxValid The largest number input that is valid
     * @param prompt   Prompt to display to the user displaying valid options
     */
    public static int awaitInput(Scanner input, int maxValid, String prompt) {
        while (true) {
            System.out.println(prompt);
            System.out.print("Choice: ");
            try {
                // Parse this way to avoid infinite loops
                int choice = Integer.parseInt(input.next());
                // If the selection was not a valid choice
                if (0 <= choice && choice <= maxValid) return choice;
            } catch (NumberFormatException ignored) {
            }
            clearScreen();
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void promptEnter() {
        System.out.println("Press Enter to continue");
        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (Exception ignored) {
        }
    }
}