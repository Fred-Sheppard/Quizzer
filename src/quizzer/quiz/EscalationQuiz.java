package quizzer.quiz;

import quizzer.Question;
import quizzer.QuestionLoader;
import quizzer.UI;
import quizzer.User;

import java.util.Comparator;

/**
 * quizzer.quiz.Quiz type that asks the questions in order of difficulty.
 */
public class EscalationQuiz extends Quiz {

    public EscalationQuiz(String topic, User user, QuestionLoader loader, UI ui) {
        super(topic, user, loader, ui);
    }

    @Override
    public int askQuestions() {
        // Sort the questions by difficulty
        questions.sort(Comparator.comparing(Question::difficulty));
        return super.askQuestions();
    }
}
