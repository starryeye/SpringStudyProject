package dev.starryeye.globaltransaction.config.properties.sub;

import com.atomikos.spring.AtomikosDataSourceBean;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

public class MyAtomikosDataSourceBean extends AtomikosDataSourceBean {

    private final XaProperties xaProperties;

    @ConstructorBinding
    public MyAtomikosDataSourceBean(
            XaProperties xaProperties,
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

        initialize();
    }

    private void initialize() {
        setXaProperties(xaProperties.toProperties());
    }
}
