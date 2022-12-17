package com.example.fastcampusmysql.domain.post.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.fastcampusmysql.domain.post.entity.Post;

import lombok.Builder;

public record PostDto(Long id, Long memberId, String contents, Long likeCount, LocalDate createdDate,
					  LocalDateTime createdAt) {

	@Builder
	public static PostDto of(Post post) {
		return new PostDto(
			post.getId(),
			post.getMemberId(),
			post.getContents(),
			post.getLikeCount(),
			post.getCreatedDate(),
			post.getCreatedAt()
		);
	}
}
