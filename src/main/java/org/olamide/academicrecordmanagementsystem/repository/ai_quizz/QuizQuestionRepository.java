package org.olamide.academicrecordmanagementsystem.repository.ai_quizz;

import org.olamide.academicrecordmanagementsystem.model.ai.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Integer> {

    List<QuizQuestion> findByQuizIdOrderByOrderIndexAsc(Integer quizId);
}
