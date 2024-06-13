package dev.aj.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

//@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain addSecurityFilters(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.formLogin(Customizer.withDefaults())
                           .authorizeHttpRequests(customizer -> customizer
                                   .requestMatchers(HttpMethod.POST, "/employees", "/departments")
                                   .hasAnyAuthority("admin", "superuser")
                                   .requestMatchers(HttpMethod.PUT, "/employees/*", "/departments/*")
                                   .hasAnyAuthority("admin", "superuser")
                                   .requestMatchers(HttpMethod.GET, "/employees/**", "/departments/**")
                                   .hasAnyAuthority("user", "visitor", "admin", "superuser")
                                   .requestMatchers("/**").authenticated()
                           )
                           .cors(corsCustomizer -> {
                               CorsConfigurationSource source = request -> {
                                   CorsConfiguration configuration = new CorsConfiguration();
                                   configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:8080"));
                                   configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
                                   configuration.setAllowedHeaders(Arrays.asList("*"));
                                   configuration.setAllowCredentials(true);
                                   return configuration;
                               };
                               corsCustomizer.configurationSource(source);
                           })
                           .build();
    }

    @SneakyThrows
    @Bean
    public UserDetailsService inMemoryUserDetailsManager(@Qualifier("encoder") PasswordEncoder encoder) {
        InputStream securityCredentials = this.getClass().getResourceAsStream("/SecurityCredentials.json");
        List<SecurityUser> securityUsers = objectMapper.readValue(securityCredentials, new TypeReference<List<SecurityUser>>() {
        });

        return new InMemoryUserDetailsManager(securityUsers.stream()
                                                           .map(user -> User.withUsername(user.username)
                                                                            .password(encoder.encode(user.password))
                                                                            .authorities(List.of(new SimpleGrantedAuthority(user.authority)))
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

    public record SecurityUser(String username, String password, String authority) {
    }

}
