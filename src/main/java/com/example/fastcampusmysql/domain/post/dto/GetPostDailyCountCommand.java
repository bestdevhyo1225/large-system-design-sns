package com.example.fastcampusmysql.domain.post.dto;

import java.time.LocalDate;

public record GetPostDailyCountCommand(Long memberId, LocalDate firstDate, LocalDate lastDate) {
}
