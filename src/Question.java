package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class representing a question, answer and set of wrong answers for a topic,
 * to be loaded from a file.
 */
public class Question {

    public enum Difficulty {
        NOVICE,
        INTERMEDIATE,
        EXPERT
    }

    private final String question;
    private final String answer;
    private final String[] wrongs;
    private final Difficulty difficulty;

    public Question(String line) {
        String[] arr = line.split("\\|");
        question = arr[0];
        answer = arr[1];
        wrongs = Arrays.copyOfRange(arr, 2, 5);
        difficulty = Difficulty.valueOf(arr[5]);
    }

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

    public ArrayList<String> possibilities() {
        ArrayList<String> list = new ArrayList<>(List.of(wrongs));
        list.add(answer);
        return list;
    }
}
