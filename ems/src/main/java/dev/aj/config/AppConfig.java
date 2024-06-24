package dev.aj.config;

import dev.aj.config.security.entity.SecurityUser;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProviderImpl", modifyOnCreate = false, auditorAwareRef = "auditorAwareRef")
public class AppConfig {

    @Bean(name = "dateTimeProviderImpl")
    public DateTimeProvider dateTimeProviderImpl() {
        return () -> Optional.of(Instant.now());
    }

    @Bean(name = "auditorAwareRef")
    @Lazy
    public AuditorAware<String> auditorAware() {
        return Stream.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                     .map(Authentication::getPrincipal)
                     .filter(SecurityUser.class::isInstance)
                     .map(SecurityUser.class::cast)
                     .findFirst()
                     .<AuditorAware<String>>map(securityUser -> () -> Optional.of(securityUser.getEmail()))
                     .orElseGet(() -> () -> Optional.of("admin"));
    }


    @Bean(name = "encoder")
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
