package dev.starryeye.globaltransaction.config.properties.sub;

import com.atomikos.spring.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

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

    public AtomikosDataSourceBean createAtomikosDataSource() {

        /**
         * 해당 인스턴스(this) 에 설정을 완료하여 직접 리턴해주면 좋겠지만.. application 부팅 순서상 오류가 나는듯 하여..
         * 새로 만들어서 리턴해준다.
         */

        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUrl(this.xaProperties.getUrl());
        xaDataSource.setUser(this.xaProperties.getUsername());
        xaDataSource.setPassword(this.xaProperties.getPassword());

        return getAtomikosDataSourceBean(xaDataSource);
    }

    private AtomikosDataSourceBean getAtomikosDataSourceBean(MysqlXADataSource xaDataSource) {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(xaDataSource);
        atomikosDataSourceBean.setXaDataSourceClassName(this.getXaDataSourceClassName());
        atomikosDataSourceBean.setUniqueResourceName(this.getUniqueResourceName());
        atomikosDataSourceBean.setBorrowConnectionTimeout(this.getBorrowConnectionTimeout());
        atomikosDataSourceBean.setMaxLifetime(this.getMaxLifetime());
        atomikosDataSourceBean.setMaxPoolSize(this.getMaxPoolSize());
        return atomikosDataSourceBean;
    }
}
