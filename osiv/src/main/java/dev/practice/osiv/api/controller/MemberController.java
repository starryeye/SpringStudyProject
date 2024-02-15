package dev.practice.osiv.api.controller;

import dev.practice.osiv.api.facade.CreateMemberUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final CreateMemberUsecase createMemberUsecase;
}
