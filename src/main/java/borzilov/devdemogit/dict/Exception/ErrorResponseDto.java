package borzilov.devdemogit.dict.Exception;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,

        String detailedMessage, //errorMessage

        LocalDateTime errorTime
) {
}
