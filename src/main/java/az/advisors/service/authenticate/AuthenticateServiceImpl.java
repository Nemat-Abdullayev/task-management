package az.advisors.service.authenticate;

import az.advisors.model.entity.User;
import az.advisors.security.JwtAuthenticationRequest;
import az.advisors.security.JwtAuthenticationResponse;
import az.advisors.security.JwtTokenUtil;
import az.advisors.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateServiceImpl(UserService userService,
                                   JwtTokenUtil jwtTokenUtil,
                                   AuthenticationManager authenticationManager,
                                   PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> generateToken(JwtAuthenticationRequest authenticationRequest) {
        try {
            LOGGER.info("Enter inputs of authentication request==>username :[" + authenticationRequest.getUsername() + "] password :[" + authenticationRequest.getPassword() + "]");
            User user = userService.findByUsername(authenticationRequest.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()
            ));
            final String token = jwtTokenUtil.generateToken(user);
            LOGGER.info("after generate token is Token: [" + token + "]");
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("An error while generateToken:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }
    }
}
