package org.olamide.academicrecordmanagementsystem.service.quiz;

import org.olamide.academicrecordmanagementsystem.dto.ai_quiz.QuizResultDTO;
import org.olamide.academicrecordmanagementsystem.dto.ai_quiz.QuizSubmissionDTO;
import org.olamide.academicrecordmanagementsystem.model.Student;
import org.olamide.academicrecordmanagementsystem.model.ai.Quiz;
import org.olamide.academicrecordmanagementsystem.model.ai.QuizAnswer;
import org.olamide.academicrecordmanagementsystem.model.ai.QuizAttempt;
import org.olamide.academicrecordmanagementsystem.model.ai.QuizQuestion;
import org.olamide.academicrecordmanagementsystem.repository.StudentRepository;
import org.olamide.academicrecordmanagementsystem.repository.ai_quizz.QuizAnswerRepository;
import org.olamide.academicrecordmanagementsystem.repository.ai_quizz.QuizAttemptRepository;
import org.olamide.academicrecordmanagementsystem.repository.ai_quizz.QuizQuestionRepository;
import org.olamide.academicrecordmanagementsystem.repository.ai_quizz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private QuizAnswerRepository quizAnswerRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public QuizAttempt startQuizAttempt(Integer quizId, Integer studentId) {
        // Check if quiz exists
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Check if student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if student already has an incomplete attempt
        quizAttemptRepository.findByStudentIdAndQuizIdAndIsCompletedFalse(studentId, quizId)
                .ifPresent(attempt -> {
                    throw new RuntimeException("Student already has an incomplete attempt for this quiz");
                });

        // Create new attempt
        QuizAttempt attempt = QuizAttempt.builder()
                .quiz(quiz)
                .student(student)
                .startTime(LocalDateTime.now())
                .isCompleted(false)
                .build();

        return quizAttemptRepository.save(attempt);
    }

    @Transactional
    public QuizResultDTO submitQuiz(QuizSubmissionDTO submission) {
        // Find the student's incomplete attempt
        QuizAttempt attempt = quizAttemptRepository
                .findByStudentIdAndQuizIdAndIsCompletedFalse(
                        submission.getStudentId(),
                        submission.getQuizId()
                )
                .orElseThrow(() -> new RuntimeException("No active quiz attempt found"));

        // Process each answer
        int correctCount = 0;
        for (QuizSubmissionDTO.AnswerDTO answerDTO : submission.getAnswers()) {
            QuizQuestion question = quizQuestionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            boolean isCorrect = question.getCorrectAnswer().equals(answerDTO.getAnswer());
            if (isCorrect) {
                correctCount++;
            }

            QuizAnswer answer = QuizAnswer.builder()
                    .question(question)
                    .studentAnswer(answerDTO.getAnswer())
                    .isCorrect(isCorrect)
                    .build();

            attempt.addAnswer(answer);
        }

        // Calculate score and percentage
        attempt.setEndTime(LocalDateTime.now());
        attempt.setScore(correctCount);
        attempt.setPercentage((double) correctCount / attempt.getQuiz().getTotalQuestions() * 100);
        attempt.setIsCompleted(true);

        quizAttemptRepository.save(attempt);

        // Build result DTO
        return buildResultDTO(attempt);
    }

    public List<QuizAttempt> getStudentAttempts(Integer studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }

    public List<QuizAttempt> getQuizAttempts(Integer quizId) {
        return quizAttemptRepository.findByQuizId(quizId);
    }

    public QuizAttempt getAttemptById(Integer attemptId) {
        return quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found"));
    }

    private QuizResultDTO buildResultDTO(QuizAttempt attempt) {
        QuizResultDTO result = new QuizResultDTO();
        result.setAttemptId(attempt.getId());
        result.setQuizId(attempt.getQuiz().getId());
        result.setQuizTitle(attempt.getQuiz().getTitle());
        result.setStudentId(attempt.getStudent().getId());
        result.setScore(attempt.getScore());
        result.setTotalQuestions(attempt.getQuiz().getTotalQuestions());
        result.setPercentage(attempt.getPercentage());
        result.setStartTime(attempt.getStartTime());
        result.setEndTime(attempt.getEndTime());

        if (attempt.getStartTime() != null && attempt.getEndTime() != null) {
            Duration duration = Duration.between(attempt.getStartTime(), attempt.getEndTime());
            result.setTimeTakenMinutes(duration.toMinutes());
        }

        return result;
    }
}