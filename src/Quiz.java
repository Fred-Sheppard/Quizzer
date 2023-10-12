import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Quiz {

    private final String topic;
    private final ArrayList<Question> questions;
    private final Scanner scanner;

    public Quiz(String topic, QuestionLoader loader, Scanner scanner) {
        this.topic = topic;
        questions = loader.getEntries(topic);
        this.scanner = scanner;
    }

    public boolean askQuestion(Question question) {
        Main.clearScreen();
        var possibilities = question.possibilities();
        Collections.shuffle(possibilities);
        int answer = possibilities.indexOf(question.answer());
        StringBuilder builder = new StringBuilder(question.question());
        builder.append("\n");
        for (int i = 0; i < 4; i++) {
            builder.append(String.format("(%d) %s%n", i, possibilities.get(i)));
        }
        int choice = Main.awaitInput(scanner, 3, builder.toString());
        if (choice == answer) {
            System.out.println("Correct! Well done.");
            return true;
        } else {
            System.out.println("Sorry. The correct answer was " + answer);
            return false;
        }
    }

    public void askQuestions() {
        Main.clearScreen();
        int numQuestions = questions.size();
        System.out.println("You have selected the " + topic + " topic.");
        System.out.printf("This topic contains %d questions.%n", numQuestions);
        Main.promptEnter();
        int correct = 0;
        for (Question q : questions) {
            boolean userCorrect = askQuestion(q);
            if (userCorrect) correct++;
            Main.promptEnter();
        }
        Main.clearScreen();
        System.out.printf("Quiz complete! You got %d out of %d questions correct! (%.0f%%)%n",
                correct, numQuestions, (float) correct / (float) numQuestions * 100.0);
        Main.promptEnter();
    }
}
