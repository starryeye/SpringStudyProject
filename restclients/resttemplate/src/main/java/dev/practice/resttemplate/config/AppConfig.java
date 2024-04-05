package dev.practice.resttemplate.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RestTemplateProperties.class)
public class AppConfig {
}
