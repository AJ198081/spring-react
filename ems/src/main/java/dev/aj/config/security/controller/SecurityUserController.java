package dev.aj.config.security.controller;

import dev.aj.config.security.dto.LoginDetails;
import dev.aj.config.security.dto.UserRegisterationDetails;
import dev.aj.config.security.service.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<HttpStatus> loginUser(@RequestBody LoginDetails loginDetails) {
        try {
            UserDetails userDetails = securityUserDetailsService.loadUserByUsername(loginDetails.usernameOrEmail());
            return ResponseEntity.noContent().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
