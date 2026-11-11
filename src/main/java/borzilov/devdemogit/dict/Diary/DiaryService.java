package borzilov.devdemogit.dict.Diary;

import borzilov.devdemogit.dict.Diary.extex.DiaryPageAlreadyExistsException;
import borzilov.devdemogit.dict.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Diary createPageOfDiary(
            Diary pageToCreate,
            Long userId
    ) {
        var entityToSave = mapper.toEntity(pageToCreate);

        entityToSave.setUser(userRepository.getReferenceById(userId));

        try {
            var savedDiary = diaryRepository.saveAndFlush(entityToSave);
            return mapper.toDomain(savedDiary);
        } catch (DataIntegrityViolationException e) {
            throw new DiaryPageAlreadyExistsException(pageToCreate.date());
        }
    }

    public Diary getDiaryById(
            Long id,
            Long userId
    ) {
        DiaryEntity diaryEntity = diaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found page of diary by id = " + id
                ));

        return mapper.toDomain(diaryEntity);
    }

    public Diary getDiaryByDate(
            LocalDate date,
            Long userId
    ) {
        DiaryEntity diaryEntity = diaryRepository.findByDateAndUserId(date, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found page of diary by date = " + date
                ));

        return mapper.toDomain(diaryEntity);
    }
}
