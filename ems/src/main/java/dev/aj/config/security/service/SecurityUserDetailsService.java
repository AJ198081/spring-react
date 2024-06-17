package dev.aj.config.security.service;

import dev.aj.config.security.dto.UserRegisterationDetails;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.mapper.UserRegistrationMapper;
import dev.aj.config.security.repository.SecurityUserRepository;
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
    private final UserRegistrationMapper loginUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return securityUserRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @SneakyThrows
    @Override
    public UserRegisterationDetails registerUser(UserRegisterationDetails userRegisterationDetails) {
        SecurityUser securityUser = loginUserMapper.toEntity(userRegisterationDetails);
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
