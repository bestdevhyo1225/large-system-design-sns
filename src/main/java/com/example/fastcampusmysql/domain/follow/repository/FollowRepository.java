package com.example.fastcampusmysql.domain.follow.repository;

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

import com.example.fastcampusmysql.domain.follow.entity.Follow;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

	private static final String TABLE = "Follow";
	private static final RowMapper<Follow> rowMapper = (ResultSet resultSet, int rowNum) -> Follow.builder()
		.id(resultSet.getLong("id"))
		.fromMemberId(resultSet.getLong("fromMemberId"))
		.toMemberId(resultSet.getLong("toMemberId"))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.build();
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<Follow> findAllByFromMemberId(Long fromMemberId) {
		String sql = String.format("SELECT * FROM %s WHERE fromMemberId = :fromMemberId", TABLE);
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("fromMemberId", fromMemberId);

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
	}

	public List<Follow> findAllByToMemberId(Long toMemberId) {
		String sql = String.format("SELECT * FROM %s WHERE toMemberId = :toMemberId", TABLE);
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("toMemberId", toMemberId);

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
	}

	public Follow save(Follow follow) {
		if (follow.getId() == null) {
			return insert(follow);
		}

		throw new UnsupportedOperationException("Follow??? ????????? ???????????? ????????????.");
	}

	private Follow insert(Follow follow) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate());
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(follow);
		Long id = simpleJdbcInsert.withTableName(TABLE)
			.usingGeneratedKeyColumns("id")
			.executeAndReturnKey(sqlParameterSource)
			.longValue();

		return Follow.builder()
			.id(id)
			.fromMemberId(follow.getFromMemberId())
			.toMemberId(follow.getToMemberId())
			.createdAt(follow.getCreatedAt())
			.build();
	}
}
