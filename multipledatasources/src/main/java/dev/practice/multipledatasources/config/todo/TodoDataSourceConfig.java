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
    @ConfigurationProperties("spring.datasource.todo-datasource")
    public DataSourceProperties todoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.todo-datasource.hikari") // 원래 datasource.hikari.connection-timeout 으로 설정 가능
    public DataSource todoDataSource() {

        // 투두 전용 datasource 생성
        return todoDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
}
