package com.example.fastcampusmysql.application.usecase;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.service.PostLikeWriteService;
import com.example.fastcampusmysql.domain.post.service.PostReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatePostLikeUsecase {

	private final MemberReadService memberReadService;
	private final PostReadService postReadService;
	private final PostLikeWriteService postLikeWriteService;

	public void execute(Long postId, Long memberId) {
		PostDto postDto = postReadService.getPost(postId);
		MemberDto memberDto = memberReadService.getMember(memberId);
		postLikeWriteService.create(postDto.id(), memberDto.id());
	}
}
