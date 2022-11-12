package com.example.fastcampusmysql.util;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import com.example.fastcampusmysql.domain.member.entity.Member;

public class MemberFixtureFactory {

	public static Member create() {
		return new EasyRandom(new EasyRandomParameters()).nextObject(Member.class);
	}

	public static Member create(Long seed) {
		EasyRandomParameters easyRandomParameters = new EasyRandomParameters().seed(seed);
		return new EasyRandom(easyRandomParameters).nextObject(Member.class);
	}
}
