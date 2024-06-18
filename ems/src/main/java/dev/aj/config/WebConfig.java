package dev.aj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@Profile("dev")
public class WebConfig implements WebMvcConfigurer {

    //    Add CORS configuration, just for 'dev' profile, no need to annotate @CrossOrigin on Controller as that takes effect in 'prod' as well
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] allowedMethods = {HttpMethod.GET.toString(), HttpMethod.POST.toString(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name()};

        registry.addMapping("/employees/**")
                .allowedOrigins(VITE_HOST)
                .allowedHeaders("*")
                .allowedMethods(allowedMethods)
                .allowCredentials(true)
                .maxAge(600);

        registry.addMapping("/departments/**")
                .allowedOrigins(VITE_HOST)
                .allowedHeaders("*")
                .allowedMethods(allowedMethods)
                .allowCredentials(true)
                .maxAge(534);

        registry.addMapping("/user/**")
                .allowedOrigins(VITE_HOST)
                .allowedHeaders("*")
                .allowedMethods(allowedMethods)
                .allowCredentials(true)
                .maxAge(534);
    }

    public static final String VITE_HOST = "http://localhost:5173";
}
