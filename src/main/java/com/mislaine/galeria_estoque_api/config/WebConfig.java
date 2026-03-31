package com.mislaine.galeria_estoque_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //Define configurações globais do sistema
public class WebConfig implements WebMvcConfigurer {
    // Ao implementar essa interface, você ganha o poder de "customizar" como o Spring lida com requisições Web

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todos os endpoints da API
                .allowedOrigins("http://localhost:4200") // Permite apenas o seu Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*"); // Permite todos os cabeçalhos
    }
}