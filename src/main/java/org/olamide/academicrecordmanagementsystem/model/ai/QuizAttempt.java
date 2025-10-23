package org.olamide.academicrecordmanagementsystem.model.ai;


import jakarta.persistence.*;
import lombok.*;
import org.olamide.academicrecordmanagementsystem.model.Auditable;
import org.olamide.academicrecordmanagementsystem.model.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_attempts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class QuizAttempt extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @Setter
    @ToString.Exclude
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @Setter
    @ToString.Exclude
    private Student student;

    @Column(nullable = false)
    @Setter
    private LocalDateTime startTime;

    @Setter
    private LocalDateTime endTime;

    @Setter
    private Integer score; // out of totalQuestions

    @Setter
    private Double percentage;

    @Column(nullable = false)
    @Setter
    @Builder.Default
    private Boolean isCompleted = false;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<QuizAnswer> answers = new ArrayList<>();

    // Helper methods
    public void addAnswer(QuizAnswer answer) {
        answers.add(answer);
        answer.setQuizAttempt(this);
    }

    public void removeAnswer(QuizAnswer answer) {
        answers.remove(answer);
        answer.setQuizAttempt(null);
    }
}
