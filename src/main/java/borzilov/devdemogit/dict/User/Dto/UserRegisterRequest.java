package borzilov.devdemogit.dict.User.Dto;

import jakarta.validation.constraints.*;

public record UserRegisterRequest(
        @NotBlank(message = "login must not be blank")
        @Size(min = 3, max = 50)
        String login,

        @NotBlank(message = "password must not be blank")
        @Size(min = 8, max = 100)
        String password
) {
}
