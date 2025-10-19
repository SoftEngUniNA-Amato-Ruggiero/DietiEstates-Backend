package it.softengunina.dietiestatesbackend;

import it.softengunina.dietiestatesbackend.interceptors.AgentInterceptor;
import it.softengunina.dietiestatesbackend.interceptors.ManagerInterceptor;
import it.softengunina.dietiestatesbackend.interceptors.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    UserInterceptor userInterceptor;
    AgentInterceptor agentInterceptor;
    ManagerInterceptor managerInterceptor;

    WebConfiguration(UserInterceptor userInterceptor,
            AgentInterceptor agentInterceptor,
            ManagerInterceptor managerInterceptor) {
        this.userInterceptor = userInterceptor;
        this.agentInterceptor = agentInterceptor;
        this.managerInterceptor = managerInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/me", "/agencies",
                        "/saved-searches", "/saved-searches/**",
                        "/notification-preferences", "/notification-preferences/**");

        registry.addInterceptor(agentInterceptor)
                .addPathPatterns("/insertions", "/insertions/**");

        registry.addInterceptor(managerInterceptor)
                .addPathPatterns("/agents", "/managers");
    }
}
