import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        QuestionLoader loader = new QuestionLoader(new File("test/questions/"));
        String[] topics = loader.listTopics();
        System.out.println("Topics:");
        for (String s : topics) {
            System.out.println(s);
        }
        System.out.println();
        var answers = loader.getEntries("CS");
        for (Answer a : answers) {
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