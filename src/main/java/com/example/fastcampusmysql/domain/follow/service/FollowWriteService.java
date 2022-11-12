package com.example.fastcampusmysql.domain.follow.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowWriteService {

	private final FollowRepository followRepository;

	public void create(MemberDto fromMemberDto, MemberDto toMemberDto) {
		// validate fromMemberId != toMemberId
		Assert.isTrue(!fromMemberDto.id().equals(toMemberDto.id()), "fromMemberId와 toMemberId가 같습니다.");

		Follow follow = Follow.builder().fromMemberId(fromMemberDto.id()).toMemberId(toMemberDto.id()).build();

		followRepository.save(follow);
	}
}
