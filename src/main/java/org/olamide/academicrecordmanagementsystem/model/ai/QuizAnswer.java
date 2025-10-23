package org.olamide.academicrecordmanagementsystem.model.ai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_answers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_attempt_id", nullable = false)
    @Setter
    @ToString.Exclude
    @JsonIgnore
    private QuizAttempt quizAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @Setter
    @ToString.Exclude
    private QuizQuestion question;

    @Column(nullable = false)
    @Setter
    private String studentAnswer; // the answer student selected

    @Column(nullable = false)
    @Setter
    @Builder.Default
    private Boolean isCorrect = false;

    @Transient
    public Integer getQuestionId() {
        return question != null ? question.getId() : null;
    }

    @Transient
    public String getQuestionText() {
        return question != null ? question.getQuestionText() : null;
    }
}
