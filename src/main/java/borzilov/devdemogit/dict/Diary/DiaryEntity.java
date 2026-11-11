package borzilov.devdemogit.dict.Diary;

import borzilov.devdemogit.dict.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private String target;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private Integer hoursOfSleep;

    @Column(nullable = false)
    private Integer qualityOfSleep;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Integer productivity;

    @Column(nullable = false)
    private Integer ratingOfDay;

    @Column(nullable = false)
    private String resultOfDay;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() {this.updatedAt = Instant.now();}

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

}
