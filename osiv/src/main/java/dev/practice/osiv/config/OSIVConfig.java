package dev.practice.osiv.config;

import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.orm.hibernate5.support.OpenSessionInViewInterceptor;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class OSIVConfig implements WebMvcConfigurer {

    /**
     * 아래는 JPA 와 Hibernate 에서 제공하는 OSIV 클래스이다.
     *
     * 스프링의 OSIV 방식을 따르도록 설계된 클래스이다.
     * -> OSIV 를 사용하지만, 트랜잭션은 비즈니스 계층에서만 사용한다.(@Transactional 범위)
     * -> 트랜잭션 범위와 영속성 컨텍스트 범위가 다르다.
     *
     * actuator 로 확인 시..
     * 기본적으로 OpenEntityManagerInViewInterceptor 가 등록된다.
     * -> spring.jpa.open-in-view 속성을 false 로 하면 등록되지 않음
     *
     * 트랜잭션 범위와 영속성 컨텍스트 범위가 동일하도록 맞추려면 spring.jpa.open-in-view 속성을 false 로 하자..
     */


    /**
     * 스프링 OSIV 동작 방식
     *
     * OpenEntityManagerInViewInterceptor::preHandle() 내부 코드를 보면..
     * interceptor 시작 시점에 EntityManager 를 하나 생성하는 것을 볼 수 있다. 끝날 시점엔 삭제
     * 해당 EntityManager 를 현재 요청을 처리하는 스레드에 저장(binding)해놓는다. (TransactionSynchronizationManager 를 이용)
     *
     * JpaTransactionManager::doGetTransaction() 내부 코드를 보면.
     * 그 이후, 트랜잭션이 시작될 때, EntityManager 를 생성하지 않고 현재 스레드에 저장되어있는 것을 재사용한다.
     * 그래서 OSIV 에서는 트랜잭션이 다르더라도 현재 스레드에 저장되어있는 EntityManager 를 재사용하므로 영속성컨텍스트(1차 캐시)가 공유되는 것이다.
     *
     * OSIV 를 사용하지 않으면, 트랜잭션이 생성될 때, EntityManager 를 생성하게 되고
     * 서로 다른 트랜잭션이면 영속성 컨텍스트가 달라서 공유되지 못한다.
     *
     */

    /**
     * [디버깅으로 동일한 entityManager 를 사용하는지 확인해볼 수 있다.]
     *
     * 아래는 proxy 로 생성된 entityManager 가 가지는 실제 타겟 EntityManager 를 까보는 코드이다. Debugger 에서 사용해보자.
     * ((EntityManagerProxy)entityManager).getTargetEntityManager()
     *
     * 아래 브레이킹 포인트 에서 entityManager, persistenceContext 의 주소 값을 확인해보자.
     * 1. OpenEntityManagerInViewInterceptor::preHandle(WebRequest request) 의 EntityManager em = createEntityManager();
     * 2. SimpleJpaRepository::save(S entity) 의 entityManager.persist(entity);
     *
     * persistenceContext : 1차 캐시 로 봐도 무방한듯
     */

//    @Bean
//    public FilterRegistrationBean<Filter> hibernateOsivFilter() {
//
//        // hibernate OSIV, servlet filter
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//
//        filterFilterRegistrationBean.setFilter(new OpenSessionInViewFilter());
//        filterFilterRegistrationBean.setOrder(1);
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//        return filterFilterRegistrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<Filter> JpaOeivFilter() {
//
//        // JPA OEIV, servlet filter
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//
//        filterFilterRegistrationBean.setFilter(new OpenEntityManagerInViewFilter());
//        filterFilterRegistrationBean.setOrder(1);
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//        return filterFilterRegistrationBean;
//    }
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        // hibernate OSIV, spring interceptor
//        registry.addWebRequestInterceptor(new OpenSessionInViewInterceptor())
//                .order(1)
//                .addPathPatterns("/**");
//
//        // JPA OEIV, spring interceptor
//        registry.addWebRequestInterceptor(new OpenEntityManagerInViewInterceptor())
//                .order(1)
//                .addPathPatterns("/**");
//    }
}
