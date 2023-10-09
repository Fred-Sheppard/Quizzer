import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        QuestionLoader loader = new QuestionLoader(new File("test/questions/"));
        String[] topics = loader.listTopics();
        for (String s : topics) {
            System.out.println(s);
        }
        var map = loader.getEntries("CS");
        System.out.println(map);
    }
}