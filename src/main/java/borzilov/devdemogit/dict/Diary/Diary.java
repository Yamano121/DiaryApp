package borzilov.devdemogit.dict.Diary;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.time.LocalTime;

public record Diary(
        @Null
        Long id,

        @NotNull
        String target,

        @NotNull
        LocalDate date,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime,

        @NotNull
        @Min(value = 0, message = "hoursOfSleep must be >= 0")
        @Max(value = 24, message = "hoursOfSleep must be <= 24")
        Integer hoursOfSleep,

        @NotNull
        @Min(value = 1, message = "qualityOfSleep must be >= 1")
        @Max(value = 10, message = "qualityOfSleep must be <= 10")
        Integer qualityOfSleep,

        @NotNull
        Integer level,

        @NotNull
        Integer productivity,

        @NotNull
        Integer ratingOfDay,

        @NotNull
        String resultOfDay
) {
}
