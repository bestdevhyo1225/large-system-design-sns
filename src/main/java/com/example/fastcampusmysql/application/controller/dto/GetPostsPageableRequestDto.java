package com.example.fastcampusmysql.application.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetPostsPageableRequestDto(

	@PositiveOrZero(message = "page는 0과 같거나 큰 값을 입력하세요")
	@Schema(description = "Page 번호", example = "0", required = true)
	Integer page,

	@PositiveOrZero(message = "size는 0과 같거나 큰 값을 입력하세요")
	@Schema(description = "Page 사이즈", example = "10", required = true)
	Integer size,

	@NotBlank(message = "sort를 입력하세요")
	@Schema(description = "정렬 필드", example = "id", required = true)
	String sort,

	@NotBlank(message = "sort를 입력하세요")
	@Schema(description = "정렬 순서", example = "desc", required = true)
	String direction
) {
}
