package quizzer;

import java.io.*;
import java.util.*;

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
    private final HashMap<String, Integer> history;

    /**
     * File containing the user's question history.
     * Public to allow it to be written to directly by calling code.
     */
    private final File historyFile;

    /**
     * Creates a quizzer.User with the given username.
     *
     * @param name The user's username
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public User(String name) {
        this.name = name;
        historyFile = new File("GameData/UserHistory/" + name + ".txt");
        // If the file or any of the parent directories do not exist, create them
        if (!historyFile.exists()) {
            historyFile.getParentFile().mkdirs();
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        history = (HashMap<String, Integer>) updateHistoryFromFile();
    }

    public Map<String, Integer> updateHistoryFromFile() {
        // Use a try-with block to automatically flush and close the file
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            // Create a new HashMap to count the number of questions answered incorrectly by the user
            // <quizzer.Question>|<Count> gets split into key and value, and is placed into the map
            reader.lines().forEach(line -> {
                String[] split = line.split("\\|");
                map.put(split[0], Integer.parseInt(split[1]));
            });
        } catch (IOException e) {
            throw new RuntimeException("Error reading user history file", e);
        }
        return map;
    }

    public void updateFile() {
        // Write user's history to a file using Stream API
        // Use a try-with block to automatically flush and close the file on finish
        try (PrintWriter writer = new PrintWriter(new FileWriter(historyFile))) {
            // Use a forEach loop to concisely print the data
            getHistory().forEach((k, v) -> writer.println(k + "|" + v));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return name;
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
     * To query standard deviation, use quizzer.User.stdDev().
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


    /**
     * Returns a String representation of a leaderboard, comparing users by their means.
     *
     * @return String leaderboard
     */
    public static String leaderboard() {
        File userDir = new File("GameData/UserHistory/");
        // Use a TreeMap to sort the entries as they are entered
        TreeMap<Double, String> leaderboard = new TreeMap<>();
        // Add all usernames to the TreeMap
        // All users have a file named after them
        for (String name : Objects.requireNonNull(userDir.list())) {
            User user = new User(name.split("\\.")[0]);
            // Place each user in the map, sorting them by their mean
            leaderboard.put(user.getStatistic(Statistic.MEAN), user.name());
        }
        StringBuilder builder = new StringBuilder("User \t Score\n");
        builder.append("---- \t -----\n");
        // Sort descending, with the top score at the top of the leaderboard
        leaderboard.descendingMap().forEach((score, name) ->
                builder.append(String.format("%s \t %.2f%n", name, score)));
        // Return a String to allow the caller to display the leaderboard as desired
        return builder.toString();
    }

    // Getters and Setters

    public HashMap<String, Integer> getHistory() {
        return history;
    }
}