package dev.practice.multipledatasources.config.todo;

import dev.practice.multipledatasources.repository.todo.TodoEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories( // JPA repository 빈을 등록하도록 하는 어노테이션
        basePackages = "dev.practice.multipledatasources.repository.todo", // 스캔범위
        entityManagerFactoryRef = "todosEntityManagerFactory",
        transactionManagerRef = "todosTransactionManager"
)
public class TodoJpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean todosEntityManagerFactory(
            @Qualifier("todosDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder entityManagerFactoryBuilder
    ) {

        Class<?>[] targetEntities = {TodoEntity.class};

        // EntityManagerFactory 생성
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages(targetEntities)
                .build();
    }

    @Bean
    public PlatformTransactionManager todosTransactionManager(
            @Qualifier("todosEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {

        // TransactionManager 생성
        return new JpaTransactionManager(
                Objects.requireNonNull(entityManagerFactory.getObject())
        );
    }
}
