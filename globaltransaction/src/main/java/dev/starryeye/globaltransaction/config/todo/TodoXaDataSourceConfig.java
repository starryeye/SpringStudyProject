package dev.starryeye.globaltransaction.config.todo;

import dev.starryeye.globaltransaction.config.properties.MyXaDataSources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TodoXaDataSourceConfig {

    @Bean
    public DataSource todoDataSource(MyXaDataSources myXaDataSources) {
        return myXaDataSources.getTodo();
    }

}
