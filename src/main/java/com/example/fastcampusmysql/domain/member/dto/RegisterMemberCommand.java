package com.example.fastcampusmysql.domain.member.dto;

import java.time.LocalDate;

import com.example.fastcampusmysql.domain.member.entity.Member;

public record RegisterMemberCommand(String email, String nickname, LocalDate birthday) {

	public Member toEntity() {
		return Member.builder().email(email).nickname(nickname).birthday(birthday).build();
	}
}
