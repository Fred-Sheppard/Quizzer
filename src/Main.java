import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        QuestionLoader loader = new QuestionLoader(new File("test/questions/"));
        String[] topics = loader.listTopics();
        for (String s : topics) {
            System.out.println(s);
        }
        var answer = loader.getEntries("CS");
        System.out.println(answer);
        var wrongs = answer.wrongs();
        for (String s : wrongs) {
            System.out.println(s);
        }
    }
}