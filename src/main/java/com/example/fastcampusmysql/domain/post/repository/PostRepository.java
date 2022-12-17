package com.example.fastcampusmysql.domain.post.repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.post.dto.GetPostDailyCountCommand;
import com.example.fastcampusmysql.domain.post.dto.PostDailyCountDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.util.PageHelper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepository {

	private static final String TABLE = "POST";

	private static final RowMapper<Post> POST_ROW_MAPPER =
		(ResultSet resultSet, int rowNum) ->
			Post.builder()
				.id(resultSet.getLong("id"))
				.memberId(resultSet.getLong("memberId"))
				.contents(resultSet.getString("contents"))
				.likeCount(resultSet.getLong("likeCount"))
				.createdDate(resultSet.getObject("createdDate", LocalDate.class))
				.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
				.build();

	private static final RowMapper<PostDailyCountDto> POST_DAILY_COUNT_DTO_ROW_MAPPER =
		(ResultSet resultSet, int rowNum) ->
			PostDailyCountDto.builder()
				.memberId(resultSet.getLong("memberId"))
				.date(resultSet.getObject("createdDate", LocalDate.class))
				.postCount(resultSet.getLong("postCount"))
				.build();

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<PostDailyCountDto> findPostDaliyCountGroupByCreatedDate(GetPostDailyCountCommand command) {
		String sql = String.format("""
			SELECT createdDate, memberId, COUNT(id) AS postCount
			FROM %s
			WHERE memberId = :memberId AND createdDate BETWEEN :firstDate AND :lastDate
			GROUP BY memberId, createdDate
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberId", command.memberId())
			.addValue("firstDate", command.firstDate())
			.addValue("lastDate", command.lastDate());

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_DAILY_COUNT_DTO_ROW_MAPPER);
	}

	public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
		String sql = String.format("""
			SELECT *
			FROM %s
			WHERE memberId = :memberId
			ORDER BY %s
			LIMIT :limit
			OFFSET :offset
			""", TABLE, PageHelper.orderBy(pageable.getSort()));

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberId", memberId)
			.addValue("limit", pageable.getPageSize())
			.addValue("offset", pageable.getOffset());

		List<Post> posts = namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_ROW_MAPPER);

		return new PageImpl<>(posts, pageable, getCountByMemberId(memberId));
	}

	private Long getCountByMemberId(Long memberId) {
		String sql = String.format("""
			SELECT COUNT(id)
			FROM %s
			WHERE memberId = :memberId
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberId", memberId);

		return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Long.class);
	}

	public Optional<Post> findById(Long postId, Boolean requiredLock) {
		String sql = String.format("SELECT * FROM %s WHERE id = :postId", TABLE);

		if (requiredLock) {
			sql += "FOR UPDATE";
		}

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("postId", postId);

		Post post = namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, POST_ROW_MAPPER);

		return Optional.ofNullable(post);
	}

	public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, Long size) {
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

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_ROW_MAPPER);
	}

	public List<Post> findAllByMemberIdsAndOrderByIdDesc(List<Long> memberIds, Long size) {
		if (memberIds.isEmpty()) {
			return List.of();
		}

		String sql = String.format("""
			SELECT *
			FROM %s
			WHERE memberId IN (:memberIds)
			ORDER BY id DESC
			LIMIT :limit
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberIds", memberIds)
			.addValue("limit", size);

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_ROW_MAPPER);
	}

	public List<Post> findAllByMemberIdAndLessThanIdAndOrderByIdDesc(Long memberId, Long id, Long size) {
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

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_ROW_MAPPER);
	}

	public List<Post> findAllByMemberIdsAndLessThanIdAndOrderByIdDesc(List<Long> memberIds, Long id, Long size) {
		if (memberIds.isEmpty()) {
			return List.of();
		}

		String sql = String.format("""
			SELECT *
			FROM %s
			WHERE memberId IN (:memberIds)
			AND id < :id
			ORDER BY id DESC
			LIMIT :limit
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberIds", memberIds)
			.addValue("id", id)
			.addValue("limit", size);

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_ROW_MAPPER);
	}

	public List<Post> findAllByInId(List<Long> ids) {
		if (ids.isEmpty()) {
			return List.of();
		}

		String sql = String.format("""
			SELECT *
			FROM %s
			WHERE id IN (:ids)
			""", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("ids", ids);

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_ROW_MAPPER);
	}

	public Post save(Post post) {
		if (post.getId() == null) {
			return insert(post);
		}
		return update(post);
	}

	public void bulkInsert(List<Post> posts) {
		String sql = String.format("""
			INSERT INTO %s (memberId, contents, createdDate, createdAt)
			VALUES (:memberId, :contents, :createdDate, :createdAt)
			""", TABLE);

		SqlParameterSource[] sqlParameterSources = posts.stream()
			.map(BeanPropertySqlParameterSource::new)
			.toArray(SqlParameterSource[]::new);

		namedParameterJdbcTemplate.batchUpdate(sql, sqlParameterSources);
	}

	private Post insert(Post post) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate());
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(post);
		Long id = simpleJdbcInsert.withTableName(TABLE)
			.usingGeneratedKeyColumns("id")
			.executeAndReturnKey(sqlParameterSource)
			.longValue();

		return Post.builder()
			.id(id)
			.memberId(post.getMemberId())
			.contents(post.getContents())
			.createdDate(post.getCreatedDate())
			.createdAt(post.getCreatedAt())
			.build();
	}

	private Post update(Post post) {
		String sql = String.format("""
			UPDATE %s
			SET memberId = :memberId, contents = :contents, likeCount = :likeCount
			WHERE id = :id
			""", TABLE);
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(post);

		namedParameterJdbcTemplate.update(sql, sqlParameterSource);

		return post;
	}
}
