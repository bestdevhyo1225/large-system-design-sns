package com.example.fastcampusmysql.domain.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.dto.GetPostDailyCountCommand;
import com.example.fastcampusmysql.domain.post.dto.PostDailyCountDto;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostReadService {

	private final PostRepository postRepository;

	public List<PostDailyCountDto> getDailyPostCount(GetPostDailyCountCommand command) {
		return postRepository.findPostDaliyCountGroupByCreatedDate(command);
	}

	public Page<PostDto> getPosts(Long memberId, Pageable pageable) {
		Page<Post> pageResult = postRepository.findAllByMemberId(memberId, pageable);
		List<PostDto> postDtos = pageResult
			.stream()
			.map(post -> PostDto.builder().post(post).build())
			.toList();

		return new PageImpl<>(postDtos, pageResult.nextPageable(), pageResult.getTotalElements());
	}

	public PageCursor<PostDto> getPosts(Long memberId, CursorRequest cursorRequest) {
		List<Post> posts = findAllByMemberId(memberId, cursorRequest);

		Long nextKey = posts.stream()
			.mapToLong(Post::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		List<PostDto> postDtos = posts
			.stream()
			.map(post -> PostDto.builder().post(post).build())
			.toList();

		return new PageCursor<>(cursorRequest.next(nextKey), postDtos);
	}

	public List<Post> findAllByMemberId(Long memberId, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postRepository.findAllByMemberIdAndLessThanIdAndOrderByIdDesc(
				memberId,
				cursorRequest.key(),
				cursorRequest.size()
			);
		}
		return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
	}
}
