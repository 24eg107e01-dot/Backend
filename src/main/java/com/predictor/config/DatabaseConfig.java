package com.predictor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
@Slf4j
public class DatabaseConfig {

    @Value("${spring.datasource.url:jdbc:postgresql://dpg-db74440u01pc73c6tkvg-a:5432/project_9hb8}")
    private String defaultUrl;

    @Value("${spring.datasource.username:project_9hb8_user}")
    private String defaultUsername;

    @Value("${spring.datasource.password:hmvvuSYq2MVNGPSRFjDxmQJrgmlkyPZc}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null || dbUrl.isBlank()) {
            dbUrl = System.getenv("SPRING_DATASOURCE_URL");
        }
        if (dbUrl == null || dbUrl.isBlank()) {
            dbUrl = defaultUrl;
        }

        String username = System.getenv("SPRING_DATASOURCE_USERNAME");
        if (username == null || username.isBlank()) {
            username = defaultUsername;
        }

        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
        if (password == null || password.isBlank()) {
            password = defaultPassword;
        }

        // Parse Render postgresql:// or postgres:// URI format into JDBC
        if (dbUrl.startsWith("postgres://") || dbUrl.startsWith("postgresql://")) {
            try {
                String cleanUri = dbUrl.replace("postgres://", "http://").replace("postgresql://", "http://");
                URI uri = new URI(cleanUri);
                if (uri.getUserInfo() != null) {
                    String[] parts = uri.getUserInfo().split(":");
                    username = parts[0];
                    if (parts.length > 1) {
                        password = parts[1];
                    }
                }
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String host = uri.getHost();
                String path = uri.getPath();
                dbUrl = "jdbc:postgresql://" + host + ":" + port + path;
                log.info("Converted Render DATABASE_URL to JDBC: {}", dbUrl);
            } catch (Exception e) {
                log.warn("Error parsing DATABASE_URL URI, using as-is", e);
            }
        }

        // Test connectivity to PostgreSQL (handles Render internal host vs local fallback)
        boolean postgresReachable = false;
        try {
            Class.forName("org.postgresql.Driver");
            DriverManager.setLoginTimeout(3);
            try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
                postgresReachable = true;
                log.info("Successfully connected to Render PostgreSQL database: {}", dbUrl);
            }
        } catch (Exception e) {
            log.warn("PostgreSQL at {} unreachable ({}), checking environment...", dbUrl, e.getMessage());
        }

        if (postgresReachable) {
            return DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url(dbUrl)
                    .username(username)
                    .password(password)
                    .build();
        }

        // If running in Render production, proceed with PostgreSQL configuration
        if (System.getenv("RENDER") != null || System.getenv("PORT") != null || dbUrl.contains("dpg-")) {
            log.info("Deploying with PostgreSQL configuration: {}", dbUrl);
            return DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url(dbUrl)
                    .username(username)
                    .password(password)
                    .build();
        }

        // Local development zero-setup fallback
        log.info("Local environment: using embedded H2 database fallback.");
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:studentdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL")
                .username("sa")
                .password("")
                .build();
    }
}
