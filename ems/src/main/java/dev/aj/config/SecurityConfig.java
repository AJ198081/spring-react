package dev.aj.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    public static final String ADMIN = "admin";
    public static final String SUPERUSER = "superuser";
    public static final String USER = "user";
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain addSecurityFilters(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.authorizeHttpRequests(customizer -> customizer
                                   .requestMatchers(HttpMethod.GET, "/employees/**", "/departments/**")
                                   .hasAnyRole(USER, ADMIN, SUPERUSER)
                                   .requestMatchers(HttpMethod.POST, "/employees", "/departments")
                                   .hasAnyRole(ADMIN, SUPERUSER)
                                   .requestMatchers(HttpMethod.PUT, "/employees/*", "/departments/*")
                                   .hasAnyRole(USER, ADMIN, SUPERUSER)
                                   .requestMatchers("/", "/**")
                                   .authenticated()
                           )
                           .cors(corsCustomizer -> {
                               CorsConfigurationSource source = request -> {
                                   CorsConfiguration configuration = new CorsConfiguration();
                                   configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                                   configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
                                   configuration.setAllowedHeaders(Arrays.asList("*"));
                                   configuration.setAllowCredentials(true);
                                   return configuration;
                               };
                               corsCustomizer.configurationSource(source);
                           })
                           .csrf(customizer -> customizer.disable())
                           .httpBasic(Customizer.withDefaults())
                           .formLogin(Customizer.withDefaults())
                           .build();
    }

    @SneakyThrows
    @Bean
    public UserDetailsService inMemoryUserDetailsManager(@Qualifier("encoder") PasswordEncoder encoder) {
        InputStream securityCredentials = this.getClass().getResourceAsStream("/SecurityCredentials.json");
        List<SecurityUser> securityUsers = objectMapper.readValue(securityCredentials, new TypeReference<List<SecurityUser>>() {
        });

        return new InMemoryUserDetailsManager(securityUsers.stream()
                                                           .map(securityUser -> User.withUsername(securityUser.username)
                                                                                    .password(encoder.encode(securityUser.password))
                                                                                    .roles(securityUser.role)
                                                                                    .accountExpired(false)
                                                                                    .accountExpired(false)
                                                                                    .accountLocked(false)
                                                                                    .build())
                                                           .toList());
    }

    @Bean(name = "encoder")
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public record SecurityUser(String username, String password, String role) {
    }

}
