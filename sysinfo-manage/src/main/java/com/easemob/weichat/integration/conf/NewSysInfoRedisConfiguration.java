package com.easemob.weichat.integration.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.easemob.weichat.service.util.RedisConfigureUtil;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author shengyp
 * @since 09/08/16
 */
@Slf4j
@Configuration
public class NewSysInfoRedisConfiguration {

    @Value("${kf.sysinfomanage.redis.host}")
    private String newSysInfoRedisHost;
    @Value("${kf.sysinfomanage.redis.port}")
    private int newSysInfoRedisPort;
    @Value("${kf.sysinfomanage.redis.timeout}")
    private int newSysInfoRedisTimeout;
    @Value("${kf.sysinfomanage.redis.pool.max-idle}")
    private int newSysInfoRedisPoolMaxIdle;
    @Value("${kf.sysinfomanage.redis.pool.min-idle}")
    private int newSysInfoRedisPoolMinIdle;
    @Value("${kf.sysinfomanage.redis.pool.max-wait}")
    private int newSysInfoRedisPoolMaxWait;
    @Value("${kf.sysinfomanage.redis.pool.max-total}")
    private int newSysInfoRedisPoolMaxTotal;

    @Bean(name="newSysInfoRedisTemplate")
    public RedisTemplate<String, String> newSysInfotRedisTemplate() {
        log.info("newSysInfoRedisTemplate init with properties");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(newSysInfoRedisPoolMaxIdle);
        jedisPoolConfig.setMinIdle(newSysInfoRedisPoolMinIdle);
        jedisPoolConfig.setMaxWaitMillis(newSysInfoRedisPoolMaxWait);
        jedisPoolConfig.setMaxTotal(newSysInfoRedisPoolMaxTotal);
        
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setHostName(newSysInfoRedisHost);
        jedisConnectionFactory.setPort(newSysInfoRedisPort);
        jedisConnectionFactory.setTimeout(newSysInfoRedisTimeout);
        jedisConnectionFactory.afterPropertiesSet();

        RedisConfigureUtil.configureKEA(jedisConnectionFactory);
        RedisTemplate<String, String> stringRedisTemplate = new RedisTemplate<String, String>(){
        @Override
            protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
                return new DefaultStringRedisConnection(connection);
            }
        };
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        stringRedisTemplate.setKeySerializer(stringSerializer);
        stringRedisTemplate.setValueSerializer(stringSerializer);
        stringRedisTemplate.setHashKeySerializer(stringSerializer);
        stringRedisTemplate.setHashValueSerializer(stringSerializer);
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        return stringRedisTemplate;
    }
}
