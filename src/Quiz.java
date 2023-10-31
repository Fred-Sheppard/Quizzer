package src;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private final User user;

    public Quiz(String topic, User user, QuestionLoader loader) {
        this.topic = topic;
        this.user = user;
        questions = loader.getEntries(topic);
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
        PromptInput prompt = new PromptInput();
        int choice = prompt.display(builder.toString(), 3);
        if (choice == answer) {
            DisplayTextWindow.launchWindow("Correct! Well done.");
            return true;
        } else {
            DisplayTextWindow.launchWindow("Sorry. The correct answer was " + answer);
            user.history.merge(question.question(), 1, Integer::sum);
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
        DisplayTextWindow.launchWindow("You have selected the " + topic + " topic.");
        DisplayTextWindow.launchWindow("This topic contains" + numQuestions + "questions.\n");
        int correct = 0;
        for (Question q : questions) {
            boolean userCorrect = askQuestion(q);
            if (userCorrect) correct++;
        }
        Main.clearScreen();
        DisplayTextWindow.launchWindow("Quiz complete! You got " + correct + " out of" + numQuestions + " questions correct!\n Your percentage is " + ((float) correct / (float) numQuestions * 100.0) + "%");
        //correct, numQuestions, ((float) correct / (float) numQuestions * 100.0);
        user.history.merge("Rounds", 1, Integer::sum);
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(user.historyFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.history.forEach((k, v) -> writer.println(k + "|" + v));
        writer.flush();
        writer.close();
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