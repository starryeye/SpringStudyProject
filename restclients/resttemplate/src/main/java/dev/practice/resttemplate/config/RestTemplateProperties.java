package dev.practice.resttemplate.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "rest.template")
public class RestTemplateProperties {

    @NotNull
    private Integer connectionTimeout;
    @NotNull
    private Integer readTimeout;

    private Integer retryCount;
    private Integer backoff;
}
