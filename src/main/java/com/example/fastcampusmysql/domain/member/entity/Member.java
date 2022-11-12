package com.example.fastcampusmysql.domain.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {

	private static final Long NAME_MAX_LENGTH = 10L;
	private final Long id;
	private final LocalDate birthday;
	private final LocalDateTime createdAt;
	private final String email;
	private String grade;
	private String nickname;

	@Builder
	public Member(Long id, LocalDate birthday, LocalDateTime createdAt, String email, String nickname, String grade) {
		this.id = id;
		this.birthday = Objects.requireNonNull(birthday);
		this.createdAt = createdAt == null ? LocalDateTime.now().withNano(0) : createdAt;
		this.email = Objects.requireNonNull(email);
		this.grade = grade == null ? "GENERAL" : grade;
		this.nickname = getNicknameAfterValidate(nickname);
	}

	public void changeNickname(String nickname) {
		this.nickname = getNicknameAfterValidate(nickname);
	}

	public void chagneGrade(String grade) {
		this.grade = grade;
	}

	public boolean isInfluncer() {
		return grade.contains("INFLUNCER");
	}

	private String getNicknameAfterValidate(String nickname) {
		validateNickname(nickname);
		return nickname;
	}

	private void validateNickname(String nickname) {
		if (Objects.requireNonNull(nickname).length() > NAME_MAX_LENGTH) {
			throw new IllegalArgumentException("최대 길이를 초과했습니다.");
		}
	}
}
