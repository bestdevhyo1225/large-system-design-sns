package com.example.fastcampusmysql.application.controller.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;

public record RegisterMemberRequestDto(
	@NotBlank(message = "email을 입력하세요")
	String email,
	@NotBlank(message = "nickname을 입력하세요")
	String nickname,
	@NotNull(message = "birthday를 입력하세요")
	LocalDate birthday
) {

	public RegisterMemberCommand toCommand() {
		return new RegisterMemberCommand(email, nickname, birthday);
	}
}
