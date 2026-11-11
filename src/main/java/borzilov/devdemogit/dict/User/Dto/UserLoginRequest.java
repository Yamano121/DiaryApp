package borzilov.devdemogit.dict.User.Dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "login must not be blank")
        String login,

        @NotBlank(message = "password must not be blank")
        String password
) {
}