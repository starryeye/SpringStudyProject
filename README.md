# SpringStudyProject
  
Spring & JPA Study

## 스프링
  
subject  
- Spring Framework
- Spring Boot
- Spring MVC
- Spring Validation
- Spring Transaction
- Spring AOP
- Spring Cloud OpenFeign
- Thymeleaf
- Mustache


## 스프링 DB 연동
  
subject  
- Jdbc
- JdbcTemplate
- MyBatis
- Jpa
- Spring Data Jpa
- QueryDsl


## 기타
  
- Prometheus
- Grafana


## Projects
선택적으로 복습하며 코드와 공부한 주제를 매핑한다.  
다음 복습에서는 더욱 구체적인 키워드로 적어보자.  
  
- core
  - Spring Framwork
    - OCP, DIP 를 만족하는 스프링(IOC, DI) 컨테이너
    - 스프링 빈
- servlet, item-service
  - Spring MVC
    - 서블릿, 서블릿 컨테이너
    - SpringMVC 구조
      - DispatcherServlet, HandlerMapping, HandlerAdapter, viewResolver
      - ArgumentResolver, ReturnValueHandler, HttpMessageConverter
    - Controller
- validation
  - Spring MVC
    - BindingResult, MessageCodesResolver
    - validator, WebDataBinder
    - Bean Validation
- login
  - Spring MVC
    - HttpSession, Filter, HandlerInterceptor
    - preHandle, HandlerAdapter, postHandle, afterCompletion
    - ArgumentResolver
- exception
  - Spring MVC
    - HandlerExceptionResolver
    - ControllerAdvice
- typeconverter
  - Spring Framwork
    - Converter, ConversionService
    - Formatter
- jdbc
  - Jdbc, Spring Framework
    - DataManager, DataSource, HikariDataSource, DriverManagerDataSource
    - PlatformTransactionManager, TransactionSynchronizationManager
    - TransactionTemplate
    - Transaction AOP
    - ExceptionTranslator, DataAccessException
- itemservice-db
  - JdbcTemplate, MyBatis, JPA, Spring Data JPA, QueryDsl
- springtx
  - Spring Framwork
    - Transaction AOP
    - Transaction Propagation
- advanced, proxy, aop
  - Spring AOP
    - point cut, advice, advisor, BeanPostProcessor
    - Jdk dynamic proxy, CGLIB
- jpashopbegin, jpashop, data-jpa, querydsl
  - Jpa, Spring Data Jpa, QueryDsl

## sub projects
- logging
  - spring boot + log4j2
  - Pattern Layout, Json Layout, JsonTemplate Layout
- nplusone
  - JPA N+1 problem
- OpenFeign
  - openfeign client
- restclients
  - spring 6 client
    - Http interface, WebClient, RestTemplate
    - todo, RestClient
- multipledatasources
  - N 개의 DB 와 1 개의 application 간 connect
- xmlresponse
  - default 로, xml format 으로 응답하는 application
