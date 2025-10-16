package it.softengunina.dietiestatesbackend;

import it.softengunina.dietiestatesbackend.interceptors.UsersInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    UsersInterceptor usersInterceptor;

    WebConfiguration (UsersInterceptor usersInterceptor) {
        this.usersInterceptor = usersInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(usersInterceptor)
                 .excludePathPatterns(apiDocsPath+"/**", swaggerPath+"/**")
                 .excludePathPatterns("/insertions/search", "/insertions/{id}")
                 .excludePathPatterns("/error");
    }
}
