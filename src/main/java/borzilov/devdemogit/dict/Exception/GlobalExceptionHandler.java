package borzilov.devdemogit.dict.Exception;

import borzilov.devdemogit.dict.Diary.extex.DiaryPageAlreadyExistsException;
import borzilov.devdemogit.dict.User.extex.InvalidCredentialsException;
import borzilov.devdemogit.dict.User.extex.LoginAlreadyTakenException;
import borzilov.devdemogit.dict.User.extex.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginAlreadyTakenException.class)
    public ResponseEntity<ErrorResponseDto> loginAlreadyTaken(
            LoginAlreadyTakenException e
    ) {
        log.warn("Login already taken: {}", e.getMessage());

        var errorDto = new ErrorResponseDto(
                "LOGIN_ALREADY_TAKEN",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValid(
            MethodArgumentNotValidException e
    ) {
        log.warn("Validation failed: {}", e.getMessage());

        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();

        var errorDto = new ValidationErrorResponse(
                "VALIDATION_FAILED",
                errors,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> invalidCredentials(
            InvalidCredentialsException e
    ) {
        log.warn("Invalid credentials: {}", e.getMessage());
        var errorDto = new ErrorResponseDto(
                "INVALID_CREDENTIALS",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> unauthorized(
            UnauthorizedException e
    ) {
        log.warn("Unauthorized: {}", e.getMessage());
        var errorDto = new ErrorResponseDto(
                "UNAUTHORIZED",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> entityNotFound(
            EntityNotFoundException e
    ) {
        log.warn("Entity not found: {}", e.getMessage());
        var errorDto = new ErrorResponseDto(
                "NOT_FOUND",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(DiaryPageAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> diaryPageAlreadyExists(
            DiaryPageAlreadyExistsException e
    ) {
        log.warn("Diary page already exists: {}", e.getMessage());
        var errorDto = new ErrorResponseDto(
                "DIARY_PAGE_ALREADY_EXISTS",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> dataIntegrityViolation(
            DataIntegrityViolationException e
    ) {
        log.error("Data integrity violation", e);  // тут log.error, потому что это неожиданно
        var errorDto = new ErrorResponseDto(
                "DATA_INTEGRITY_VIOLATION",
                "Data integrity violation",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

}