package com.example.scholarship.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.scholarship.repo")
public class DatabaseConfig {
    // No @Bean methods - let Spring Boot auto-configure DataSource
}