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
    @ConfigurationProperties("spring.datasource.memo-datasource")
    public DataSourceProperties memoDataSourceProperties() {
        // spring.datasource.memo-datasource 로 시작하는 모든 속성이 DataSourceProperties 로 바인딩된다.
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.memo-datasource.hikari")
    public DataSource memoDataSource() {

        // memo 전용 datasource 생성
        return memoDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
}
