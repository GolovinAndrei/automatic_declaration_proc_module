package by.btslogistics.autorelease.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsGlobalConfiguration {

    @Value("${api.prefix}")
    private String apiPrefix;

    @Value("${header.cors.origins.allow}")
    private String [] headerCorsOriginsAllow;

    @Value("${header.cors.methods.allow}")
    private String [] headerCorsMethodsAllow;

    /**
     * регистрируем шаблон адреса, на который будет настраиваться
     * обработка запросов, которые поступают с другого домена и не имеют
     * заголовока CORS.
     * По умолчанию, запросы Cross_Origin запрещены на данном сервере
     *
     * @return возрвращает объект типа WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfig() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(apiPrefix)
                        .allowedOrigins(headerCorsOriginsAllow)
                        .allowedMethods(headerCorsMethodsAllow);
            }
        };
    }
}
