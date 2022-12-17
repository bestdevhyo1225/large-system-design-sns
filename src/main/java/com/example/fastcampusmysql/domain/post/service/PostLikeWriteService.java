package com.example.fastcampusmysql.domain.post.service;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.entity.PostLike;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeWriteService {

	private final PostLikeRepository postLikeRepository;

	public Long create(Long postId, Long memberId) {
		PostLike postLike = PostLike.builder()
			.postId(postId)
			.memberId(memberId)
			.build();

		return postLikeRepository.save(postLike).getId();
	}
}
