import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {

    private final ArrayList<Question> questions;
    private final Scanner scanner;

    public Quiz(String topic, QuestionLoader loader, Scanner scanner) {
        questions = loader.getEntries(topic);
        this.scanner = scanner;
    }

    public void askQuestion(Question question) {
        Main.clearScreen();
        var possibilities = question.possibilities();
        StringBuilder builder = new StringBuilder(question.question());
        builder.append("\n");
        for (int i = 0; i < 4; i++) {
            builder.append(String.format("(%d) %s%n", i, possibilities.get(i)));
        }
        int choice = Main.awaitInput(scanner, 3, builder.toString());
        if (choice == 3) {
            System.out.println("Correct! Well done.");
        } else {
            System.out.println("Sorry. The correct answer was (3).");
        }
    }

    public void askQuestions() {
        for (Question q : questions) {
            askQuestion(q);
            System.out.println("Press Enter to continue");
            try {
                //noinspection ResultOfMethodCallIgnored
                System.in.read();
            } catch (Exception ignored) {
            }
        }
    }
}
