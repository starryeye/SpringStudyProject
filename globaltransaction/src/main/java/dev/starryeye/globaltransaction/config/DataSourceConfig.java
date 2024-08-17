package dev.starryeye.globaltransaction.config;

import jakarta.transaction.TransactionManager;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.memo-datasource")
    public DataSourceProperties memoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource memoDataSource() {
        return memoDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.todo-datasource")
    public DataSourceProperties todoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource todoDataSource() {
        return todoDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(TransactionManager transactionManager) {
        return new JtaTransactionManager(transactionManager);
    }
}
