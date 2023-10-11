import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        QuestionLoader loader = new QuestionLoader(new File("/home/fred/Code/Java/Quizzer/test/questions/"));
        Scanner input = new Scanner(System.in);
        String[] topics = loader.listTopics();
        ArrayList<String> options = new ArrayList<>();
        options.add("Welcome to Quizzer! Select a topic to begin:");
        for (int i = 0; i < topics.length; i++) {
            options.add(String.format("(%d) %s", i, topics[i]));
        }
        int choice = awaitInput(input, new int[]{0, 1, 2}, options);
        System.out.printf("Choice: (%d) %s%n", choice, topics[choice]);
        var answers = loader.getEntries(topics[choice]);
        for (var a : answers) {
            System.out.println(a);
        }
    }

    /**
     * Repeatedly prompts the user for a valid numerical input.
     *
     * @param input   Scanner object to receive input
     * @param options List of valid integers
     * @param prompts Prompts to display to the user displaying valid options
     */
    public static int awaitInput(Scanner input, int[] options, ArrayList<String> prompts) {
        while (true) {
            for (String s : prompts) {
                System.out.println(s);
            }
            System.out.print("Choice: ");
            try {
                String choice = input.next();
                int i = Integer.parseInt(choice);
                // If the selection was not a valid choice
                if (Arrays.stream(options).filter(x -> x == i).findAny().isEmpty()) continue;
                return i;
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