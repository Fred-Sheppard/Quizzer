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
        ArrayList<Question> list = new ArrayList<>();
        reader.lines().forEach(line -> list.add(new Question(line)));
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
