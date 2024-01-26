package dev.practice.multipledatasources.config.memo;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackageClasses = MemoEntity.class, // JpaRepository 스캔 시작 범위
        entityManagerFactoryRef = "memosEntityManagerFactory",
        transactionManagerRef = "memosTransactionManager"
)
public class MemoJpaConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean memosEntityManagerFactory(
            @Qualifier("memosDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder entityManagerFactoryBuilder
    ) {

        Class<?>[] targetEntities = {MemoEntity.class};

        // EntityManagerFactory 생성
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages(targetEntities)
                .build();
    }

    @Bean
    public PlatformTransactionManager memosTransactionManager(
            @Qualifier("memosEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {

        // TransactionManager 생성
        return new JpaTransactionManager(
                Objects.requireNonNull(entityManagerFactory.getObject())
        );
    }
}
