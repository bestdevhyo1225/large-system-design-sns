package com.example.fastcampusmysql.domain.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowReadService {

	private final FollowRepository followRepository;

	public List<FollowDto> getFollowings(Long fromMemberId) {
		return followRepository.findAllByFromMemberId(fromMemberId)
			.stream()
			.map(follow -> FollowDto.builder().follow(follow).build())
			.toList();
	}

	public List<FollowDto> getFollowers(Long toMemberId) {
		return followRepository.findAllByToMemberId(toMemberId)
			.stream()
			.map(follow -> FollowDto.builder().follow(follow).build())
			.toList();
	}
}
