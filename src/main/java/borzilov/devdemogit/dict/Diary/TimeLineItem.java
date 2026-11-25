package borzilov.devdemogit.dict.Diary;

import jakarta.persistence.*;
import lombok.*;

/**
 * Один интервал таймлайна.
 * Пример строки: "17:00 — 20:00 | Работа".
 *
 * Связь @ManyToOne на DiaryEntity (одна запись дня — много интервалов).
 */
@Entity
@Table(name = "timeline_items")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Время начала, строка "HH:mm" (например "17:00"). Может быть null. */
    private String timeFrom;

    /** Время окончания, "HH:mm". Может быть null. */
    private String timeTo;

    /** Описание активности. Может быть null. */
    @Column(length = 500)
    private String activity;

    /**
     * Обратная ссылка на день.
     * mappedBy на стороне DiaryEntity указывает на это поле.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private DiaryEntity diary;
}