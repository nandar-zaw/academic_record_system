package org.olamide.academicrecordmanagementsystem.service.ai;

import org.olamide.academicrecordmanagementsystem.enums.EnrollmentStatus;
import org.olamide.academicrecordmanagementsystem.model.Enrollment;
import org.olamide.academicrecordmanagementsystem.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PredictionService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public double predictGrade(int studentId, int courseId) {
        List<Enrollment> history = enrollmentRepository.findByStudent_IdAndStatus(studentId, EnrollmentStatus.COMPLETED);

        List<Map<String, Object>> historyJson = history.stream().map(
                e-> Map.<String, Object>of(
                        "courseId", e.getCourse().getId(),
                        "grade",  e.getGrade().ordinal() * 10.0 + 50.0
                )).collect(Collectors.toList());
        Map<String, Object> requestBody = Map.of(
                "studentId", studentId,
                "history", historyJson,
                "targetCourseId", courseId
        );

        String url = "http://localhost:5000/predictGrade?studentId=" + studentId + "&courseId=" + courseId;
        Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
        return response != null &&  response.get("predicted_grade") != null? (Double) response.get("predicted_grade") : 0.0;
    }
}
