package dev.starryeye.globaltransaction.config.properties.sub;

import com.atomikos.spring.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.sql.SQLException;

public class MyAtomikosDataSourceBean extends AtomikosDataSourceBean {

    private final DataSourceProperties xaProperties;

    @ConstructorBinding
    public MyAtomikosDataSourceBean(
            DataSourceProperties xaProperties,
            String xaDataSourceClassName,
            String resourceName,
            int borrowConnectionTimeout,
            int maxLifetime,
            int maximumPoolSize
    ) {
        this.xaProperties = xaProperties;
        this.setXaDataSourceClassName(xaDataSourceClassName);
        this.setUniqueResourceName(resourceName);
        this.setBorrowConnectionTimeout(borrowConnectionTimeout);
        this.setMaxLifetime(maxLifetime);
        this.setMaxPoolSize(maximumPoolSize);
    }

    public void mysqlInitialize() {

        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUrl(this.xaProperties.getUrl());
        xaDataSource.setUser(this.xaProperties.getUsername());
        xaDataSource.setPassword(this.xaProperties.getPassword());

        this.setXaDataSource(xaDataSource);
    }
}
