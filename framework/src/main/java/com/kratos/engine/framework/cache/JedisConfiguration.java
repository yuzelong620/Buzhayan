package com.kratos.engine.framework.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 缓存配置
 *
 * @author herton
 */
@Configuration
public class JedisConfiguration {

    @Value("${spring.redis.expiration:3600}")
    private Long expiration;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.namespace}")
    private String namespace;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(5000);
        jedisPoolConfig.setMaxIdle(5000);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setMinIdle(30);
        if (StringUtils.isEmpty(password)) {
            return new JedisPool(jedisPoolConfig, host, port, 10000);
        } else {
            return new JedisPool(jedisPoolConfig, host, port, 10000, password);
        }
    }

    public Long getExpiration() {
        return expiration;
    }

    public String getNamespace() {
        return namespace;
    }
}
