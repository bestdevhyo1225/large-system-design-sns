package com.example.fastcampusmysql.domain.member.repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberNicknameHistoryRepository {

	private static final String TABLE = "MemberNicknameHistory";
	private static final RowMapper<MemberNicknameHistory> rowMapper = (ResultSet resultSet, int rowNum) ->
		MemberNicknameHistory.builder()
			.id(resultSet.getLong("id"))
			.memberId(resultSet.getLong("memberId"))
			.nickname(resultSet.getString("nickname"))
			.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
			.build();
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<MemberNicknameHistory> findAllByMemberId(Long memberId) {
		String sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE);
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue("memberId", memberId);
		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
	}

	public MemberNicknameHistory save(MemberNicknameHistory memberNicknameHistory) {
		if (memberNicknameHistory.getId() == null) {
			return insert(memberNicknameHistory);
		}

		throw new UnsupportedOperationException("MemberNicknameHistory는 갱신을 지원하지 않습니다.");
	}

	private MemberNicknameHistory insert(MemberNicknameHistory memberNicknameHistory) {
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberNicknameHistory);
		Long id = getSimpleJdbcInsert().executeAndReturnKey(sqlParameterSource).longValue();

		return MemberNicknameHistory.builder()
			.id(id)
			.memberId(memberNicknameHistory.getMemberId())
			.nickname(memberNicknameHistory.getNickname())
			.createdAt(memberNicknameHistory.getCreatedAt())
			.build();
	}

	private SimpleJdbcInsert getSimpleJdbcInsert() {
		return new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate()).withTableName(TABLE)
			.usingGeneratedKeyColumns("id");
	}
}
