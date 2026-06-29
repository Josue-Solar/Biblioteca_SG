package com.biblioteca.libro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.biblioteca.libro.repository") // Asegúrate de que la ruta coincida con la de tus repositorios
public class JpaConfig {
    // Clase vacía, solo sirve para guardar las configuraciones de BD
}
