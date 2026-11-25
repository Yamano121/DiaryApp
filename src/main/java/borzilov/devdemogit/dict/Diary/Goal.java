package borzilov.devdemogit.dict.Diary;

import jakarta.persistence.*;
import lombok.*;

/**
 * Одна цель дня.
 * Пример: "Дописать главу" — completed=false.
 */
@Entity
@Table(name = "goals")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Текст цели. Может быть null (но фронт фильтрует пустые). */
    @Column(length = 500)
    private String text;

    /** Выполнена ли. По умолчанию false. */
    private Boolean completed;

    /** Ссылка на день */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private DiaryEntity diary;
}