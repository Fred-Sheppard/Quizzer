import java.util.Arrays;

public class Answer {

    private final String question;
    private final String answer;
    private final String[] wrongs;

    public Answer(String[] array) {
        question = array[0];
        answer = array[1];
        wrongs = Arrays.stream(array).skip(2).toArray(String[]::new);
    }

    public String toString() {
        return "Question: " + question +
                "\nAnswer: " + answer;
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
