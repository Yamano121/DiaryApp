package borzilov.devdemogit.dict.Exception;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(
        String error,

        List<String> errors,

        LocalDateTime timestamp
) {
}
