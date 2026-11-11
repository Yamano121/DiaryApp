package borzilov.devdemogit.dict.Config;

import borzilov.devdemogit.dict.User.extex.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String USER_ID_ATTR = "userId";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(USER_ID_ATTR) == null) {
            log.warn("Unauthorized access attempt to {} {}", request.getMethod(), request.getRequestURI());
            throw new UnauthorizedException();
        }

        return true;
    }
}