package borzilov.devdemogit.dict.Diary;

import borzilov.devdemogit.dict.Config.AuthInterceptor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST-контроллер дневника.
 *
 * Изменения:
 *  - POST /diary теперь UPSERT (раньше был только CREATE).
 *  - GET /diary/by-date/{date} возвращает 204 No Content, если записи нет
 *    (фронт трактует 204 как "новая запись").
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * Создать ИЛИ обновить запись дня.
     * Возвращает 200 OK с сохранённой записью (не 201, чтобы фронт не различал случаи).
     */
    @PostMapping
    public ResponseEntity<Diary> save(
            @RequestBody @Valid Diary page,
            @SessionAttribute(AuthInterceptor.USER_ID_ATTR) Long userId
    ) {
        log.info("Called save diary for userId={}, date={}", userId, page.date());
        return ResponseEntity.ok(diaryService.save(page, userId));
    }

    /** Получить запись по id (для внутренних нужд/отладки). */
    @GetMapping("/{id}")
    public ResponseEntity<Diary> getDiaryById(
            @PathVariable("id") Long id,
            @SessionAttribute(AuthInterceptor.USER_ID_ATTR) Long userId
    ) {
        return ResponseEntity.ok(diaryService.getDiaryById(id, userId));
    }

    /**
     * Получить запись за конкретную дату.
     * Если нет — 204 No Content (пустое тело).
     */
    @GetMapping("/by-date/{date}")
    public ResponseEntity<Diary> getDiaryByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @SessionAttribute(AuthInterceptor.USER_ID_ATTR) Long userId
    ) {
        Diary d = diaryService.getDiaryByDate(date, userId);
        return d == null
                ? ResponseEntity.noContent().build()      // 204
                : ResponseEntity.ok(d);                   // 200 + JSON
    }
}