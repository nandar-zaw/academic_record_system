package org.olamide.academicrecordmanagementsystem.service.ai;

import org.olamide.academicrecordmanagementsystem.model.Course;
import org.olamide.academicrecordmanagementsystem.model.Enrollment;
import org.olamide.academicrecordmanagementsystem.repository.CourseRepository;
import org.olamide.academicrecordmanagementsystem.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<Course> recommendCourses(int studentId){
        List<Course> allCourses = courseRepository.findAll();
        List<Enrollment> currentEnrollments = enrollmentRepository.findByStudent_Id(studentId);
        Set<Integer> enrolledCourseIds = currentEnrollments.stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toSet());
        return allCourses.stream()
                .filter(c-> !enrolledCourseIds.contains(c.getId()))
                .sorted((c1,c2)-> Double.compare(
                        predictionService.predictGrade(studentId, c2.getId()),
                        predictionService.predictGrade(studentId,c1.getId())
                ))
                .limit(5)
                .toList();
    }
}
