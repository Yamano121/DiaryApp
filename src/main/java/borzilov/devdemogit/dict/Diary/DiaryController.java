package borzilov.devdemogit.dict.Diary;

import borzilov.devdemogit.dict.Config.AuthInterceptor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;


    @PostMapping()
    public ResponseEntity<Diary> createPageOfDiary(
            @RequestBody @Valid Diary pageToCreate,
            @SessionAttribute(AuthInterceptor.USER_ID_ATTR) Long userId
    ) {
        log.info("Called createPageOfDiary for userId={}", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(diaryService.createPageOfDiary(pageToCreate, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diary> getDiaryById(
            @PathVariable("id") Long id,
            @SessionAttribute(AuthInterceptor.USER_ID_ATTR) Long userId
    ) {
        return ResponseEntity.ok(diaryService.getDiaryById(id, userId));
    }

    @GetMapping("/by-date/{date}")
    public ResponseEntity<Diary> getDiaryByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @SessionAttribute(AuthInterceptor.USER_ID_ATTR) Long userId
    ) {
        return ResponseEntity.ok(diaryService.getDiaryByDate(date, userId));
    }
}
