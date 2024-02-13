package dev.practice.pagingandsorting.controller;

import dev.practice.pagingandsorting.repository.Member;
import dev.practice.pagingandsorting.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public Page<Member> getMembersByName(
            @RequestParam("name")String name,
            @PageableDefault(size = 3, sort = "registrationDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        /**
         * Spring data 가 제공하는 페이징과 정렬 기능을 Spring MVC 편리하게 사용할 수 있도록
         * HandlerMethodArgumentResolver 를 제공한다.
         *
         * 페이징 : PageableHandlerMethodArgumentResolver
         * 정렬 : SortHandlerMethodArgumentResolver
         *
         * /members ? name="memberName" & page=0 & size=3 & sort=registrationDate
         */

        return memberService.findMembersByName(name, pageable);
    }
}
