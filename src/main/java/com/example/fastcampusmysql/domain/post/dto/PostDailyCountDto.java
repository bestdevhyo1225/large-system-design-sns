package com.example.fastcampusmysql.domain.post.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record PostDailyCountDto(Long memberId, LocalDate date, Long postCount) {
}
