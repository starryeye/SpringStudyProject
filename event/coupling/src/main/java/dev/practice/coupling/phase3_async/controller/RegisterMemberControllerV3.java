package dev.practice.coupling.phase3_async.controller;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.phase3_async.service.MemberServiceV3;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterMemberControllerV3 {

    private final MemberServiceV3 memberServiceV3;

    @PostMapping("/api/v1/phase3/new")
    public ResponseEntity<RegisterMemberResponse> registerMember(@RequestBody RegisterMemberRequest request) {

        return ResponseEntity.ok(memberServiceV3.registerProcess(request));
    }
}
