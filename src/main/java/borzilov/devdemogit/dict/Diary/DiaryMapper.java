package borzilov.devdemogit.dict.Diary;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DiaryMapper {

    public Diary toDomain(DiaryEntity e) {
        if (e == null) return null;

        return new Diary(
                e.getId(),
                e.getDate(),
                e.getHoursOfSleep(),
                e.getQualityOfSleep(),
                e.getLevel(),
                e.getProductivity(),
                e.getRatingOfDay(),
                e.getReflection(),
                e.getOneSentence(),

                e.getTimeline().stream()
                        .map(t -> new Diary.TimeLineItemDto(
                                t.getId(), t.getTimeFrom(), t.getTimeTo(), t.getActivity()))
                        .collect(Collectors.toList()),

                e.getGoals().stream()
                        .map(g -> new Diary.GoalDto(
                                g.getId(), g.getText(), g.getCompleted()))
                        .collect(Collectors.toList()),

                e.getHabits().stream()
                        .map(h -> new Diary.HabitDto(
                                h.getId(), h.getName(), h.getCompleted()))
                        .collect(Collectors.toList())
        );
    }

    public DiaryEntity toEntity(Diary d) {
        if (d == null) return null;

        DiaryEntity entity = DiaryEntity.builder()
                .id(d.id())
                .date(d.date())
                .hoursOfSleep(d.hoursOfSleep())
                .qualityOfSleep(d.qualityOfSleep())
                .level(d.level())
                .productivity(d.productivity())
                .ratingOfDay(d.ratingOfDay())
                .reflection(d.reflection())
                .oneSentence(d.oneSentence())
                .build();

        if (d.timeline() != null) {
            for (var t : d.timeline()) {
                entity.getTimeline().add(TimeLineItem.builder()
                        .timeFrom(t.timeFrom())
                        .timeTo(t.timeTo())
                        .activity(t.activity())
                        .diary(entity)
                        .build());
            }
        }
        if (d.goals() != null) {
            for (var g : d.goals()) {
                entity.getGoals().add(Goal.builder()
                        .text(g.text())
                        .completed(Boolean.TRUE.equals(g.completed()))
                        .diary(entity)
                        .build());
            }
        }
        if (d.habits() != null) {
            for (var h : d.habits()) {
                entity.getHabits().add(Habit.builder()
                        .name(h.name())
                        .completed(Boolean.TRUE.equals(h.completed()))
                        .diary(entity)
                        .build());
            }
        }
        return entity;
    }
}