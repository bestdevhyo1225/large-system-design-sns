package com.example.fastcampusmysql.application.controller.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.fastcampusmysql.domain.post.dto.GetPostDailyCountCommand;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetPostDailyCountRequestDto(
	@Positive(message = "memberId는 0보다 큰 값을 입력하세요")
	@Schema(description = "회원번호", example = "1", required = true)
	Long memberId,

	@NotNull(message = "firstDate를 입력하세요")
	// @RequestBody 처리되어야 하는 상황이면 type은 string 타입으로 정의해야 한다.
	@Schema(description = "시작일", type = "LocalDate", example = "2022-11-12", required = true)
	// @RequestBody 처리되어야 하는 상황이면 @JsonFormat을 사용해야 한다.
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate firstDate,

	@NotNull(message = "lastDate를 입력하세요")
	// @RequestBody 처리되어야 하는 상황이면 type은 string 타입으로 정의해야 한다.
	@Schema(description = "종료일", type = "LocalDate", example = "2032-11-12", required = true)
	// @RequestBody 처리되어야 하는 상황이면 @JsonFormat을 사용해야 한다.
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate lastDate
) {

	public GetPostDailyCountCommand toCommand() {
		return new GetPostDailyCountCommand(memberId, firstDate, lastDate);
	}
}
