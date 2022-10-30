package com.example.fastcampusmysql.domain.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Member {

    private final static Long NAME_MAX_LENGTH = 10L;
    private final Long id;
    private final LocalDate birthday;
    private final LocalDateTime createdAt;
    private final String email;
    private String nickname;

    private Member(Long id, String email, String nickname, LocalDate birthday, LocalDateTime createdAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.nickname = nickname;
        this.birthday = Objects.requireNonNull(birthday);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public static Member of(Long id, String email, String nickname, LocalDate birthday, LocalDateTime createdAt) {
        validateNickname(nickname);
        return new Member(id, email, nickname, birthday, createdAt);
    }

    private static void validateNickname(String nickname) {
        Objects.requireNonNull(nickname);

        if (nickname.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("최대 길이를 초과했습니다.");
        }
    }

    public void changeNickname(String nickname) {
        Objects.requireNonNull(nickname);
        validateNickname(nickname);
        this.nickname = nickname;
    }
}
