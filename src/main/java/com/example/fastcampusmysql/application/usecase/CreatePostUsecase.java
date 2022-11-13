package com.example.fastcampusmysql.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.dto.RegisterPostCommand;
import com.example.fastcampusmysql.domain.post.service.PostTimelineWriteService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatePostUsecase {

	private final FollowReadService followReadService;
	private final PostWriteService postWriteService;
	private final PostTimelineWriteService postTimelineWriteService;

	public Long execute(RegisterPostCommand command) {
		Long postId = postWriteService.create(command);
		List<Long> fromMemberIds = followReadService.getFollowers(command.memberId())
			.stream()
			.map(FollowDto::fromMemberId)
			.toList();

		postTimelineWriteService.deliveryToPostTimeline(postId, fromMemberIds);

		return postId;
	}
}
