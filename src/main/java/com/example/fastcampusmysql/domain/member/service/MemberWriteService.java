package com.example.fastcampusmysql.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

	private final MemberRepository memberRepository;
	private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

	@Transactional
	public MemberDto register(RegisterMemberCommand command) {
		Member member = memberRepository.save(command.toEntity());

		saveMemberNicknameHistory(member);

		return MemberDto.builder().member(member).build();
	}

	@Transactional
	public void changeNickname(Long memberId, String nickname) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		member.changeNickname(nickname);
		memberRepository.save(member);

		saveMemberNicknameHistory(member);
	}

	private void saveMemberNicknameHistory(Member member) {
		MemberNicknameHistory memberNicknameHistory = MemberNicknameHistory.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.build();

		memberNicknameHistoryRepository.save(memberNicknameHistory);
	}
}
