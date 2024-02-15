package dev.practice.osiv.api.controller;

import dev.practice.osiv.api.facade.CreateMemberUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final CreateMemberUsecase createMemberUsecase;

    @PostMapping("/new")
    public void createMember(@RequestParam("name")String name) {

        createMemberUsecase.run(name);
    }
}
