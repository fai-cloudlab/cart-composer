package com.fujitsu.cloudlab.cart.config;

import com.fujitsu.cloudlab.cart.model.Cart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.cache.redis.host}")
  private String REDIS_HOSTNAME;

  @Value("${spring.cache.redis.port}")
  private Integer REDIS_PORT;

  @Value("${spring.cache.redis.password}")
  private String REDIS_PASSWORD;

  @Bean
  protected JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration configuration =
        new RedisStandaloneConfiguration(REDIS_HOSTNAME, REDIS_PORT);
    // configuration.setPassword(RedisPassword.of(REDIS_PASSWORD));
    JedisConnectionFactory factory = new JedisConnectionFactory(configuration);
    factory.afterPropertiesSet();
    return factory;
  }

  @Bean
  public RedisTemplate<String, Cart> redisTemplate() {
    final RedisTemplate<String, Cart> redisTemplate = new RedisTemplate<String, Cart>();
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }
}
