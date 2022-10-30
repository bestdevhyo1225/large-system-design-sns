package com.example.fastcampusmysql.controller.request;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import java.time.LocalDate;

public record RegisterMemberRequest(String email, String nickname, LocalDate birthday) {

    public RegisterMemberCommand toCommand() {
        return new RegisterMemberCommand(email, nickname, birthday);
    }
}
