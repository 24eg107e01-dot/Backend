package com.predictor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Slf4j
public class DatabaseConfig {

    @Value("${spring.datasource.url:jdbc:postgresql://dpg-db74440u01pc73c6tkvg-a.oregon-postgres.render.com:5432/project_9hb8?sslmode=require}")
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
                
                // If Render short internal host (dpg-xxxx), expand to public DNS to prevent UnknownHostException
                if (host != null && host.startsWith("dpg-") && !host.contains(".")) {
                    host = host + ".oregon-postgres.render.com";
                }

                String path = uri.getPath();
                dbUrl = "jdbc:postgresql://" + host + ":" + port + path + "?sslmode=require";
                log.info("Converted Render URI to JDBC with external DNS: {}", dbUrl);
            } catch (Exception e) {
                log.warn("Error parsing DATABASE_URL URI, using as-is", e);
            }
        } else if (dbUrl.contains("dpg-") && !dbUrl.contains(".render.com")) {
            // Fix short hostname inside JDBC url if passed directly
            dbUrl = dbUrl.replaceFirst("(dpg-[a-zA-Z0-9]+)(:|/)", "$1.oregon-postgres.render.com$2");
            if (!dbUrl.contains("sslmode=")) {
                dbUrl += (dbUrl.contains("?") ? "&" : "?") + "sslmode=require";
            }
            log.info("Auto-expanded short Render database host to: {}", dbUrl);
        }

        log.info("Configuring PostgreSQL DataSource with URL: {} for user: {}", dbUrl, username);

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(dbUrl)
                .username(username)
                .password(password)
                .build();
    }
}
