package dev.practice.xmlresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * https://www.baeldung.com/spring-boot-customize-jackson-objectmapper
     *
     * 기본 제공되는 MappingJackson2HttpMessageConverter 말고 사용자가 직접 만들어준다.
     *
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
            Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder // 스프링은 원래 얘를 가지고 만든다. 그래서 옵션을 그대로 이어받을 수 있음
    ) {
        // 사용자가 원하는 옵션 추가
        jackson2ObjectMapperBuilder
                .featuresToEnable(SerializationFeature.WRAP_ROOT_VALUE)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));

        return new MappingJackson2HttpMessageConverter(jackson2ObjectMapperBuilder.build());
    }

    /**
     * client 가 어떤 데이터 포맷으로 받을지.. 요청 데이터에 포함시키는 방법은 총 3가지 이다.
     * 1. (Deprecated) Using URL suffixes (extensions) in the request (eg .xml/.json)
     * 2. Using URL Query parameter in the request (eg ?format=json)
     * 3. Using Accept header in the request
     *
     * 아래는 HttpMessageConverter 의 우선 순위와 상관 없이 정책을 정하는 방법인듯..
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false) // 기본 값 false, Query param 으로 format=xml 등으로 들어오면 해당 포맷으로 처리 (활성화 true)
                .ignoreAcceptHeader(false) // 기본 값 false, accept 헤더를 이용하여 포맷을 정한다. (활성화 false)
                .defaultContentType(MediaType.APPLICATION_XML) // 기본 제공 포멧을 정한다. (Spring 기본 제공 포멧은 Json 이다.)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("========================================================================");
        converters.forEach(converter -> log.info("converter = {}", converter));
        log.info("========================================================================");

        // HttpMessageConverter 우선순위 변경은 아래와 같이 처리하면 된다.
//        for (HttpMessageConverter<?> converter : converters) {
//            if(converter instanceof MappingJackson2HttpMessageConverter)) {
//                converters.remove(converter);
//                break;
//            }
//        }
//
//        converters.add(new MappingJackson2HttpMessageConverter());
//
//        log.info("========================================================================");
//        converters.forEach(converter -> log.info("converter = {}", converter));
//        log.info("========================================================================");
    }
}
