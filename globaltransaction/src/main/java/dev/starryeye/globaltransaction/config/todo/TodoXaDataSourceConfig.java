package dev.starryeye.globaltransaction.config.todo;

import com.atomikos.spring.AtomikosDataSourceBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TodoXaDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.xa.todo")
    public DataSource todoDataSourceProperties() {
        // spring.datasource.xa.todo 로 시작하는 모든 속성이 AtomikosDataSourceBean 로 바인딩된다.
        return new AtomikosDataSourceBean();
    }

}
