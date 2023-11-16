import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class representing a question, answer and set of wrong answers for a topic,
 * to be loaded from a file.
 */
public class Question {

    /**
     * The difficulty of a given question.
     * Used in sorting in Escalation mode.
     */
    public enum Difficulty {
        NOVICE,
        INTERMEDIATE,
        EXPERT
    }

    /**
     * The question being asked.
     * e.g. "Who invented the finite state machine?"
     */
    private final String question;
    /**
     * The answer to the question.
     * e.g. "Alan Turing"
     */
    private final String answer;
    /**
     * List of incorrect answers.
     */
    private final String[] wrongs;
    /**
     * The difficulty of the question being asked.
     */
    private final Difficulty difficulty;

    /**
     * Creates a new question by parsing the passed String.
     * The String should be of the following format:
     * <p>
     * Question|Answer|Wrong1|Wrong2|Wrong3|Difficulty
     *
     * @param line The String formatted as above, from which the question if read
     */
    public Question(String line) {
        // Split the line by pipes
        String[] arr = line.split("\\|");
        if (arr.length < 6) {
            throw new IllegalArgumentException(String.format("Invalid line found when reading file:%n%s", line));
        }
        question = arr[0];
        answer = arr[1];
        // The next 3 elements are the incorrect answers
        wrongs = Arrays.copyOfRange(arr, 2, 5);
        difficulty = Difficulty.valueOf(arr[5]);
    }

    /**
     * Format the question for printing.
     *
     * @return String representation of the Question
     */
    public String toString() {
        return String.format("[Q: %s. A: %s. W: %s. D: %s]", question, answer, Arrays.toString(wrongs), difficulty);
    }

    public String question() {
        return question;
    }

    public String answer() {
        return answer;
    }

    public String[] wrongs() {
        return wrongs;
    }

    public Difficulty difficulty() {
        return difficulty;
    }

    /**
     * All possible answers for this question.
     * That is, all wrong answers plus the correct answer.
     *
     * @return List of possible answers
     */
    public List<String> possibilities() {
        ArrayList<String> list = new ArrayList<>(List.of(wrongs));
        list.add(answer);
        return list;
    }
}
