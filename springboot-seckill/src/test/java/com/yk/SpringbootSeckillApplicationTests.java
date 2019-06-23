package com.yk;

import com.yk.redis.RedisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootSeckillApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired(required = true)
    JedisPool jedisPool;

    @Test
    public void contextLoads() {

        logger.info("jedisPool uuid : " + 1111);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("11","111");
        }
    }

}
