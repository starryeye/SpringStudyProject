package dev.starryeye.httpinterfaces.client;

import dev.starryeye.httpinterfaces.dto.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface UserHttpInterfaceClient {

    @GetExchange("/users")
    List<User> findAll();

    @GetExchange("/users/{id}")
    User findById(@PathVariable("id") Integer id);
}
