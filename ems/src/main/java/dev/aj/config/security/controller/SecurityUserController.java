package dev.aj.config.security.controller;

import dev.aj.config.security.dto.LoginUserDetails;
import dev.aj.config.security.entity.service.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LoginUserDetails> registerUser(@RequestBody LoginUserDetails user) {
        return ResponseEntity.ok(securityUserDetailsService.registerUser(user));
    }
}
