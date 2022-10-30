package com.example.fastcampusmysql.controller;

import com.example.fastcampusmysql.controller.request.RegisterMemberRequest;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    public final MemberWriteService memberWriteService;

    @PostMapping
    public Member register(@RequestBody RegisterMemberRequest request) {
        return memberWriteService.create(request.toCommand());
    }
}
