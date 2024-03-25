package dev.practice.logging;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public Response test() {

        ThreadContext.put("test", "controller");
        log.info("hello world!");
        ThreadContext.remove("test");

        MyThreadContext.putValue("test2", 2);
        log.info("hello integer!");
        MyThreadContext.remove("test2");


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
