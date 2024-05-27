package dev.starryeye.httpinterfaces;

import dev.starryeye.httpinterfaces.client.UserHttpInterfaceClient;
import dev.starryeye.httpinterfaces.client.UserRestClient;
import dev.starryeye.httpinterfaces.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

//    private final UserRestClient client;
    private final UserHttpInterfaceClient client;

    @GetMapping("")
    public List<User> getUsers() {
        return client.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return client.findById(id);
    }
}
