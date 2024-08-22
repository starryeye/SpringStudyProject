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
  - JPA N+1 Query problem
- OpenFeign
  - openfeign client
- restclients
  - restclients
    - spring 6 Rest client 4가지
      - Http interface, WebClient, RestTemplate, RestClient
  - resttemplate
    - RestTemplate 심화
      - timeout, retry, errorhandle, logging
      - httpMessageConvertor (todo)
  - restclient
    - RestClient 심화
  - httpinterfaces
    - HttpInterface + RestClient 심화
- multipledatasources
  - N 개의 DB 와 1 개의 application 간 connection
  - with JPA
  - Transaction, EntityManager 등 각종 주의사항
- globaltransaction
  - N 개의 DB 와 연동된 1 개의 application 에서 분산(글로벌) 트랜잭션 처리
  - JtaTransactionManager, Atomikos
  - with JPA
- xmlresponse
  - default 로, xml format 으로 응답하는 application
- compositekey
  - JPA 복합키
- eagerandlazy
  - JPA 즉시로딩, 지연로딩
- cascadeandorphan
  - JPA 영속성 전이, 고아 객체 삭제
- fetchjoin
  - JPA 페치 조인
- pagingandsorting
  - Spring Data JPA, 페이징과 정렬
- osiv
  - Spring 과 JPA 그리고 OSIV
- event
  - event/applicationevent
    - ApplicationEvent 방식의 sync, async
  - event/genericevent
    - 제네릭 이벤트 sync, async
  - event/transactionboundevent
    - transaction bound event
  - event/coupling
    - 회원 가입과 메일 전송간의 의존성을 강 결합에서 느슨한 결합으로 만들어보기
  - to be continue.. (after study DDD)
- batch (todo)
  - H2(sequence)
  - MySQL(identity)
- valid (todo)
  - Java and Spring Validation
