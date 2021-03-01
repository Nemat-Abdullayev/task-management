package az.advisors.service.user;

import az.advisors.model.entity.User;
import az.advisors.model.view.UserRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;


public interface UserService {
    User findByUsername(String username);

    ResponseEntity<?> create(UserRequest userRequest, HttpServletRequest request);
}
