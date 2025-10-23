package org.olamide.academicrecordmanagementsystem.model.ai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.olamide.academicrecordmanagementsystem.model.Auditable;
import org.olamide.academicrecordmanagementsystem.model.Course;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Quiz extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Setter
    private String title;

    @Setter
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @Setter
    @ToString.Exclude
    @JsonIgnore
    private Course course;

    @Column(nullable = false)
    @Setter
    private String difficulty; // easy, medium, hard

    @Column(nullable = false)
    @Setter
    private Integer totalQuestions;

    @Column(nullable = false)
    @Setter
    private Integer durationMinutes; // time limit

    @Setter
    private LocalDateTime availableFrom;

    @Setter
    private LocalDateTime availableUntil;

    @Column(nullable = false)
    @Setter
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<QuizQuestion> questions = new ArrayList<>();

    @Transient
    public String getCourseCode() {
        return course != null ? course.getCourseCode() : null;
    }

    @Transient
    public String getCourseTitle() {
        return course != null ? course.getTitle() : null;
    }

    // Helper methods
    public void addQuestion(QuizQuestion question) {
        questions.add(question);
        question.setQuiz(this);
    }

    public void removeQuestion(QuizQuestion question) {
        questions.remove(question);
        question.setQuiz(null);
    }
}
