package dev.practice.coupling.phase1.controller;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.phase1.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterMemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/new")
    public ResponseEntity<RegisterMemberResponse> registerMember(@RequestBody RegisterMemberRequest request) {

        return ResponseEntity.ok(memberService.registerProcess(request));
    }
}
