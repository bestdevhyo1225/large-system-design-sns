package com.example.fastcampusmysql.domain.post.repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

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

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepository {

	private static final String TABLE = "POST";

	private static final RowMapper<PostDailyCountDto> POST_DAILY_COUNT_DTO_ROW_MAPPER =
		(ResultSet resultSet, int rowNum) ->
			PostDailyCountDto.builder()
				.memberId(resultSet.getLong("memberId"))
				.date(resultSet.getObject("createdDate", LocalDate.class))
				.postCount(resultSet.getLong("postCount"))
				.build();

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<PostDailyCountDto> findPostDaliyCountGroupByCreatedDate(GetPostDailyCountCommand command) {
		String sql = String.format(""
			+ "SELECT createdDate, memberId, COUNT(id) AS postCount "
			+ "FROM %s "
			+ "WHERE memberId = :memberId AND createdDate BETWEEN :firstDate AND :lastDate "
			+ "GROUP BY memberId, createdDate"
			+ "", TABLE);

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
			.addValue("memberId", command.memberId())
			.addValue("firstDate", command.firstDate())
			.addValue("lastDate", command.lastDate());

		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, POST_DAILY_COUNT_DTO_ROW_MAPPER);
	}

	public Post save(Post post) {
		if (post.getId() == null) {
			return insert(post);
		}
		throw new UnsupportedOperationException("Post는 갱신을 지원하지 않습니다.");
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
}
