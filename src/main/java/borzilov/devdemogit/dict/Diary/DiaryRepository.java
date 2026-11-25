package borzilov.devdemogit.dict.Diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

// Убрали @EntityGraph, так как он вызывал MultipleBagFetchException.
// Загрузка списков теперь происходит внутри @Transactional в сервисе.
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    Optional<DiaryEntity> findByDateAndUserId(LocalDate date, Long userId);
    Optional<DiaryEntity> findByIdAndUserId(Long id, Long userId);
}