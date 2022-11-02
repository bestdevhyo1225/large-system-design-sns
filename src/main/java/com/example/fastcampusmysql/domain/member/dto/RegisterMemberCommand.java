package com.example.fastcampusmysql.domain.member.dto;

import com.example.fastcampusmysql.domain.member.entity.Member;
import java.time.LocalDate;

public record RegisterMemberCommand(String email, String nickname, LocalDate birthday) {

    public Member toEntity() {
        return Member.builder().email(email).nickname(nickname).birthday(birthday).build();
    }
}
