package org.olamide.academicrecordmanagementsystem.model.ai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_questions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @Setter
    @ToString.Exclude
    @JsonIgnore
    private Quiz quiz;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String questionText;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String option1;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String option2;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String option3;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String option4;

    @Column(nullable = false)
    @Setter
    private String correctAnswer; // stores which option is correct (option1, option2, etc.)

    @Column(columnDefinition = "TEXT")
    @Setter
    private String explanation;

    @Column(nullable = false)
    @Setter
    @Builder.Default
    private Integer orderIndex = 0; // to maintain question order
}