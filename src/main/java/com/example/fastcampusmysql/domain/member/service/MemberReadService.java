package com.example.fastcampusmysql.domain.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberReadService {

	private final MemberRepository memberRepository;
	private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

	public MemberDto getMember(Long id) {
		Member member = memberRepository.findById(id).orElseThrow();
		return MemberDto.builder().member(member).build();
	}

	public List<MemberDto> getMembers(List<Long> ids) {
		return memberRepository.findAllByMemberIdIn(ids)
			.stream()
			.map(member -> MemberDto.builder().member(member).build())
			.toList();
	}

	public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId) {
		return memberNicknameHistoryRepository.findAllByMemberId(memberId)
			.stream()
			.map(memberNicknameHistory -> MemberNicknameHistoryDto.builder()
				.memberNicknameHistory(memberNicknameHistory)
				.build())
			.toList();
	}
}
