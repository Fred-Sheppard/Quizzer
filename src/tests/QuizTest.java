package tests;

import quizzer.Question;
import quizzer.QuestionLoader;
import quizzer.UI;
import quizzer.User;
import quizzer.quiz.EscalationQuiz;
import quizzer.quiz.Quiz;

import java.io.File;

public class QuizTest {
    public static void run() {
        // Each question is answered false
        UI allWrong = new UI() {
            @Override
            public boolean askQuestion(Question question) {
                return false;
            }

            @Override
            public void displayResults(int correctQuestions, int totalQuestions) {

            }
        };
        Quiz quiz = new EscalationQuiz("Maths", new User(""), new QuestionLoader(new File("questions")), allWrong);
        assert quiz.askQuestions() == 0;

        // Each question is answered true
        UI allRight = new UI() {
            @Override
            public boolean askQuestion(Question question) {
                return false;
            }

            @Override
            public void displayResults(int correctQuestions, int totalQuestions) {

            }
        };
        quiz = new EscalationQuiz("Maths", new User(""), new QuestionLoader(new File("questions")), allRight);
        assert quiz.askQuestions() == 6;
    }
}
