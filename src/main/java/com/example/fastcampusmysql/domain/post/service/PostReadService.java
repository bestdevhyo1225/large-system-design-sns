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
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostReadService {

	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;

	private static long getNextKey(List<Post> posts) {
		return posts.stream()
			.mapToLong(Post::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);
	}

	public List<PostDailyCountDto> getDailyPostCount(GetPostDailyCountCommand command) {
		return postRepository.findPostDaliyCountGroupByCreatedDate(command);
	}

	public Page<PostDto> getPosts(Long memberId, Pageable pageable) {
		Page<Post> pageResult = postRepository.findAllByMemberId(memberId, pageable);
		List<PostDto> postDtos = pageResult.stream()
			.map(post ->
				PostDto.builder()
					.post(post)
					.build()
			)
			.toList();

		return new PageImpl<>(postDtos, pageResult.nextPageable(), pageResult.getTotalElements());
	}

	public PageCursor<PostDto> getPosts(Long memberId, CursorRequest cursorRequest) {
		List<Post> posts = findAllByMemberId(memberId, cursorRequest);
		List<PostDto> postDtos = posts.stream()
			.map(post ->
				PostDto.builder()
					.post(post)
					.build()
			)
			.toList();
		Long nextKey = getNextKey(posts);

		return new PageCursor<>(postDtos, cursorRequest.next(nextKey));
	}

	public PageCursor<PostDto> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
		List<Post> posts = findAllByMemberIds(memberIds, cursorRequest);
		List<PostDto> postDtos = posts.stream()
			.map(post ->
				PostDto.builder()
					.post(post)
					.build()
			)
			.toList();
		Long nextKey = getNextKey(posts);

		return new PageCursor<>(postDtos, cursorRequest.next(nextKey));
	}

	public List<PostDto> getPosts(List<Long> ids) {
		return postRepository.findAllByInId(ids).stream()
			.map(post -> PostDto.builder().post(post).build())
			.toList();
	}

	public PostDto getPost(Long postId) {
		Post post = postRepository.findById(postId, false).orElseThrow();
		Long postLikeCount = postLikeRepository.countByPostId(postId);
		return PostDto.builder()
			.post(post)
			.postLikeCount(postLikeCount)
			.build();
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

	public List<Post> findAllByMemberIds(List<Long> memberIds, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postRepository.findAllByMemberIdsAndLessThanIdAndOrderByIdDesc(
				memberIds,
				cursorRequest.key(),
				cursorRequest.size()
			);
		}
		return postRepository.findAllByMemberIdsAndOrderByIdDesc(memberIds, cursorRequest.size());
	}
}
