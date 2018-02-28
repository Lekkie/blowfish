package com.avantir.blowfish.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by lekanomotayo on 26/02/2018.
 */

@Configuration
public class MigrationConfig {

    /**
     * Override default flyway initializer to do nothing
     */
    @Bean
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        flyway.setInitOnMigrate(true);
        return new FlywayMigrationInitializer(flyway, (f) ->{} );
    }


    /**
     * Create a second flyway initializer to run after jpa has created the schema
     */
    @Bean
    @DependsOn("entityManagerFactory")
    public FlywayMigrationInitializer delayedFlywayInitializer(Flyway flyway) {
        flyway.setInitOnMigrate(true);
        return new FlywayMigrationInitializer(flyway, null);
    }

}
