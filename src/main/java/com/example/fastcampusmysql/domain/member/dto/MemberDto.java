package com.example.fastcampusmysql.domain.member.dto;

import java.time.LocalDate;

import com.example.fastcampusmysql.domain.member.entity.Member;

import lombok.Builder;

public record MemberDto(Long id, String email, String nickname, String grade, LocalDate birthday) {

	@Builder
	public static MemberDto of(Member member) {
		return new MemberDto(member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getGrade(),
			member.getBirthday());
	}
}
