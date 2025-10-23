package org.olamide.academicrecordmanagementsystem.repository.ai_quizz;


import org.olamide.academicrecordmanagementsystem.model.ai.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {

    List<QuizAttempt> findByStudentId(Integer studentId);

    List<QuizAttempt> findByQuizId(Integer quizId);

    List<QuizAttempt> findByStudentIdAndQuizId(Integer studentId, Integer quizId);

    Optional<QuizAttempt> findByStudentIdAndQuizIdAndIsCompletedFalse(Integer studentId, Integer quizId);
}
