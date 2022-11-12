package com.example.fastcampusmysql.domain.post.service;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.dto.RegisterPostCommand;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostWriteService {

	private final PostRepository postRepository;

	public Long create(RegisterPostCommand command) {
		return postRepository.save(command.toEntity()).getId();
	}
}
