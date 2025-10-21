package org.olamide.academicrecordmanagementsystem.controller.ai;

import org.olamide.academicrecordmanagementsystem.model.Course;
import org.olamide.academicrecordmanagementsystem.service.ai.PredictionService;
import org.olamide.academicrecordmanagementsystem.service.ai.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/ai/")
public class AiIntegrationController {
    @Autowired
    private PredictionService predictionService;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/predictGrade")
    public double predictGrade(@RequestParam int studentId, @RequestParam int courseId) {
        return predictionService.predictGrade(studentId, courseId);
    }

    @GetMapping("/recommendCourses")
    public List<Course> recommendCourses(@RequestParam int studentId){
        return recommendationService.recommendCourses(studentId);
    }
}
