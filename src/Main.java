import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        QuestionLoader loader = new QuestionLoader(new File("test/questions/"));
        String[] topics = loader.listTopics();
        System.out.println("Topics:");
        for (String s : topics) {
            System.out.println(s);
        }
        System.out.println();
        var answers = loader.getEntries("CS");
        for (Answer a : answers) {
            System.out.println(a);
        }
    }
}