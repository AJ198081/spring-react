package dev.aj.config;

import java.time.Instant;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProviderImpl", modifyOnCreate = false)
public class AppConfig {

    @Bean(name = "dateTimeProviderImpl")
    public DateTimeProvider dateTimeProviderImpl() {
        return () -> Optional.of(Instant.now());
    }
}
