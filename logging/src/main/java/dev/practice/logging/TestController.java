package dev.practice.logging;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public Response test() {

        log.info("hello world!");

        return new Response("hello", 10);
    }

    @Getter
    public static class Response {
        private String name;
        private Integer age;

        public Response(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }
}
