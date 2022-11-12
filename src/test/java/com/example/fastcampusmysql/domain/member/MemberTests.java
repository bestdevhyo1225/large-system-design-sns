package com.example.fastcampusmysql.domain.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.util.MemberFixtureFactory;

public class MemberTests {

	@Test
	@DisplayName("회원 생성시 닉네임이 null 값인 경우, NullPointerException 예외를 던진다.")
	public void testNicknameIsNull() {
		Long id = null;
		String email = "email";
		LocalDate birthday = LocalDate.now();
		LocalDateTime createdAt = LocalDateTime.now().withNano(0);

		assertThrows(NullPointerException.class,
			() -> Member.builder().id(id).email(email).birthday(birthday).createdAt(createdAt).build());
	}

	@Test
	@DisplayName("회원 생성시 닉네임이 10자를 초과한 경우, IllegalArgumentException 예외를 던진다.")
	public void testNicknameLongerThan10Characters() {
		Long id = null;
		String email = "email";
		String nickname = "nickname-nickname";
		LocalDate birthday = LocalDate.now();
		LocalDateTime createdAt = LocalDateTime.now().withNano(0);

		assertThrows(IllegalArgumentException.class,
			() -> Member.builder()
				.id(id)
				.email(email)
				.nickname(nickname)
				.birthday(birthday)
				.createdAt(createdAt)
				.build());
	}

	@Test
	@DisplayName("회원은 닉네임을 변경할 수 있다.")
	public void testChangeNickname() {
		// given
		Member member = MemberFixtureFactory.create();
		String expected = "jhs";

		// when
		member.changeNickname(expected);

		// then
		assertEquals(expected, member.getNickname());
	}

	@Test
	@DisplayName("회원 닉네임을 변경시 null 값인 경우, NullPointException 예외를 던진다.")
	public void testChangeNicknameIsNull() {
		Member member = MemberFixtureFactory.create();
		String expected = null;

		assertThrows(NullPointerException.class, () -> member.changeNickname(expected));
	}

	@Test
	@DisplayName("회원 닉네임을 변경시 10자를 초과한 경우, IllegalArgumentException 예외를 던진다.")
	public void testChangeNicknameLongerThan10Characters() {
		Member member = MemberFixtureFactory.create();
		String expected = "nickname-nickname";

		assertThrows(IllegalArgumentException.class, () -> member.changeNickname(expected));
	}
}
