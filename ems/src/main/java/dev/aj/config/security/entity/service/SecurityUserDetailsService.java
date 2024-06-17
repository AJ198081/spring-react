package dev.aj.config.security.entity.service;

import dev.aj.config.security.dto.LoginUserDetails;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.entity.repository.SecurityUserRepository;
import dev.aj.config.security.mapper.LoginUserMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityUserDetailsService implements UserDetailsService, UserRegisterationService {

    private final SecurityUserRepository securityUserRepository;
    private final LoginUserMapper loginUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return securityUserRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @SneakyThrows
    @Override
    public LoginUserDetails registerUser(LoginUserDetails loginUserDetails) {
        SecurityUser securityUser = loginUserMapper.toEntity(loginUserDetails);
        Optional<SecurityUser> optionalSecurityUser = securityUserRepository.findByUsernameOrEmail(securityUser.getUsername(), securityUser.getEmail());

        if(optionalSecurityUser.isPresent()) {
            SecurityUser secUser = optionalSecurityUser.get();
            secUser.setUsername(securityUser.getUsername());
            secUser.setName(securityUser.getName());
            secUser.setEmail(securityUser.getEmail());
            secUser.setPassword(securityUser.getPassword());
            secUser.setRoles(securityUser.getRoles());
            return loginUserMapper.toDto(securityUserRepository.saveAndFlush(secUser));
        } else {
            return loginUserMapper.toDto(securityUserRepository.save(securityUser));
        }
    }
}
