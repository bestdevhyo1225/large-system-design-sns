package com.example.fastcampusmysql.domain.follow.dto;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import java.time.LocalDateTime;
import lombok.Builder;

public record FollowDto(Long id, Long fromMemberId, Long toMemberId, LocalDateTime createdAt) {

    @Builder
    public static FollowDto of(Follow follow) {
        return new FollowDto(follow.getId(), follow.getFromMemberId(), follow.getToMemberId(), follow.getCreatedAt());
    }
}
