package dev.starryeye.globaltransaction.config.memo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "memoEntityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = "dev.starryeye.globaltransaction.domain.memo"
)
public class MemoJpaConfig {

    // 필요하다면 application.yml 로 빼도록
    private Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        jpaProperties.put("javax.persistence.transactionType", "JTA");
        return jpaProperties;
    }

    @Primary
    @Bean
    public EntityManagerFactoryBuilder memoApplicationEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties(), null
        );
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean memoEntityManagerFactory(
            @Qualifier("memoApplicationEntityManagerFactoryBuilder") EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("memoDataSource") DataSource dataSource
    ) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("dev.starryeye.globaltransaction.domain.memo")
                .persistenceUnit("mysql")
                .properties(jpaProperties())
                .jta(true)
                .build();
    }
}
