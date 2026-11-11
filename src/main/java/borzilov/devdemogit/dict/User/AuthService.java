package borzilov.devdemogit.dict.User;


import borzilov.devdemogit.dict.User.Dto.UserLoginRequest;
import borzilov.devdemogit.dict.User.Dto.UserRegisterRequest;
import borzilov.devdemogit.dict.User.Dto.UserResponse;
import borzilov.devdemogit.dict.User.extex.InvalidCredentialsException;
import borzilov.devdemogit.dict.User.extex.LoginAlreadyTakenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Transactional
    public UserResponse register(UserRegisterRequest userRegisterRequest) {
        if (repository.existsByLogin(userRegisterRequest.login())) {
            throw new LoginAlreadyTakenException(userRegisterRequest.login());
        }
        var entity = UserEntity.builder()
                .login(userRegisterRequest.login())
                .passwordHash(passwordEncoder.encode(userRegisterRequest.password()))
                .enabled(true)
                .build();
        try {
            var saved = repository.saveAndFlush(entity);
            return mapper.toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            // гонка: кто-то вставил тот же логин между existsBy и save
            throw new LoginAlreadyTakenException(userRegisterRequest.login());
        }
    }

    @Transactional(readOnly = true)
    public UserResponse login(UserLoginRequest request) {
        log.info("Login attempt for login={}", request.login());

        var user = repository.findByLogin(request.login())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getEnabled()) {
            log.warn("Login attempt for disabled user: {}", request.login());
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.warn("Wrong password for login={}", request.login());
            throw new InvalidCredentialsException();
        }

        return mapper.toResponse(user);
    }


}
