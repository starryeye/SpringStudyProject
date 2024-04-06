package dev.practice.resttemplate.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableConfigurationProperties(RestTemplateProperties.class)
@EnableRetry
public class AppConfig {
}
