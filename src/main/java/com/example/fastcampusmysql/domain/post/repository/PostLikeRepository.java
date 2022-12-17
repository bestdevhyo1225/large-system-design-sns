package com.example.fastcampusmysql.domain.post.repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.post.entity.PostLike;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {

	private static final String TABLE = "POST_LIKE";

	private static final RowMapper<PostLike> POST_LIKE_ROW_MAPPER =
		(ResultSet resultSet, int rowNum) ->
			PostLike.builder()
				.id(resultSet.getLong("id"))
				.memberId(resultSet.getLong("memberId"))
				.postId(resultSet.getLong("postId"))
				.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
				.build();

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Long countByPostId(Long postId) {
		String sql = String.format("""
			SELECT COUNT(id)
			FROM %s
			WHERE id = :postId
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("postId", postId);

		return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Long.class);
	}

	public PostLike save(PostLike postLike) {
		if (postLike.getId() == null) {
			return insert(postLike);
		}
		throw new UnsupportedOperationException("PostLike는 갱신을 지원하지 않습니다.");
	}

	private PostLike insert(PostLike postLike) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate());
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(postLike);
		Long id = simpleJdbcInsert.withTableName(TABLE)
			.usingGeneratedKeyColumns("id")
			.executeAndReturnKey(sqlParameterSource)
			.longValue();

		return PostLike.builder()
			.id(id)
			.memberId(postLike.getMemberId())
			.postId(postLike.getPostId())
			.createdAt(postLike.getCreatedAt())
			.build();
	}
}
