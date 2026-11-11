package borzilov.devdemogit.dict.Diary;

import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepository  extends JpaRepository<DiaryEntity, Long> {
    Optional<DiaryEntity> findByDate(LocalDate date);

    Optional<DiaryEntity> findByIdAndUserId(Long id, Long userId);
    Optional<DiaryEntity> findByDateAndUserId(LocalDate date, Long userId);
}
