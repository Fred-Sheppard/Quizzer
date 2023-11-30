package quizzer.quiz;

import quizzer.Question;
import quizzer.QuestionLoader;
import quizzer.UI;
import quizzer.User;

import java.util.Comparator;

/**
 * quizzer.quiz.Quiz type that asks the questions by placing the user's worst-asked questions first.
 */
public class RedemptionQuiz extends Quiz {

    public RedemptionQuiz(String topic, User user, QuestionLoader loader, UI ui) {
        super(topic, user, loader, ui);
    }

    @Override
    public int askQuestions() {
        // Sort by the values in the map
        // This places the questions with the most wrong answers at the start of the list
        // If the question has no entry in the list, assume it has always been answered correctly
        System.out.println(user.getHistory());
        questions.sort(Comparator.<Question>comparingInt(
                question -> user
                        .getHistory()
                        .getOrDefault(question.question(), 0)).reversed());
        return super.askQuestions();
    }
}
