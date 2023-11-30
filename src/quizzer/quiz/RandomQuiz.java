package quizzer.quiz;

import quizzer.QuestionLoader;
import quizzer.UI;
import quizzer.User;

import java.util.Collections;

/**
 * quizzer.quiz.Quiz type that asks the questions in a random order.
 */
public class RandomQuiz extends Quiz {

    public RandomQuiz(String topic, User user, QuestionLoader loader, UI ui) {
        super(topic, user, loader, ui);
    }

    @Override
    public int askQuestions() {
        Collections.shuffle(questions);
        return super.askQuestions();
    }
}
