package com.example.restclients.config;

import com.example.restclients.httpinterface.MyHttpInterfaceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient myWebClient() {
        return WebClient.create("https://open.er-api.com");
    }
}
