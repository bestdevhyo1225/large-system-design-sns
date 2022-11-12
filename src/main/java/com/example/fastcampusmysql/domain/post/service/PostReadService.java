package com.example.fastcampusmysql.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.dto.GetPostDailyCountCommand;
import com.example.fastcampusmysql.domain.post.dto.PostDailyCountDto;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostReadService {

	private final PostRepository postRepository;

	public List<PostDailyCountDto> getDailyPostCount(GetPostDailyCountCommand command) {
		return postRepository.findPostDaliyCountGroupByCreatedDate(command);
	}
}
