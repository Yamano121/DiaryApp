package borzilov.devdemogit.dict.User;

import borzilov.devdemogit.dict.User.Dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserResponse(entity.getId(), entity.getLogin());
    }
}