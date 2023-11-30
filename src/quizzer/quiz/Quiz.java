package quizzer.quiz;

import quizzer.Question;
import quizzer.QuestionLoader;
import quizzer.UI;
import quizzer.User;

import java.util.ArrayList;

/**
 * Class representing a quiz.
 * <p>
 * A quiz is a set of questions asked in a specific order.
 * This order could be random, based on difficulty,
 * or on how successfully the user has answered each question in the past.
 */
public class Quiz {

    /**
     * List of questions to be asked in a quiz.
     * This can be sorted to provide a unique quiz mode.
     */
    protected final ArrayList<Question> questions;
    /**
     * The user taking the quiz.
     * Quiz results will be written to this user's statistics.
     */
    protected final User user;
    /**
     * The topic of the quiz e.g. Discrete Maths, Comp Org etc.
     */
    private final String topic;
    /**
     * The UI object that will display this quiz.
     */
    private final UI ui;
    /**
     * @param topic  The topic of the quiz.
     * @param user   The user taking the quiz.
     * @param loader The question loader to load questions for the specified topic.
     * @param ui     Todo
     */
    protected Quiz(String topic, User user, QuestionLoader loader, UI ui) {
        this.topic = topic;
        this.user = user;
        this.ui = ui;
        // Load the questions from a file
        questions = loader.getEntries(topic);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Asks a single question, and verifies if the user entered the correct answer.
     *
     * @param question The question to be asked
     * @return If the user selected the correct answer
     */
    private boolean askQuestion(Question question) {
        // Check if the user answered correctly
        if (ui.askQuestion(question)) {
            // If the question has never been answered by this user, it will not exist in the user history
            // If this is the case, place a zero
            user.getHistory().putIfAbsent(question.question(), 0);
            return true;
        } else {
            // If they answer incorrectly, increment that question's counter in the user history
            user.getHistory().merge(question.question(), 1, Integer::sum);
            return false;
        }
    }

    /**
     * Ask all the questions in the sorted/shuffled list,
     * keeping track of the amount answered correctly
     *
     * @return The number of correctly-answered questions
     */
    public int askQuestions() {
        // Ask all questions, keeping track of the amount answered correctly
        int correct = 0;
        for (Question question : questions) {
            if (askQuestion(question)) {
                correct++;
            }
        }
        ui.displayResults(correct, questions.size());
        // Increment the number of rounds played in the user's history once all questions have been asked
        user.getHistory().merge("Rounds", 1, Integer::sum);
        user.updateFile();
        return correct;
    }

    public String topic() {
        return topic;
    }
}

