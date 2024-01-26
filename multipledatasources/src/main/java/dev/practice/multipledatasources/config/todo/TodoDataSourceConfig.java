package dev.practice.multipledatasources.config.todo;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class TodoDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.todos")
    public DataSourceProperties todosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.todos.hikari") // 원래 datasource.hikari.connection-timeout 으로 설정 가능
    public DataSource todosDataSource() {

        // 투두 전용 datasource 생성
        return todosDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
}
