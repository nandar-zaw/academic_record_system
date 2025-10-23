package org.olamide.academicrecordmanagementsystem.dto.ai_quiz;

public class QuizRequestDTO {
    private String topic;
    private int numberOfQuestions = 5;
    private String difficulty = "medium";

    // Constructors
    public QuizRequestDTO() {}

    public QuizRequestDTO(String topic, int numberOfQuestions, String difficulty) {
        this.topic = topic;
        this.numberOfQuestions = numberOfQuestions;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
