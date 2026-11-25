package borzilov.devdemogit.dict.Diary;

import jakarta.persistence.*;
import lombok.*;

/**
 * Одна привычка за день.
 * Пример: "Читать 30 страниц" — completed=true.
 *
 * Важно: привычки привязаны к дню (diary_id), а не к пользователю глобально.
 * Каждый день — свой набор привычек (по ТЗ: "привычки, которые обязан выполнять в течение дня").
 */
@Entity
@Table(name = "habits")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String name;

    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private DiaryEntity diary;
}