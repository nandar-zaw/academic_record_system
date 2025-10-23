package org.olamide.academicrecordmanagementsystem.dto.ai_quiz;


import java.util.List;

public class QuizSubmissionDTO {
    private Integer quizId;
    private Integer studentId;
    private List<AnswerDTO> answers;

    // Constructors
    public QuizSubmissionDTO() {}

    public QuizSubmissionDTO(Integer quizId, Integer studentId, List<AnswerDTO> answers) {
        this.quizId = quizId;
        this.studentId = studentId;
        this.answers = answers;
    }

    // Getters and Setters
    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    // Inner class for individual answers
    public static class AnswerDTO {
        private Integer questionId;
        private String answer;

        public AnswerDTO() {}

        public AnswerDTO(Integer questionId, String answer) {
            this.questionId = questionId;
            this.answer = answer;
        }

        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}