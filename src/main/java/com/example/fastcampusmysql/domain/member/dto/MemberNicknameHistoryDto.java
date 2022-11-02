package com.example.fastcampusmysql.domain.member.dto;

import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import java.time.LocalDateTime;
import lombok.Builder;


public record MemberNicknameHistoryDto(Long id, Long memberId, String nickname, LocalDateTime createdAt) {

    @Builder
    public static MemberNicknameHistoryDto of(MemberNicknameHistory memberNicknameHistory) {
        return new MemberNicknameHistoryDto(memberNicknameHistory.getId(),
                                            memberNicknameHistory.getMemberId(),
                                            memberNicknameHistory.getNickname(),
                                            memberNicknameHistory.getCreatedAt());
    }
}
