package com.valuewith.tweaver.commons.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtilService {

  private final StringRedisTemplate stringRedisTemplate;

  public String getData(String key) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void setDataTimeout(String key, String value, Long minutes) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    Duration timeout = Duration.ofMinutes(minutes);
    valueOperations.set(key, value, timeout);
  }
}
