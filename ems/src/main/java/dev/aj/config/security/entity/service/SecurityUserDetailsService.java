package dev.aj.config.security.entity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.config.security.dto.LoginUserDetails;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.entity.repository.SecurityUserRepository;
import dev.aj.config.security.mapper.LoginUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityUserDetailsService implements UserDetailsService, UserRegisterationService {

    private final SecurityUserRepository securityUserRepository;
    private final LoginUserMapper loginUserMapper;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return securityUserRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @SneakyThrows
    @Override
    public LoginUserDetails registerUser(LoginUserDetails loginUserDetails) {
        SecurityUser securityUser = loginUserMapper.toEntity(loginUserDetails);
        SecurityUser savedUser = securityUserRepository.save(securityUser);
        return loginUserMapper.toDto(savedUser);
    }
}
