package dev.aj.config.security.controller;

import dev.aj.config.security.dto.LoginDetails;
import dev.aj.config.security.dto.UserRegisterationDetails;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.service.SecurityUserDetailsService;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SecurityUserController {

    private final SecurityUserDetailsService securityUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterationDetails> registerUser(@RequestBody UserRegisterationDetails user) {
        return ResponseEntity.ok(securityUserDetailsService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody LoginDetails loginDetails, Authentication authentication) throws UserPrincipalNotFoundException {
        try {
            Optional<SecurityUser> secUser = Stream.ofNullable(authentication)
                                                   .filter(auth -> auth.getClass().isAssignableFrom(UsernamePasswordAuthenticationToken.class))
                                                   .map(UsernamePasswordAuthenticationToken.class::cast)
                                                   .map(UsernamePasswordAuthenticationToken::getPrincipal)
                                                   .filter(SecurityUser.class::isInstance)
                                                   .map(SecurityUser.class::cast)
                                                   .filter(securityUser -> loginDetails.usernameOrEmail().equals(securityUser.getEmail()) || loginDetails.usernameOrEmail().equals(securityUser.getUsername()))
                                                   .findFirst();
            if (secUser.isPresent()) {
                return ResponseEntity.noContent().build();
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        } catch (UsernameNotFoundException e) {
            throw new UserPrincipalNotFoundException("Invalid username: %s or password %s".formatted(loginDetails.usernameOrEmail(), loginDetails.password()));
        }

    }
}
