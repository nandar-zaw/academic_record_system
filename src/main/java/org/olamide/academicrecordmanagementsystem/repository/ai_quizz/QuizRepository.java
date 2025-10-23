package org.olamide.academicrecordmanagementsystem.repository.ai_quizz;

import org.olamide.academicrecordmanagementsystem.model.ai.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    List<Quiz> findByCourseId(Integer courseId);

    List<Quiz> findByCourseIdAndIsActive(Integer courseId, Boolean isActive);
}