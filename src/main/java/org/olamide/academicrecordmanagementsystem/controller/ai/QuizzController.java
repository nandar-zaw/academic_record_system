package org.olamide.academicrecordmanagementsystem.controller.ai;

import org.olamide.academicrecordmanagementsystem.dto.ai.QuizQuestionDTO;
import org.olamide.academicrecordmanagementsystem.dto.ai.QuizRequestDTO;
import org.olamide.academicrecordmanagementsystem.service.ai.GeminiAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizzController {

    @Autowired
    private GeminiAIService geminiAIService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateQuiz(@RequestBody QuizRequestDTO request) {
        try {
            List<QuizQuestionDTO> quiz = geminiAIService.generateQuiz(
                    request.getTopic(),
                    request.getNumberOfQuestions(),
                    request.getDifficulty()
            );
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating quiz: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Quiz API is working!");
    }
}