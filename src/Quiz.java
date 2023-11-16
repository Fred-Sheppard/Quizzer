import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Class representing a quiz.
 * <p>
 * A quiz is a set of questions asked in a specific order.
 * This order could be random, based on difficulty,
 * or on how successfully the user has answered each question in the past.
 */
public class Quiz {

    /**
     * The topic of the quiz e.g. Discrete Maths, Comp Org etc.
     */
    private final String topic;
    /**
     * List of questions to be asked in a quiz.
     * This can be sorted to provide a unique quiz mode.
     */
    private final ArrayList<Question> questions;
    /**
     * Scanner to take user input.
     */
    private final Scanner scanner;
    /**
     * The user taking the quiz.
     * Quiz results will be written to this user's statistics.
     */
    private final User user;

    /**
     * Constructor for the Quiz class.
     *
     * @param topic   The topic of the quiz.
     * @param user    The user taking the quiz.
     * @param loader  The question loader to load questions for the specified topic.
     * @param scanner Scanner for user input.
     */
    public Quiz(String topic, User user, QuestionLoader loader, Scanner scanner) {
        this.topic = topic;
        this.user = user;
        // Load the questions from a file
        questions = loader.getEntries(topic);
        this.scanner = scanner;
    }

    /**
     * Asks a single question, and verifies if the user entered the correct answer.
     *
     * @param question The question to be asked
     * @return If the user selected the correct answer
     */
    private boolean askQuestion(Question question) {
        Main.clearScreen();
        // All wrong answers, plus the correct answer
        var possibilities = question.possibilities();
        // Each time a question is asked, the order should be unique
        Collections.shuffle(possibilities);
        // Find which answer (0-3) is the correct one
        // This will be used later to check if they chose correctly
        int answer = possibilities.indexOf(question.answer());
        StringBuilder builder = new StringBuilder(question.question());
        builder.append("\n");
        for (int i = 0; i < 4; i++) {
            builder.append(String.format("(%d) %s%n", i, possibilities.get(i)));
        }
        // Tell the user to select their answer
        int choice = Main.promptInput(3, builder.toString(), scanner);
        if (choice == answer) {
            System.out.println("Correct! Well done.");
            // If the question has never been answered by this user, it will not exist in the user history
            // If this is the case, place a zero
            user.getHistory().putIfAbsent(question.question(), 0);
            return true;
        } else {
            System.out.println("Sorry. The correct answer was " + answer);
            // Increment the value in the map by 1
            user.getHistory().merge(question.question(), 1, Integer::sum);
            return false;
        }
    }

    /**
     * Asks all the questions in the given list.
     * <p>
     * This cannot be called by outside consumers,
     * who should instead call a helper method that specifies the order to ask the questions in.
     *
     * @param questions The list of questions to be asked
     */
    private void askQuestions(ArrayList<Question> questions) {
        Main.clearScreen();
        int numQuestions = questions.size();
        System.out.printf("You have selected the %s topic.%n", topic);
        System.out.printf("This topic contains %d questions.%n", numQuestions);
        Main.promptEnter();
        // Ask all the questions in the sorted/shuffled list,
        // keeping track of the amount answered correctly
        int correct = 0;
        for (Question q : questions) {
            boolean userCorrect = askQuestion(q);
            if (userCorrect) correct++;
            Main.promptEnter();
        }
        Main.clearScreen();
        System.out.printf("Quiz complete! You got %d out of %d questions correct! (%.0f%%)%n",
                correct, numQuestions, (float) correct / (float) numQuestions * 100.0);
        user.history.merge("Rounds", 1, Integer::sum);
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(user.historyFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Main.promptEnter();
    }

    /**
     * Sorts and asks the questions, placing the user's worst-answered questions first.
     */
    public void askRedemption() {
        // Sort by the values in the map
        questions.sort((a, b) -> user.history.getOrDefault(b.question(), 0)
                .compareTo(user.history.getOrDefault(a.question(), 0)));
        askQuestions(questions);
    }

    /**
     * Asks the questions in a random order.
     */
    public void askRandom() {
        Collections.shuffle(questions);
        askQuestions(questions);
    }

    /**
     * Asks the questions in order of difficulty.
     */
    public void askEscalation() {
        // Sort the questions by difficulty
        questions.sort(Comparator.comparing(Question::difficulty));
        askQuestions(questions);
    }

}