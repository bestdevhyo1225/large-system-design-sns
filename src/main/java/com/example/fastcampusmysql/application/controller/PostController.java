package com.example.fastcampusmysql.application.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fastcampusmysql.application.controller.dto.GetPostDailyCountRequestDto;
import com.example.fastcampusmysql.application.controller.dto.GetPostsPageableRequestDto;
import com.example.fastcampusmysql.application.controller.dto.RegisterPostRequestDto;
import com.example.fastcampusmysql.domain.post.dto.PostDailyCountDto;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostWriteService postWriteService;
	private final PostReadService postReadService;

	@PostMapping
	public Long create(@RequestBody RegisterPostRequestDto request) {
		return postWriteService.create(request.toCommand());
	}

	@GetMapping("/daliy-post-count")
	public List<PostDailyCountDto> getDailyPostCount(GetPostDailyCountRequestDto request) {
		return postReadService.getDailyPostCount(request.toCommand());
	}

	@GetMapping("/members/{memberId}")
	public Page<PostDto> getPosts(@PathVariable Long memberId, GetPostsPageableRequestDto request) {
		Sort sort = Sort.by(Sort.Direction.fromString(request.direction()), request.sort());
		Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
		return postReadService.getPosts(memberId, pageable);
	}
}
