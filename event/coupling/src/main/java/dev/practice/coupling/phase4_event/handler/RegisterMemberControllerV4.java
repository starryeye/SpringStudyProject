package dev.practice.coupling.phase4_event.handler;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.phase4_event.service.MemberServiceV4;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterMemberControllerV4 {

    private final MemberServiceV4 memberServiceV4;

    @PostMapping("/api/v1/phase4/new")
    public ResponseEntity<RegisterMemberResponse> registerMember(@RequestBody RegisterMemberRequest request) {

        return ResponseEntity.ok(memberServiceV4.registerProcess(request));
    }
}
