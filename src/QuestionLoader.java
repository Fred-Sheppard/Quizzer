import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

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
        String path = String.format("%s/%s.txt", folder.getAbsolutePath(), topic);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            System.out.printf("File %s could not be found", path);
            System.exit(1);
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
        String[] list = folder.list();
        // We check the contents in the constructor, we know the folder contains valid files
        @SuppressWarnings("DataFlowIssue")
        String[] out = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            String s = list[i].split("\\.")[0];
            out[i] = s;
        }
        return out;
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
