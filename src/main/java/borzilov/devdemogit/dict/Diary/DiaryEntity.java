package borzilov.devdemogit.dict.Diary;

import borzilov.devdemogit.dict.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "diary",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_diary_user_date",
                columnNames = {"user_id", "date"}
        )
)
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDate date;

    private Integer hoursOfSleep;
    private Integer qualityOfSleep;
    private Integer level;
    private Integer productivity;
    private Integer ratingOfDay;

    @Column(length = 4000)
    private String reflection;

    @Column(length = 1000)
    private String oneSentence;

    // Добавлен @OrderColumn, чтобы Hibernate не падал с MultipleBagFetchException
    // и сохранял порядок добавления элементов
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    @Builder.Default
    private List<TimeLineItem> timeline = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    @Builder.Default
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    @Builder.Default
    private List<Habit> habits = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}