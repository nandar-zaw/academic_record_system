package org.olamide.academicrecordmanagementsystem.controller.quiz;

import org.olamide.academicrecordmanagementsystem.dto.ai_quiz.QuizRequestDTO;
import org.olamide.academicrecordmanagementsystem.dto.ai_quiz.QuizResultDTO;
import org.olamide.academicrecordmanagementsystem.dto.ai_quiz.QuizSubmissionDTO;
import org.olamide.academicrecordmanagementsystem.model.ai.Quiz;
import org.olamide.academicrecordmanagementsystem.model.ai.QuizAttempt;
import org.olamide.academicrecordmanagementsystem.service.quiz.QuizAttemptService;
import org.olamide.academicrecordmanagementsystem.service.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizAttemptService quizAttemptService;


    @PostMapping("/generate/{courseId}")
    public ResponseEntity<?> generateQuizForCourse(
            @PathVariable Integer courseId,
            @RequestBody QuizRequestDTO request) {
        try {
            Quiz quiz = quizService.generateAndSaveQuiz(
                    courseId,
                    request.getTopic(),
                    request.getNumberOfQuestions(),
                    request.getDifficulty(),
                    30 // default 30 minutes duration
            );
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating quiz: " + e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getQuizzesByCourse(@PathVariable Integer courseId) {
        try {
            List<Quiz> quizzes = quizService.getQuizzesByCourse(courseId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching quizzes: " + e.getMessage());
        }
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuizById(@PathVariable Integer quizId) {
        try {
            Quiz quiz = quizService.getQuizById(quizId);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/course/{courseId}/active")
    public ResponseEntity<?> getActiveQuizzes(@PathVariable Integer courseId) {
        try {
            List<Quiz> quizzes = quizService.getActiveQuizzesByCourse(courseId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching quizzes: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Quiz API is working!");
    }

    @GetMapping("/question/{questionId}/hint")
    public ResponseEntity<?> getHintForQuestion(@PathVariable Integer questionId) {
        try {
            String hint = quizService.generateHintForQuestion(questionId);
            return ResponseEntity.ok(Map.of("hint", hint));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating hint: " + e.getMessage());
        }
    }

    //attempt quiz
    @PostMapping("/start")
    public ResponseEntity<?> startQuiz(@RequestParam Integer quizId,
                                       @RequestParam Integer studentId) {
        try {
            QuizAttempt attempt = quizAttemptService.startQuizAttempt(quizId, studentId);
            return ResponseEntity.ok(Map.of(
                    "message", "Quiz started successfully",
                    "attemptId", attempt.getId(),
                    "startTime", attempt.getStartTime()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error starting quiz: " + e.getMessage());
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        try {
            QuizResultDTO result = quizAttemptService.submitQuiz(submission);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error submitting quiz: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/attempts")
    public ResponseEntity<?> getStudentAttempts(@PathVariable Integer studentId) {
        try {
            List<QuizAttempt> attempts = quizAttemptService.getStudentAttempts(studentId);
            return ResponseEntity.ok(attempts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching attempts: " + e.getMessage());
        }
    }

    @GetMapping("/attempt/{attemptId}")
    public ResponseEntity<?> getAttemptDetails(@PathVariable Integer attemptId) {
        try {
            QuizAttempt attempt = quizAttemptService.getAttemptById(attemptId);
            return ResponseEntity.ok(attempt);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}