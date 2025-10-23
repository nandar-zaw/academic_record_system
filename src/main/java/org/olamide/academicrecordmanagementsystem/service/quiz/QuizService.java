package org.olamide.academicrecordmanagementsystem.service.quiz;


import org.olamide.academicrecordmanagementsystem.dto.ai_quiz.QuizQuestionDTO;
import org.olamide.academicrecordmanagementsystem.model.Course;
import org.olamide.academicrecordmanagementsystem.model.ai.Quiz;
import org.olamide.academicrecordmanagementsystem.model.ai.QuizQuestion;
import org.olamide.academicrecordmanagementsystem.repository.CourseRepository;
import org.olamide.academicrecordmanagementsystem.repository.ai_quizz.QuizQuestionRepository;
import org.olamide.academicrecordmanagementsystem.repository.ai_quizz.QuizRepository;
import org.olamide.academicrecordmanagementsystem.service.ai.GeminiAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GeminiAIService geminiAIService;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Transactional
    public Quiz generateAndSaveQuiz(Integer courseId, String topic, int numberOfQuestions,
                                    String difficulty, Integer durationMinutes) {
        // Find the course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        // Generate quiz questions using AI
        List<QuizQuestionDTO> aiQuestions = geminiAIService.generateQuiz(topic, numberOfQuestions, difficulty);

        // Create Quiz entity
        Quiz quiz = Quiz.builder()
                .title("Quiz: " + topic)
                .description("AI-generated quiz on " + topic)
                .course(course)
                .difficulty(difficulty)
                .totalQuestions(numberOfQuestions)
                .durationMinutes(durationMinutes)
                .isActive(true)
                .availableFrom(LocalDateTime.now())
                .build();

        // Convert AI questions to QuizQuestion entities
        int orderIndex = 1;
        for (QuizQuestionDTO dto : aiQuestions) {
            QuizQuestion question = QuizQuestion.builder()
                    .questionText(dto.getQuestion())
                    .option1(dto.getOptions().get(0))
                    .option2(dto.getOptions().get(1))
                    .option3(dto.getOptions().get(2))
                    .option4(dto.getOptions().get(3))
                    .correctAnswer(dto.getCorrectAnswer())
                    .explanation(dto.getExplanation())
                    .orderIndex(orderIndex++)
                    .build();

            quiz.addQuestion(question);
        }

        // Save to database
        return quizRepository.save(quiz);
    }

    public List<Quiz> getQuizzesByCourse(Integer courseId) {
        return quizRepository.findByCourseId(courseId);
    }

    public Quiz getQuizById(Integer quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
    }

    public List<Quiz> getActiveQuizzesByCourse(Integer courseId) {
        return quizRepository.findByCourseIdAndIsActive(courseId, true);
    }

    public String generateHintForQuestion(Integer questionId) {
        // Find the question
        QuizQuestion question = quizQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        // Prepare options as a list
        List<String> options = List.of(
                question.getOption1(),
                question.getOption2(),
                question.getOption3(),
                question.getOption4()
        );

        // Generate hint using AI
        return geminiAIService.generateHint(
                question.getQuestionText(),
                options,
                question.getCorrectAnswer()
        );
    }
}
