package com.easemob.weichat.integration.conf;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author likai at apache.org
 * @since 9/21/15
 */
@Configuration
public class RedisConfiguration {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.timeout}")
    private  int redisTimeout;

    @Value("${spring.redis.pool.max-idle}")
    private int redisPoolMaxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private int redisPoolMinIdle;
    @Value("${spring.redis.pool.max-active}")
    private int redisPoolMaxActive;
    @Value("${spring.redis.pool.max-wait}")
    private int redisPoolMaxWait;
    @Value("${spring.redis.pool.max-total}")
    private int redisPoolMaxTotal;
    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisPoolMaxIdle);
        poolConfig.setMinIdle(redisPoolMinIdle);
        poolConfig.setMaxTotal(redisPoolMaxTotal);
        poolConfig.setMaxWaitMillis(redisPoolMaxWait);
        return poolConfig;
    }

    @Bean()
    public JedisPool jedisPool(){
        return new JedisPool(jedisPoolConfig(), redisHost, redisPort, redisTimeout);
    }
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig());
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setTimeout(redisTimeout);
        return jedisConnectionFactory;
    }
    @Bean
    public StringRedisTemplate redisTemplate() {
        return new StringRedisTemplate(jedisConnectionFactory());
    }

}
