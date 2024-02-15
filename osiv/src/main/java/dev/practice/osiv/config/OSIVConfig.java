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
     * spring.jpa.open-in-view 속성을 false 로 하면 등록되지 않음
     *
     * 트랜잭션 범위와 영속성 컨텍스트 범위가 동일하도록 맞추려면 spring.jpa.open-in-view 속성을 false 로 하자..
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
