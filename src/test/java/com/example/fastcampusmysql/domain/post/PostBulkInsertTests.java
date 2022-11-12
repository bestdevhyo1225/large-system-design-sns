package com.example.fastcampusmysql.domain.post;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.PostFixtureFactory;

@SpringBootTest
public class PostBulkInsertTests {

	@Autowired
	private PostRepository postRepository;

	@Test
	public void bulkInsert() {
		EasyRandom easyRandom = PostFixtureFactory.get(
			2L,
			LocalDate.of(1970, 1, 1),
			LocalDate.of(2022, 1, 1)
		);

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		List<Post> posts = IntStream.range(0, 1_000_000)
			.parallel()
			.mapToObj(i -> easyRandom.nextObject(Post.class))
			.toList();

		stopWatch.stop();

		System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds() + "초");

		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();

		// postRepository.bulkInsert(posts);

		queryStopWatch.stop();

		System.out.println("쿼리 수행 시간 : " + queryStopWatch.getTotalTimeSeconds() + "초");
	}
}
