package az.advisors.service.user;

import az.advisors.model.entity.User;
import az.advisors.model.entity.UserRole;
import az.advisors.model.enums.RoleName;
import az.advisors.model.view.UserRequest;
import az.advisors.repository.user.UserRepository;
import az.advisors.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final String jwtHeader;
    private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(UserRepository userRepository,
                           @Lazy PasswordEncoder passwordEncoder,
                           RoleService roleService,
                           @Value("${jwt.header}") String jwtHeader,
                           JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtHeader = jwtHeader;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Override
    public User findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> create(UserRequest userRequest, HttpServletRequest request) {
        try {
            String[] dataFromToken = getUserDataFromHttpServletRequest(request).split("#");
            if (Objects.nonNull(dataFromToken[1]) && RoleName.ADMIN.toString().equals(dataFromToken[1])) {
                UserRole userRole = roleService.findOne(userRequest.getRoleName());
                User user = User.builder()
                        .age(userRequest.getAge())
                        .password(passwordEncoder.encode(userRequest.getPassword()))
                        .name(userRequest.getName())
                        .surname(userRequest.getSurname())
                        .userRole(userRole)
                        .username(userRequest.getUsername())
                        .build();
                return ResponseEntity.ok(userRepository.save(user));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN role can create user!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getUserDataFromHttpServletRequest(HttpServletRequest request) {
        final String requestHeader = request.getHeader(this.jwtHeader);
        if (Objects.nonNull(requestHeader)
                && requestHeader.startsWith("Bearer ")
                && !requestHeader.contains("null")) {
            String authorizationToken = requestHeader.substring(7);
            return jwtTokenUtil.getUserNameFromToken(authorizationToken);
        } else {
            return null;
        }
    }
}
