import java.util.Arrays;

/**
 * A class representing a question, answer and set of wrong answers for a topic,
 * to be loaded from a file.
 */
public class Answer {

    private final String question;
    private final String answer;
    private final String[] wrongs;

    public Answer(String line) {
        String[] arr = line.split(",");
        question = arr[0];
        answer = arr[1];
        wrongs = Arrays.stream(arr).skip(2).toArray(String[]::new);
    }

    public String toString() {
        return String.format("[Q: %s. A: %s. W: %s]", question, answer, Arrays.toString(wrongs));
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
}
