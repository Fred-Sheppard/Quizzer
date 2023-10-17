import java.io.*;
import java.util.*;

/**
 * Class representing a quiz.
 * <p>
 * A quiz is a set of questions asked in a specific order.
 * This order could be random, based on difficulty,
 * or on how successfully the user has answered each question in the past.
 */
public class Quiz {

    private final String topic;
    private final ArrayList<Question> questions;
    private final Scanner scanner;
    HashMap<String, Integer> userHistory;
    private final File userHistoryFile;

    public Quiz(String topic, String user, QuestionLoader loader, Scanner scanner) {
        this.topic = topic;
        questions = loader.getEntries(topic);
        this.scanner = scanner;
        userHistoryFile = new File("GameData/UserHistory/" + user + ".txt");
        BufferedReader reader;
        try {
            if (!userHistoryFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                userHistoryFile.createNewFile();
            }
            reader = new BufferedReader(new FileReader(userHistoryFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userHistory = new HashMap<>();
        reader.lines().forEach(line -> {
            String[] split = line.split("\\|");
            userHistory.put(split[0], Integer.parseInt(split[1]));
        });
    }

    /**
     * Asks a single question, and verifies if the user entered the correct answer.
     *
     * @param question The question to be asked
     * @return If the user selected the correct answer
     */
    private boolean askQuestion(Question question) {
        Main.clearScreen();
        var possibilities = question.possibilities();
        Collections.shuffle(possibilities);
        int answer = possibilities.indexOf(question.answer());
        StringBuilder builder = new StringBuilder(question.question());
        builder.append("\n");
        for (int i = 0; i < 4; i++) {
            builder.append(String.format("(%d) %s%n", i, possibilities.get(i)));
        }
        int choice = Main.promptInput(scanner, 3, builder.toString());
        if (choice == answer) {
            System.out.println("Correct! Well done.");
            return true;
        } else {
            System.out.println("Sorry. The correct answer was " + answer);
            userHistory.merge(question.question(), 1, Integer::sum);
            return false;
        }
    }

    /**
     * Asks all the questions in the given list.
     * <p>
     * This should not be called by outside consumers,
     * who should instead call a helper method that specifies the order to ask the questions in.
     *
     * @param questions The list of questions to be asked
     */
    private void askQuestions(ArrayList<Question> questions) {
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
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(userHistoryFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userHistory.forEach((k, v) -> writer.println(k + "|" + v));
        writer.flush();
        writer.close();
        Main.promptEnter();
    }

    /**
     * Sorts and asks the questions, placing the user's worst-answered questions first.
     */
    public void redemptionQuestions() {
        // Sort by the values in the map
        questions.sort((a, b) -> userHistory.getOrDefault(b.question(), 0)
                .compareTo(userHistory.getOrDefault(a.question(), 0)));
        askQuestions(questions);
    }

    /**
     * Asks the questions in a random order.
     */
    public void randomOrderQuestions() {
        Collections.shuffle(questions);
        askQuestions(questions);
    }

    /**
     * Asks the questions in order of difficulty.
     */
    public void sortedQuestions() {
        // Sort the questions by difficulty
        questions.sort(Comparator.comparing(Question::difficulty));
        askQuestions(questions);
    }
}
