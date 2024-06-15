package dev.aj;

import dev.aj.config.SecurityConfig;
import dev.aj.config.entity.Role;
import dev.aj.config.entity.SecurityUser;
import dev.aj.config.entity.repository.RoleRepository;
import dev.aj.config.entity.repository.SecurityUserRepository;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class EmsApp {

    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(EmsApp.class, args);
    }

    @PostConstruct
    public void createUsersInDB() {

        if (securityUserRepository.count() == 0) {
            List<SecurityConfig.SecurityUserRecord> securityUserRecords = (List<SecurityConfig.SecurityUserRecord>) applicationContext.getBean("securityUserRecords");
            PasswordEncoder encoder = applicationContext.getBean("encoder", PasswordEncoder.class);

            Set<SecurityUser> securityUsers = securityUserRecords.stream()
                                                                 .map(securityUserRecord -> {

                                                                     SecurityUser securityUser = SecurityUser.builder()
                                                                                                             .username(securityUserRecord.username())
                                                                                                             .password(encoder.encode(securityUserRecord.password()))
                                                                                                             .email(securityUserRecord.email())
                                                                                                             .roles(new HashSet<>())
                                                                                                             .build();

                                                                     Role role = roleRepository.findByName(securityUserRecord.role())
                                                                                               .orElseGet(() -> Role.builder()
                                                                                                                    .name(securityUserRecord.role())
                                                                                                                    .securityUsers(new HashSet<>())
                                                                                                                    .build());
                                                                     role.addSecurityUsers(securityUser);
                                                                     securityUser.addRole(role);

                                                                     return securityUser;
                                                                 }).collect(Collectors.toSet());

            securityUserRepository.saveAllAndFlush(securityUsers);

        }

    }
}