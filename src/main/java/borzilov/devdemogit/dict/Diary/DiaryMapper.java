package borzilov.devdemogit.dict.Diary;

import org.springframework.stereotype.Component;

@Component
public class DiaryMapper {

    // Преобразование из Entity (БД) в Domain Record (для API)
    public Diary toDomain(DiaryEntity diaryEntity) {
        if (diaryEntity == null) {
            return null;
        }

        return new Diary(
                diaryEntity.getId(),
                diaryEntity.getTarget(),
                diaryEntity.getDate(),
                diaryEntity.getStartTime(),
                diaryEntity.getEndTime(),
                diaryEntity.getHoursOfSleep(),
                diaryEntity.getQualityOfSleep(),
                diaryEntity.getLevel(),
                diaryEntity.getProductivity(),
                diaryEntity.getRatingOfDay(),
                diaryEntity.getResultOfDay()
        );
    }

    // Преобразование из Domain Record в Entity для сохранения в БД
    public DiaryEntity toEntity(Diary diaryToEntity) {
        if (diaryToEntity == null) {
            return null;
        }

        // Используем Builder, чтобы код был читаемым и безопасным к изменению порядка полей
        return DiaryEntity.builder()
                .id(diaryToEntity.id())
                .target(diaryToEntity.target())
                .date(diaryToEntity.date())
                .startTime(diaryToEntity.startTime())
                .endTime(diaryToEntity.endTime())
                .hoursOfSleep(diaryToEntity.hoursOfSleep())
                .qualityOfSleep(diaryToEntity.qualityOfSleep())
                .level(diaryToEntity.level())
                .productivity(diaryToEntity.productivity())
                .ratingOfDay(diaryToEntity.ratingOfDay())
                .resultOfDay(diaryToEntity.resultOfDay())
                // Поля createAt и updateAt не заполняем, за них отвечают @PrePersist/@PreUpdate
                .build();
    }
}
