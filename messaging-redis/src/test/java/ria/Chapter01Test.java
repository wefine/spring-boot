package ria;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RiaRedisConfiguration.class})
@ActiveProfiles("ch01")
public class Chapter01Test {
    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 432;
    private static final int ARTICLES_PER_PAGE = 25;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisTemplateIsNotNull() {
        Assert.assertNotNull(redisTemplate);
    }

    @Test
    public void testArticleIncrement() {
        BoundValueOperations<String, String> operations = redisTemplate.boundValueOps("article:");
        operations.increment(1);

        String value = operations.get();
        System.out.println(value);
    }

    public String postArticle(Jedis conn, String user, String title, String link) {
        redisTemplate.boundValueOps("article:").increment(1);
        String articleId = String.valueOf(conn.incr("article:"));

        String voted = "voted:" + articleId;
        conn.sadd(voted, user);
        conn.expire(voted, ONE_WEEK_IN_SECONDS);

        long now = System.currentTimeMillis() / 1000;
        String article = "article:" + articleId;
        HashMap<String, String> articleData = new HashMap<String, String>();
        articleData.put("title", title);
        articleData.put("link", link);
        articleData.put("user", user);
        articleData.put("now", String.valueOf(now));
        articleData.put("votes", "1");
        conn.hmset(article, articleData);
        conn.zadd("score:", now + VOTE_SCORE, article);
        conn.zadd("time:", now, article);

        return articleId;
    }
}
