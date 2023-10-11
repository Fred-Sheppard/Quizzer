import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearScreen();
            System.out.println("\nThanks for playing!");
        }));
        clearScreen();
        QuestionLoader loader = new QuestionLoader(new File("/home/fred/Code/Java/Quizzer/test/questions/"));
        String[] topics = loader.listTopics();

        StringBuilder builder = new StringBuilder("Welcome to Quizzer! Select a topic to begin:\n");
        for (int i = 0; i < topics.length; i++) {
            builder.append(String.format("(%d) %s%n", i, topics[i]));
        }

        Scanner input = new Scanner(System.in);
        int choice = awaitInput(input, 2, builder.toString());
        Quiz quiz = new Quiz(topics[choice], loader, input);
        quiz.askQuestions();
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
}