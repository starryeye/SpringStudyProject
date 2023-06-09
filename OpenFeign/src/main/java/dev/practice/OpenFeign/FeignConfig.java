package dev.practice.OpenFeign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"dev.practice.OpenFeign.adapter.out.web"})
public class FeignConfig {
}
