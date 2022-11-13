package com.example.fastcampusmysql.domain.post.dto;

import com.example.fastcampusmysql.domain.post.entity.PostTimeline;

import lombok.Builder;

public record PostTimelineDto(Long id, Long memberId, Long postId) {

	@Builder
	public static PostTimelineDto of(PostTimeline postTimeline) {
		return new PostTimelineDto(postTimeline.getId(), postTimeline.getMemberId(), postTimeline.getPostId());
	}
}
