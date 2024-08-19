package dev.starryeye.globaltransaction.config.memo;

import com.atomikos.spring.AtomikosDataSourceBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class MemoXaDataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.xa.memo")
    public DataSource memoDataSourceProperties() {
        // spring.datasource.xa.memo 로 시작하는 모든 속성이 AtomikosDataSourceBean 로 바인딩된다.
        return new AtomikosDataSourceBean();
    }

}
