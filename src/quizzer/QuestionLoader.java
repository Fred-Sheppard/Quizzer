package quizzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class to deal with loading questions from files.
 * Contains methods to return a List of questions,
 * along with a list of available topics.
 */
public class QuestionLoader {
    private final File folder;

    /**
     * Constructor for a new question loader.
     *
     * @param folder The folder in which questions are found
     */
    public QuestionLoader(File folder) {
        if (!folder.isDirectory()) {
            throw new InvalidDirectoryException(String.format("File %s is not a Directory", folder));
        } else if (Objects.requireNonNull(folder.list()).length == 0) {
            throw new InvalidDirectoryException(String.format("Directory %s is empty", folder));
        }
        // We now know the directory is valid, and is not empty. We can begin loading files.
        this.folder = folder;
    }

    /**
     * Return a map of the questions and answers for the given topic.
     *
     * @param topic The topic to retrieve the entries of e.g. CS, Maths
     * @return A map mapping questions to the correct answers
     */
    public ArrayList<Question> getEntries(String topic) {
        // The path to the text file containing the questions
        Path path = Paths.get(folder.getAbsolutePath(), topic + ".txt");
        // Populate the list with entries from the question file
        ArrayList<Question> list;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            // The Question constructor will handle parsing each line
            // For each line in the file, create a new question from that line
            // and place it into the list
            list = reader.lines()
                    .map(Question::new)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            // If the question cannot be loaded, there is an issue with file loading.
            // The quiz will be unable to run, so we must exit
            throw new InvalidDirectoryException(String.format("File %s could not be loaded", path));
        }
        return list;
    }

    /**
     * List all available topics.
     * This reads all filenames in the questions directory, and returns them as a string array.
     *
     * @return Array of all available quiz topics
     */
    public String[] listTopics() {
        // Return a list of file names, minus the file extension
        // e.g. Maths.txt -> Maths
        // Folder is guaranteed to be non-empty in the constructor, so we can ignore null checks
        //noinspection DataFlowIssue
        return Arrays.stream((folder.listFiles()))
                .map(file -> file.getName().split("\\.")[0])
                .toArray(String[]::new);
    }

    /**
     * Error class to handle an invalid directory being passed to the constructor.
     */
    public static class InvalidDirectoryException extends RuntimeException {
        public InvalidDirectoryException(String message) {
            super(message);
        }
    }
}
