package dev.starryeye.globaltransaction.config.memo;

import com.atomikos.spring.AtomikosDataSourceBean;
import dev.starryeye.globaltransaction.config.properties.MyXaDataSources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class MemoXaDataSourceConfig {

    @Primary
    @Bean
    public DataSource memoDataSource(MyXaDataSources myXaDataSources) {
        return myXaDataSources.getMemo();
    }

}
