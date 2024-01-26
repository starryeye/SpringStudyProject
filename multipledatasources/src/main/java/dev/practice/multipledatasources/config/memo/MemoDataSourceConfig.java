package dev.practice.multipledatasources.config.memo;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class MemoDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.memos")
    public DataSourceProperties memosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.memos.hikari")
    public DataSource memosDataSource() {

        // memo 전용 datasource 생성
        return memosDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
}
