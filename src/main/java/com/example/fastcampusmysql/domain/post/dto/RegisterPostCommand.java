package com.example.fastcampusmysql.domain.post.dto;

import com.example.fastcampusmysql.domain.post.entity.Post;

public record RegisterPostCommand(Long memberId, String contents) {

	public Post toEntity() {
		return Post.builder().memberId(memberId).contents(contents).build();
	}
}
