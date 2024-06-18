package dev.aj.config.security;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public static final String ADMIN = "admin";
    public static final String SUPERUSER = "superuser";
    public static final String USER = "user";
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain addSecurityFilters(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.authorizeHttpRequests(customizer -> customizer
                                   .requestMatchers("/user/login")
                                        .permitAll()
                                   .requestMatchers(HttpMethod.GET, "/employees/**", "/departments/**")
                                        .hasAnyRole(USER, ADMIN, SUPERUSER)
                                   .requestMatchers(HttpMethod.POST, "/employees", "/departments")
                                        .hasAnyRole(ADMIN, SUPERUSER)
                                   .requestMatchers(HttpMethod.PUT, "/employees/*", "/departments/*")
                                        .hasAnyRole(USER, ADMIN, SUPERUSER)
                                   .requestMatchers(HttpMethod.POST, "/user/register")
                                        .hasRole(SUPERUSER)
                                   .anyRequest()
                                        .authenticated()
                           )
                           .cors(corsCustomizer -> {
                               CorsConfigurationSource source = request -> {
                                   CorsConfiguration configuration = new CorsConfiguration();
                                   configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                                   configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                                   configuration.setAllowedHeaders(Arrays.asList("*"));
                                   configuration.setAllowCredentials(true);
                                   return configuration;
                               };
                               corsCustomizer.configurationSource(source);
                           })
                           .csrf(customizer -> customizer.disable())
                           .httpBasic(Customizer.withDefaults())
                           .formLogin(Customizer.withDefaults())
                           .sessionManagement(customizer -> customizer.maximumSessions(1))
                           .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(@Qualifier("securityUserDetailsService") UserDetailsService userDetailsService, @Qualifier("encoder") PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        ProviderManager providerManager = new ProviderManager(Arrays.asList(daoAuthenticationProvider));
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @SneakyThrows
    @Bean
    public UserDetailsService inMemoryUserDetailsManager(@Qualifier("encoder") PasswordEncoder encoder, @Qualifier("securityUserRecords") List<SecurityUserRecord> securityUserRecords) {
        return new InMemoryUserDetailsManager(securityUserRecords.stream()
                                                                 .map(securityUserRecord -> User.withUsername(securityUserRecord.username)
                                                                                                .password(encoder.encode(securityUserRecord.password))
                                                                                                .roles(securityUserRecord.role)
                                                                                                .accountExpired(false)
                                                                                                .accountExpired(false)
                                                                                                .accountLocked(false)
                                                                                                .build())
                                                                 .toList());
    }

    @SneakyThrows
    @Bean(name = "securityUserRecords")
    public List<SecurityUserRecord> securityUserRecords() {
        InputStream securityCredentials = this.getClass().getResourceAsStream("/SecurityCredentials.json");
        return objectMapper.readValue(securityCredentials, new TypeReference<List<SecurityUserRecord>>() {
        });
    }

    public record SecurityUserRecord(String username, String password, String role, String email) {
    }
}
