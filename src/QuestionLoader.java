import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class QuestionLoader {
    private final File folder;
    private final String FILE_EXTENSION = ".txt";

    public QuestionLoader(File folder) {
        if (!folder.isDirectory()) {
            throw new InvalidDirectoryException("File is not a Directory");
        } else if (Objects.requireNonNull(folder.list()).length == 0) {
            throw new InvalidDirectoryException("Directory is empty");
        }
        this.folder = folder;
    }

    /**
     * Return a map of the questions and answers for the given topic
     *
     * @param topic The topic to retrieve the entries of e.g. CS, Maths
     * @return A map mapping questions to the correct answers
     * @throws FileNotFoundException Occurs if the given topic was not found in the topics folder
     */
    public ArrayList<Answer> getEntries(String topic) throws FileNotFoundException {
        var reader = new BufferedReader(new FileReader(folder.getAbsolutePath() + "/" + topic + FILE_EXTENSION));
        ArrayList<Answer> list = new ArrayList<>();
        reader.lines().forEach(line -> list.add(new Answer(line)));
        return list;
    }

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

    public static class InvalidDirectoryException extends RuntimeException {
        public InvalidDirectoryException(String message) {
            super(message);
        }
    }
}
