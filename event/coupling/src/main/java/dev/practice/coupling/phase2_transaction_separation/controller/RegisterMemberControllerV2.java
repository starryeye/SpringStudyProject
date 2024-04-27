package dev.practice.coupling.phase2_transaction_separation.controller;

import dev.practice.coupling.phase2_transaction_separation.RegisterMemberFacadeV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterMemberControllerV2 {

    private final RegisterMemberFacadeV2 registerMemberFacadeV2;
}
