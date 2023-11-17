import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class representing a quiz.
 * <p>
 * A quiz is a set of questions asked in a specific order.
 * This order could be random, based on difficulty,
 * or on how successfully the user has answered each question in the past.
 */
public abstract class Quiz {

    /**
     * The topic of the quiz e.g. Discrete Maths, Comp Org etc.
     */
    private final String topic;
    /**
     * List of questions to be asked in a quiz.
     * This can be sorted to provide a unique quiz mode.
     */
    public final ArrayList<Question> questions;
    /**
     * The user taking the quiz.
     * Quiz results will be written to this user's statistics.
     */
    protected final User user;
    private final UI ui;

    /**
     * Constructor for the Quiz class.
     *
     * @param topic  The topic of the quiz.
     * @param user   The user taking the quiz.
     * @param loader The question loader to load questions for the specified topic.
     * @param ui     Todo
     */
    public Quiz(String topic, User user, QuestionLoader loader, UI ui) {
        this.topic = topic;
        this.user = user;
        this.ui = ui;
        // Load the questions from a file
        questions = loader.getEntries(topic);
    }

    /**
     * Asks a single question, and verifies if the user entered the correct answer.
     *
     * @param question The question to be asked
     * @return If the user selected the correct answer
     */
    private boolean askQuestion(Question question) {
        // All wrong answers, plus the correct answer
        List<String> possibilities = question.possibilities();
        // Each time a question is asked, the order should be unique
        Collections.shuffle(possibilities);
        // Find which answer (0-3) is the correct one
        // This will be used later to check if they chose correctly
        int answer = possibilities.indexOf(question.answer());
        if (ui.askQuestion(question, answer)) {
            // If the question has never been answered by this user, it will not exist in the user history
            // If this is the case, place a zero
            user.getHistory().putIfAbsent(question.question(), 0);
            return true;
        } else {
            // Increment the value in the map by 1
            user.getHistory().merge(question.question(), 1, Integer::sum);
            return false;
        }
    }

    protected int askQuestions() {
        // Ask all the questions in the sorted/shuffled list,
        // keeping track of the amount answered correctly
//        int correct = 0;
//        for (Question q : questions) {
//            boolean userCorrect = askQuestion(q);
//            if (userCorrect) correct++;
//        }
        int correct = (int) questions.stream().filter(this::askQuestion).count();
        // Increment the number of rounds played in the user's history
        user.getHistory().merge("Rounds", 1, Integer::sum);
        user.updateFile();
        return correct;
    }

    public String topic() {
        return topic;
    }
}

/**
 * Asks the questions in a random order.
 */
class RandomQuiz extends Quiz {

    public RandomQuiz(String topic, User user, QuestionLoader loader, UI ui) {
        super(topic, user, loader, ui);
    }

    @Override
    public int askQuestions() {
        Collections.shuffle(questions);
        return super.askQuestions();
    }
}

/**
 * Asks the questions in order of difficulty.
 */
class EscalationQuiz extends Quiz {

    public EscalationQuiz(String topic, User user, QuestionLoader loader, UI ui) {
        super(topic, user, loader, ui);
    }

    @Override
    public int askQuestions() {
        // Sort the questions by difficulty
        questions.sort(Comparator.comparing(Question::difficulty));
        return super.askQuestions();
    }
}

/**
 * Sorts and asks the questions, placing the user's worst-answered questions first.
 */
class RedemptionQuiz extends Quiz {

    public RedemptionQuiz(String topic, User user, QuestionLoader loader, UI ui) {
        super(topic, user, loader, ui);
    }

    @Override
    public int askQuestions() {
        // Sort by the values in the map
        // This places the questions with the most wrong answers at the start of the list
        // If the question has no entry in the list, assume it has always been answered correctly
        questions.sort((a, b) -> user.getHistory().getOrDefault(b.question(), 0)
                .compareTo(user.getHistory().getOrDefault(a.question(), 0)));
        return super.askQuestions();
    }
}
