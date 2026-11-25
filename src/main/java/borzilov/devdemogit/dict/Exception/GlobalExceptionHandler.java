package borzilov.devdemogit.dict.Exception;

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

@Slf4j                                       // ← генерирует переменную log
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ❌ УДАЛЕНО: обработчик DiaryPageAlreadyExistsException
    // (класс extex/DiaryPageAlreadyExistsException тоже удалён)

    @ExceptionHandler(LoginAlreadyTakenException.class)
    public ResponseEntity<ErrorResponseDto> loginAlreadyTaken(LoginAlreadyTakenException e) {
        log.warn("Login already taken: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto("LOGIN_ALREADY_TAKEN", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("Validation failed: {}", e.getMessage());
        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse("VALIDATION_FAILED", errors, LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> invalidCredentials(InvalidCredentialsException e) {
        log.warn("Invalid credentials: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("INVALID_CREDENTIALS", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> unauthorized(UnauthorizedException e) {
        log.warn("Unauthorized: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("UNAUTHORIZED", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> entityNotFound(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto("NOT_FOUND", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> dataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("Data integrity violation", e);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto("DATA_INTEGRITY_VIOLATION", "Data integrity violation", LocalDateTime.now()));
    }

    @ExceptionHandler(IllegalArgumentException.class)              // ← новый: при date=null
    public ResponseEntity<ErrorResponseDto> illegalArgument(IllegalArgumentException e) {
        log.warn("Bad request: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto("BAD_REQUEST", e.getMessage(), LocalDateTime.now()));
    }
}