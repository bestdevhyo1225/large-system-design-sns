package com.example.fastcampusmysql.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.entity.PostTimeline;
import com.example.fastcampusmysql.domain.post.repository.PostTimelineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostTimelineWriteService {

	private final PostTimelineRepository postTimelineRepository;

	// fromMemberIds: 나를 팔로우 하는 회원번호 리스트
	public void deliveryToPostTimeline(Long postId, List<Long> fromMemberIds) {
		List<PostTimeline> postTimelines = fromMemberIds.stream()
			.map(fromMemberId ->
				PostTimeline.builder()
					.memberId(fromMemberId)
					.postId(postId)
					.build())
			.toList();

		postTimelineRepository.bulkInsert(postTimelines);
	}
}
