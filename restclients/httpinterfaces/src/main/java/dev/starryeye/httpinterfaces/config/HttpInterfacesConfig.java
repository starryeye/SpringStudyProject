package dev.starryeye.httpinterfaces.config;

import dev.starryeye.httpinterfaces.client.UserHttpInterfaceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfacesConfig {

    @Bean
    public UserHttpInterfaceClient userHttpInterfaceClient() {

        RestClient client = RestClient.builder().baseUrl("https://jsonplaceholder.typicode.com").build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(
                RestClientAdapter.create(client)
        ).build();

        return factory.createClient(UserHttpInterfaceClient.class);
    }
}
