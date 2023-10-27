import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        // How many rounds in total have been answered
        int rounds = history.getOrDefault("Rounds", 0);
        //noinspection unchecked
        var map = (HashMap<String, Integer>) history.clone();
        map.remove("Rounds");
        // List corresponding to each question, counting how many times each was answered correctly
        List<Integer> corrects = map.values().stream().map(wrong -> rounds - wrong).toList();
        // How many questions have ever been answered correctly
        int totalCorrect = corrects.stream().reduce(0, Integer::sum);
        return switch (stat) {
            // #correct questions divided by #total questions answered
            case MEAN -> (double) totalCorrect / (rounds * map.size());
            // The middle of the sorted list of correctly answered questions
            case MEDIAN -> (double) corrects.stream().sorted().toList().get(corrects.size() / 2) / rounds;
            case TOTAL_CORRECT -> totalCorrect;
            case TOTAL_ANSWERED -> rounds * map.size();
        };
    }

    /**
     * Calculates the standard deviation of all users' answer means.
     *
     * @return The standard deviation of all users
     */
    public static double stdDev() {
        var means = userMeans();
        // Mean of the list of means (mu)
        // Same as means.sum() / means.size()
        double mu = means.stream().reduce(0.0, Double::sum) / means.size();
        // SUM(x - mu)^2
        double xLessMuSquared = means.stream().reduce(0.0, (accum, x) -> accum + (x - mu) * (x - mu));
        // stdDev = sqrt[ (x - mu)^2 / N ]
        int n = means.size();
        return Math.sqrt(xLessMuSquared / n);
    }

    /**
     * Returns a list of the means of all users.
     *
     * @return ArrayList of means
     */
    public static ArrayList<Double> userMeans() {
        // Each user has a list of all the problems they've gotten wrong
        File userDir = new File("GameData/UserHistory/");
        // Each index will hold the mean of a user
        ArrayList<Double> means = new ArrayList<>();
        // Iterate through all user history files
        for (String name : Objects.requireNonNull(userDir.list())) {
            User user = new User(name.split("\\.")[0]);
            means.add(user.getStatistic(Statistic.MEAN));
        }
        return means;
    }
}
