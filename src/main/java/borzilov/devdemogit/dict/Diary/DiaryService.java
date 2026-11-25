package borzilov.devdemogit.dict.Diary;

import borzilov.devdemogit.dict.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryMapper mapper;
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Diary save(Diary page, Long userId) {
        log.info(">>> НАЧАЛО СОХРАНЕНИЯ. userId={}, date={}", userId, page.date());
        if (page.date() == null) {
            throw new IllegalArgumentException("date is required");
        }

        DiaryEntity entity = diaryRepository.findByDateAndUserId(page.date(), userId)
                .orElseGet(() -> {
                    log.info("Создаем новую запись для date={}", page.date());
                    return new DiaryEntity();
                });

        entity.setUser(userRepository.getReferenceById(userId));
        entity.setDate(page.date());
        entity.setHoursOfSleep(page.hoursOfSleep());
        entity.setQualityOfSleep(page.qualityOfSleep());
        entity.setLevel(page.level());
        entity.setProductivity(page.productivity());
        entity.setRatingOfDay(page.ratingOfDay());
        entity.setReflection(page.reflection());
        entity.setOneSentence(page.oneSentence());

        entity.getTimeline().clear();
        if (page.timeline() != null) {
            for (var t : page.timeline()) {
                entity.getTimeline().add(TimeLineItem.builder()
                        .timeFrom(t.timeFrom()).timeTo(t.timeTo()).activity(t.activity())
                        .diary(entity).build());
            }
        }

        entity.getGoals().clear();
        if (page.goals() != null) {
            for (var g : page.goals()) {
                entity.getGoals().add(Goal.builder()
                        .text(g.text()).completed(Boolean.TRUE.equals(g.completed()))
                        .diary(entity).build());
            }
        }

        entity.getHabits().clear();
        if (page.habits() != null) {
            for (var h : page.habits()) {
                entity.getHabits().add(Habit.builder()
                        .name(h.name()).completed(Boolean.TRUE.equals(h.completed()))
                        .diary(entity).build());
            }
        }

        DiaryEntity saved = diaryRepository.saveAndFlush(entity);
        log.info("<<< СОХРАНЕНО УСПЕШНО. id={}, date={}", saved.getId(), saved.getDate());
        return mapper.toDomain(saved);
    }

    // ВАЖНО: @Transactional(readOnly = true) держит сессию Hibernate открытой,
    // пока Mapper преобразует Entity в Domain (читает коллекции timeline/goals/habits).
    // Без этого была ошибка LazyInitializationException.
    @Transactional(readOnly = true)
    public Diary getDiaryById(Long id, Long userId) {
        DiaryEntity e = diaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Not found diary by id = " + id));
        return mapper.toDomain(e);
    }

    @Transactional(readOnly = true)
    public Diary getDiaryByDate(LocalDate date, Long userId) {
        log.info(">>> ЗАГРУЗКА. userId={}, date={}", userId, date);
        Diary d = diaryRepository.findByDateAndUserId(date, userId)
                .map(mapper::toDomain)
                .orElse(null);

        if (d == null) log.info("Запись за {} не найдена (204 No Content)", date);
        else log.info("Запись найдена, id={}", d.id());

        return d;
    }
}