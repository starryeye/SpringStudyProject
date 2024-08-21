package dev.starryeye.globaltransaction.config.todo;

import dev.starryeye.globaltransaction.config.AtomikosConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "todoEntityManagerFactory",
        transactionManagerRef = AtomikosConfiguration.TRANSACTION_MANAGER_BEAN_NAME,
        basePackages = "dev.starryeye.globaltransaction.domain.todo"
)
public class TodoJpaConfig {

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

    @Bean
    public EntityManagerFactoryBuilder todoApplicationEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties(), null
        );
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean todoEntityManagerFactory(
            @Qualifier("todoApplicationEntityManagerFactoryBuilder") EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("todoDataSource") DataSource dataSource
    ) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("dev.starryeye.globaltransaction.domain.todo")
                .persistenceUnit("todoPersistenceUnit") // PersistenceUnitName
                .properties(jpaProperties())
                .jta(true)
                .build();
    }
}
