package com.example.zipanggotest;

import java.util.List;

public class QuizResult {
    private List<String> correctAnswer, wrongAnswer;

    public QuizResult(List<String> correctAnswer, List<String> wrongAnswer) {
        this.correctAnswer = correctAnswer;
        this.wrongAnswer = wrongAnswer;
    }

    public int getTotalCorrect() {
        return correctAnswer.size();
    }

    public int getTotalWrong() {
        return wrongAnswer.size();
    }

    public int getTotalSize() {
        return correctAnswer.size() + wrongAnswer.size();
    }

    public double getCorrectPercentage() {
        return (correctAnswer.size() / (double) (correctAnswer.size() + wrongAnswer.size()) * 100);
    }
}
