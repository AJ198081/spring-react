package dev.aj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("dev")
public class WebConfig implements WebMvcConfigurer {

//    Add CORS configuration, just for 'dev' profile, no need to annotate @CrossOrigin on Controller as that takes effect in 'prod' as well
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/employees/**")
                .allowedOrigins("http://localhost:5173")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .maxAge(600);

        registry.addMapping("/departments/**")
                .allowedOrigins("http://localhost:5173")
                .allowedHeaders("*")
                .allowedMethods(HttpMethod.GET.toString(), "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true)
                .maxAge(534);
    }
}
