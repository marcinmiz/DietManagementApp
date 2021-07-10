package agh.edu.pl.diet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry){
            String origin = "http://localhost:3000";
            registry.addMapping("/api/**")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedOrigins(origin);
        }
    }
