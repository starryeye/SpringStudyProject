package dev.practice.coupling.phase2_transaction_separation.controller;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.phase2_transaction_separation.RegisterMemberFacadeV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterMemberControllerV2 {

    private final RegisterMemberFacadeV2 registerMemberFacadeV2;

    @PostMapping("/api/v1/phase2/new")
    public ResponseEntity<RegisterMemberResponse> registerMember(@RequestBody RegisterMemberRequest request) {

        return ResponseEntity.ok(registerMemberFacadeV2.execute(request));
    }
}
