package com.example.fastcampusmysql.application.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.example.fastcampusmysql.domain.post.dto.RegisterPostCommand;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterPostRequestDto(
	@Positive(message = "memberId는 0보다 큰 값을 입력하세요")
	@Schema(description = "회원번호", example = "1", required = true)
	Long memberId,
	@NotBlank(message = "contents를 입력하세요")
	@Schema(description = "게시글 내용", example = "패스트캠퍼스 예제", required = true)
	String contents
) {

	public RegisterPostCommand toCommand() {
		return new RegisterPostCommand(memberId, contents);
	}
}
