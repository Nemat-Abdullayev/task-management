package az.advisors.service.authenticate;

import az.advisors.security.JwtAuthenticationRequest;
import org.springframework.http.ResponseEntity;

import javax.naming.AuthenticationException;


public interface AuthenticateService {

    ResponseEntity<?> generateToken(JwtAuthenticationRequest jwtAuthenticationRequest) throws AuthenticationException;
}
