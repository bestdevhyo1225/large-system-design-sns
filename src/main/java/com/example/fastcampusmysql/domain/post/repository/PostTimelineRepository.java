package com.example.fastcampusmysql.domain.post.repository;

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

import com.example.fastcampusmysql.domain.post.entity.PostTimeline;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostTimelineRepository {

	private static final String TABLE = "POST_TIMELINE";

	private static final RowMapper<PostTimeline> POST_TIMELINE_ROW_MAPPER =
		(ResultSet resultSet, int rowNum) ->
			PostTimeline.builder()
				.id(resultSet.getLong("id"))
				.memberId(resultSet.getLong("memberId"))
				.postId(resultSet.getLong("postId"))
				.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
				.build();

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<PostTimeline> findAllByMemberIdAndOrderByIdDesc(Long memberId, Long size) {
		String sql = String.format("""
			SELECT *
			FROM %s
			WHERE memberId = :memberId
			ORDER BY id DESC
			LIMIT :limit
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberId", memberId)
			.addValue("limit", size);

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_TIMELINE_ROW_MAPPER);
	}

	public List<PostTimeline> findAllByMemberIdAndLessThanIdAndOrderByIdDesc(Long memberId, Long id, Long size) {
		String sql = String.format("""
			SELECT *
			FROM %s
			WHERE memberId = :memberId
			AND id < :id
			ORDER BY id DESC
			LIMIT :limit
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberId", memberId)
			.addValue("id", id)
			.addValue("limit", size);

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_TIMELINE_ROW_MAPPER);
	}

	public void bulkInsert(List<PostTimeline> postTimelines) {
		String sql = String.format("""
			INSERT INTO %s (memberId, postId, createdAt)
			VALUES (:memberId, :postId, :createdAt)
			""", TABLE);

		SqlParameterSource[] sqlParameterSources = postTimelines.stream()
			.map(BeanPropertySqlParameterSource::new)
			.toArray(SqlParameterSource[]::new);

		namedParameterJdbcTemplate.batchUpdate(sql, sqlParameterSources);
	}

	public PostTimeline save(PostTimeline postTimeline) {
		if (postTimeline.getId() == null) {
			return insert(postTimeline);
		}
		throw new UnsupportedOperationException("PostTimeline는 갱신을 지원하지 않습니다.");
	}

	private PostTimeline insert(PostTimeline postTimeline) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate());
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(postTimeline);
		Long id = simpleJdbcInsert.withTableName(TABLE)
			.usingGeneratedKeyColumns("id")
			.executeAndReturnKey(sqlParameterSource)
			.longValue();

		return PostTimeline.builder()
			.id(id)
			.memberId(postTimeline.getMemberId())
			.postId(postTimeline.getPostId())
			.createdAt(postTimeline.getCreatedAt())
			.build();
	}
}
