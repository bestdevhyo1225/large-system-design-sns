package com.example.fastcampusmysql.domain.post.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

	private final Long id;

	private final Long memberId;

	private final String contents;

	private final LocalDate createdDate;

	private final LocalDateTime createdAt;
	private Long likeCount;

	@Builder
	public Post(Long id, Long memberId, String contents, Long likeCount, LocalDate createdDate,
		LocalDateTime createdAt) {
		this.id = id;
		this.memberId = Objects.requireNonNull(memberId);
		this.contents = Objects.requireNonNull(contents);
		this.likeCount = likeCount == null ? 0 : likeCount;
		this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
		this.createdAt = createdAt == null ? LocalDateTime.now().withNano(0) : createdAt;
	}

	public void increaseLikeCountOne() {
		increaseLikeCount(1L);
	}

	public void increaseLikeCount(Long value) {
		if (value <= 0) {
			return;
		}
		this.likeCount += value;
	}
}
