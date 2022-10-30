package com.example.fastcampusmysql.domain.member.dto;

import com.example.fastcampusmysql.domain.member.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RegisterMemberCommand(String email, String nickname, LocalDate birthday) {

    public Member toEntity() {
        return Member.of(null, email, nickname, birthday, LocalDateTime.now().withNano(0));
    }
}
