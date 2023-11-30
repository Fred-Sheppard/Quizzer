package quizzer;

public interface UI {
    boolean askQuestion(Question question);

    void displayResults(int correctQuestions, int totalQuestions);
}
