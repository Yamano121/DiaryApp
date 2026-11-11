package borzilov.devdemogit.dict.User;

import borzilov.devdemogit.dict.Config.AuthInterceptor;
import borzilov.devdemogit.dict.User.Dto.UserLoginRequest;
import borzilov.devdemogit.dict.User.Dto.UserRegisterRequest;
import borzilov.devdemogit.dict.User.Dto.UserResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registration(
            @RequestBody @Valid UserRegisterRequest userRegisterRequest
    ) {
        log.info("Called registration");
        var response = authService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody UserLoginRequest request,
            HttpSession session
    ) {
        log.info("Called login");
        var response = authService.login(request);
        session.setAttribute(AuthInterceptor.USER_ID_ATTR, response.id());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        log.info("Called logout");
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

}
