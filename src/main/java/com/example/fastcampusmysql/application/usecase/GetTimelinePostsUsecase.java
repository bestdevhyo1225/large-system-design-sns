package com.example.fastcampusmysql.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.dto.PostTimelineDto;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostTimelineReadService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTimelinePostsUsecase {

	private final FollowReadService followReadService;
	private final PostReadService postReadService;
	private final PostTimelineReadService postTimelineReadService;

	public PageCursor<PostDto> execute(Long memberId, CursorRequest cursorRequest) {
		List<FollowDto> followingDtos = followReadService.getFollowings(memberId);
		List<Long> toMemberIds = followingDtos.stream()
			.map(FollowDto::toMemberId)
			.toList();

		return postReadService.getPosts(toMemberIds, cursorRequest);
	}

	public PageCursor<PostDto> executeByPostTimeline(Long memberId, CursorRequest cursorRequest) {
		PageCursor<PostTimelineDto> postTimelineDtos = postTimelineReadService
			.getPostTimelines(memberId, cursorRequest);
		List<Long> postIds = postTimelineDtos
			.contents()
			.stream()
			.map(PostTimelineDto::postId)
			.toList();
		List<PostDto> postDtos = postReadService.getPosts(postIds);

		return new PageCursor<>(postDtos, postTimelineDtos.nextCursorRequest());
	}
}
