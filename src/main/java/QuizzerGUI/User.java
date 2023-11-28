package QuizzerGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A class representing a user of the quiz, created after logging in.
 * Contains methods for querying statistics of the user.
 */
public class User {
    /**
     * Username of the user.
     */
    private final String name;

    /**
     * Map of all incorrectly answered questions,
     * along with how many times they have been answered incorrectly.
     * This is public to allow it to be edited directly by calling code.
     */
    public HashMap<String, Integer> history;

    /**
     * File containing the user's question history.
     * Public to allow it to be written to directly by calling code.
     */
    public final File historyFile;

    /**
     * Creates a User with the given username.
     *
     * @param name The user's username
     */
    public User(String name) {
        this.name = name;
        historyFile = new File("GameData/UserHistory/" + name + ".txt");
        BufferedReader reader;
        try {
            if (!historyFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                historyFile.createNewFile();
            }
            reader = new BufferedReader(new FileReader(historyFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        history = new HashMap<>();
        reader.lines().forEach(line -> {
            String[] split = line.split("\\|");
            history.put(split[0], Integer.parseInt(split[1]));
        });
    }

    public String name() {
        return name;
    }


    /**
     * Returns a given statistic for the user.
     * Valid statistics are:
     * <pre>{@code
     * MEAN
     * MODE
     * MEDIAN
     * TOTAL_CORRECT
     * TOTAL_ANSWERED}</pre>
     * <p>
     * To query standard deviation, use User.stdDev().
     *
     * @param stat The statistic to be queried
     * @return The value of the queried statistic
     */
    public double getStatistic(Statistic stat) {
        // If user as never answered any questions
        if (history.isEmpty()) return 0;
        // How many rounds in total have been answered
        int rounds = history.getOrDefault("Rounds", 0);
        //noinspection unchecked
        var map = (HashMap<String, Integer>) history.clone();
        map.remove("Rounds");
        List<Integer> corrects = history.values().stream()
                .map(wrong -> rounds - wrong)
                .toList();
        int totalCorrect = corrects.stream().reduce(0, Integer::sum);
        return switch (stat) {
            case MEAN -> (double) totalCorrect / (rounds * history.size());
            case MEDIAN -> (double) corrects.stream()
                    .sorted()
                    .toList()
                    .get(corrects.size() / 2) / rounds;
            case TOTAL_CORRECT -> totalCorrect;
            case TOTAL_ANSWERED -> rounds * history.size();
            default -> 0.0;  // Added a default case to handle any unhandled enum values
        };
    }

    public static ArrayList<Double> allUserMeans() {
        File userDir = new File("GameData/UserHistory/");
        ArrayList<Double> means = new ArrayList<>();
        for (String name : Objects.requireNonNull(userDir.list())) {
            User user = new User(name.split("\\.")[0]);
            means.add(user.getStatistic(Statistic.MEAN));
        }
        return means;
    }
    public static double stdDev() {
        var means = allUserMeans();
        double mu = means.stream().reduce(0.0, Double::sum) / means.size();
        double xLessMuSquared = means.stream()
                .reduce(0.0, (accum, x) -> accum + (x - mu) * (x - mu));
        int n = means.size();
        return Math.sqrt(xLessMuSquared / n);
    }
    /**
     * Returns a String representation of a leaderboard, comparing users by their means.
     *
     * @return String leaderboard
     */
    public static String leaderboard() {
        File userDir = new File("GameData/UserHistory/");
        TreeMap<Double, String> leaderboard = new TreeMap<>();
        for (String name : Objects.requireNonNull(userDir.list())) {
            User user = new User(name.split("\\.")[0]);
            leaderboard.put(user.getStatistic(Statistic.MEAN), user.name());
        }
        StringBuilder builder = new StringBuilder("User \t Score\n");
        leaderboard.descendingMap().forEach((score, name) -> builder.append(String.format("%s \t %.2f%n", name, score)));
        return builder.toString();
    }
}
