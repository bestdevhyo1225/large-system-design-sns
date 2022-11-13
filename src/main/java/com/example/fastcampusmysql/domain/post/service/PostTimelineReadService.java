package com.example.fastcampusmysql.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.dto.PostTimelineDto;
import com.example.fastcampusmysql.domain.post.entity.PostTimeline;
import com.example.fastcampusmysql.domain.post.repository.PostTimelineRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostTimelineReadService {

	private final PostTimelineRepository postTimelineRepository;

	public PageCursor<PostTimelineDto> getPostTimelines(Long memberId, CursorRequest cursorRequest) {
		List<PostTimeline> postTimelines = findAllByMemberId(memberId, cursorRequest);
		List<PostTimelineDto> postTimelineDtos = postTimelines.stream()
			.map(postTimeline -> PostTimelineDto.builder().postTimeline(postTimeline).build())
			.toList();
		Long nextKey = postTimelines.stream()
			.mapToLong(PostTimeline::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		return new PageCursor<>(postTimelineDtos, cursorRequest.next(nextKey));
	}

	public List<PostTimeline> findAllByMemberId(Long memberId, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postTimelineRepository.findAllByMemberIdAndLessThanIdAndOrderByIdDesc(
				memberId,
				cursorRequest.key(),
				cursorRequest.size()
			);
		}
		return postTimelineRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
	}
}
