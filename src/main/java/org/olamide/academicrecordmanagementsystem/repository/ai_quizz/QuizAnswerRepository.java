package org.olamide.academicrecordmanagementsystem.repository.ai_quizz;


import org.olamide.academicrecordmanagementsystem.model.ai.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Integer> {

    List<QuizAnswer> findByQuizAttemptId(Integer quizAttemptId);
}