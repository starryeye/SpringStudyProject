package dev.practice.multipledatasources.config.todo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories( // JPA repository 빈을 등록하도록 하는 어노테이션
        basePackages = "dev.practice.multipledatasources.repository.todo", // JpaRepository 스캔 시작 범위
        entityManagerFactoryRef = "todoEntityManagerFactory",
        transactionManagerRef = "todoTransactionManager" // @Transactional("todoTransactionManager")
)
public class TodoJpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean todoEntityManagerFactory(
            @Qualifier("todoDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder entityManagerFactoryBuilder
    ) {

        String[] targetpackages = {"dev.practice.multipledatasources.repository.todo"}; // entity 들이 있는 패키지

//        // EntityManagerFactory 생성
//        return entityManagerFactoryBuilder
//                .dataSource(dataSource)
//                .packages(targetpackages)
//                .build();
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPersistenceUnitName("todoPersistenceUnit"); // em 주입에 관련됨
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(targetpackages);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // application.yml 에 spring.jpa 가 안 먹혀서 직접 해줘야함..
        emf.setJpaPropertyMap(
                Map.of(
                        "hibernate.hbm2ddl.auto", "create",
                        "hibernate.format_sql", true,
                        "hibernate.show_sql", true,
                        "open-in-view", false
                )
        );

        return emf;
    }

    @Bean
    public PlatformTransactionManager todoTransactionManager(
            @Qualifier("todoEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {

        // TransactionManager 생성
        return new JpaTransactionManager(
                Objects.requireNonNull(entityManagerFactory.getObject())
        );
    }
}
