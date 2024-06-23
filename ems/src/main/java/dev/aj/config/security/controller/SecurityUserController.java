package dev.aj.config.security.controller;

import dev.aj.config.security.dto.JwtAuthenticationResponse;
import dev.aj.config.security.dto.LoginDetails;
import dev.aj.config.security.dto.UserRegisterationDetails;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.service.SecurityUserDetailsService;
import dev.aj.config.security.util.JwtTokenService;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class SecurityUserController {

    private final SecurityUserDetailsService securityUserDetailsService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterationDetails> registerUser(@RequestBody UserRegisterationDetails user) {
        return ResponseEntity.ok(securityUserDetailsService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody LoginDetails loginDetails, Authentication authentication) throws UserPrincipalNotFoundException {
        try {
            if (securityUserOptional(loginDetails, authentication).isPresent()) {
                return ResponseEntity.noContent().build();
            } else {
                UserDetails userDetails = securityUserDetailsService.loadUserByUsername(loginDetails.usernameOrEmail());
                log.debug("User found: {}", userDetails.getUsername());
                return ResponseEntity.noContent().build();
            }

        } catch (UsernameNotFoundException e) {
            throw new UserPrincipalNotFoundException("Invalid username: %s or password %s".formatted(loginDetails.usernameOrEmail(), loginDetails.password()));
        }
    }

    @PostMapping("/jwt_login")
    public ResponseEntity<JwtAuthenticationResponse> jwtLogin(@RequestBody LoginDetails loginDetails, Authentication authentication) throws UserPrincipalNotFoundException {
        try {
            Optional<SecurityUser> securityUserOptional = securityUserOptional(loginDetails, authentication);
            if (securityUserOptional.isPresent()) {
                JwtAuthenticationResponse jwtAuthenticationResponse = getJwtAuthenticationResponse(authentication);
                return ResponseEntity.ok()
                                     .header(HttpHeaders.SET_COOKIE, getAccessTokenCookie(jwtAuthenticationResponse).toString())
                                     .body(jwtAuthenticationResponse);
            } else {
                securityUserDetailsService.loadUserByUsername(loginDetails.usernameOrEmail());
                JwtAuthenticationResponse jwtAuthenticationResponse = getJwtAuthenticationResponse(SecurityContextHolder.getContext().getAuthentication());
                return ResponseEntity.ok()
                                     .header(HttpHeaders.SET_COOKIE, getAccessTokenCookie(jwtAuthenticationResponse).toString())
                                     .body(jwtAuthenticationResponse);
            }
        } catch (UsernameNotFoundException e) {
            throw new UserPrincipalNotFoundException("Invalid username: %s or password %s".formatted(loginDetails.usernameOrEmail(), loginDetails.password()));
        }
    }

    private ResponseCookie getAccessTokenCookie(JwtAuthenticationResponse jwtAuthenticationResponse) {
        return ResponseCookie.from("accessToken", jwtAuthenticationResponse.getAccessToken())
                             .httpOnly(true)
                             .secure(false)
                             .path("/")
                             .maxAge(3600)
                             .build();
    }

    private Optional<SecurityUser> securityUserOptional(@RequestBody LoginDetails loginDetails, Authentication authentication) {
        return Stream.ofNullable(authentication)
                     .filter(auth -> auth.getClass().isAssignableFrom(UsernamePasswordAuthenticationToken.class))
                     .map(UsernamePasswordAuthenticationToken.class::cast)
                     .map(UsernamePasswordAuthenticationToken::getPrincipal)
                     .filter(SecurityUser.class::isInstance)
                     .map(SecurityUser.class::cast)
                     .filter(securityUser -> loginDetails.usernameOrEmail().equals(securityUser.getEmail()) || loginDetails.usernameOrEmail().equals(securityUser.getUsername()))
                     .findFirst();
    }

    private JwtAuthenticationResponse getJwtAuthenticationResponse(Authentication authentication) {
        return JwtAuthenticationResponse.builder()
                                        .accessToken(jwtTokenService.generateToken(authentication))
                                        .tokenType("Bearer")
                                        .build();
    }
}
