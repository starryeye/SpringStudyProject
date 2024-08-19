package dev.starryeye.globaltransaction.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    public PlatformTransactionManager jtaTransactionManager() {
        return new JtaTransactionManager();
    }

    @Bean
    public PlatformTransactionManager memoJpaTransactionManager(EntityManagerFactory memoEntityManagerFactory) {
        return new JpaTransactionManager(memoEntityManagerFactory);
    }

    @Bean
    public PlatformTransactionManager todoJpaTransactionManager(EntityManagerFactory todoEntityManagerFactory) {
        return new JpaTransactionManager(todoEntityManagerFactory);
    }
}
