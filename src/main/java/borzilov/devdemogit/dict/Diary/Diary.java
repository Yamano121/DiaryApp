package borzilov.devdemogit.dict.Diary;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO (domain-модель), которая летит между фронтом и бэком.
 *
 * Почему record, а не класс:
 *  - неизменяемый объект (immutable) — безопасно передавать между слоями
 *  - автоматически генерируются геттеры, equals, hashCode, toString
 *  - меньше кода
 *
 * ВАЖНО: все поля (кроме id) — nullable.
 * Это нужно, чтобы фронт мог прислать "пустое поле" как null,
 * и на UI отобразилась "—" (прочерк), а не 0 или пустая строка.
 */
public record Diary(
        /**
         * id — null при создании новой записи,
         *      заполняется, когда запись уже сохранена в БД.
         * @Null — запрещаем фронту присылать id (защита от подмены чужой записи).
         */
        @Null
        Long id,

        /**
         * Дата дня, за который ведётся запись.
         * Формат: "YYYY-MM-DD" (LocalDate).
         * Может быть null, но в сервисе мы проверим и кинем IllegalArgumentException.
         */
        LocalDate date,

        /**
         * Метрики дня. Все — Integer (могут быть null = "не заполнено").
         * Ограничения:
         *  - hoursOfSleep:  0..24
         *  - qualityOfSleep: 0..10
         *  - level:          0..10
         *  - productivity:   0..10
         *  - ratingOfDay:    0..10
         */
        @Min(0) @Max(24)
        Integer hoursOfSleep,

        @Min(0) @Max(10)
        Integer qualityOfSleep,

        @Min(0) @Max(10)
        Integer level,

        @Min(0) @Max(10)
        Integer productivity,

        @Min(0) @Max(10)
        Integer ratingOfDay,

        /**
         * Итоги дня.
         *  - reflection: несколько предложений о прошедшем дне
         *  - oneSentence: одно предложение дня
         */
        String reflection,
        String oneSentence,

        /**
         * Вложенные списки. Могут быть null/пустыми.
         * Фронт присылает их вместе с днём, бэк сохраняет каскадно.
         */
        List<TimeLineItemDto> timeline,
        List<GoalDto> goals,
        List<HabitDto> habits
) {

        /**
         * Вложенный record для таймлайна.
         * Один интервал: с 09:00 по 12:00 — "Работа".
         */
        public record TimeLineItemDto(
                Long id,        // null при создании
                String timeFrom,// "09:00" или null
                String timeTo,  // "12:00" или null
                String activity // "Работа" или null
        ) {}

        /**
         * Вложенный record для цели дня.
         * completed = true/false — выполнена ли цель.
         */
        public record GoalDto(
                Long id,
                String text,
                Boolean completed
        ) {}

        /**
         * Вложенный record для привычки.
         * Привычка привязана к конкретному дню (по ТЗ).
         */
        public record HabitDto(
                Long id,
                String name,
                Boolean completed
        ) {}
}