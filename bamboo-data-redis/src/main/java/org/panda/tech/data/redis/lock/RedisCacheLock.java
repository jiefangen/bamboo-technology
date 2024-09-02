package org.panda.tech.data.redis.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存锁
 *
 * @author fangen
 **/
public class RedisCacheLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Boolean isLocked(String lockKey, long expireTime, TimeUnit timeUnit) {
        // 使用RedisCallback接口执行set命令，设置锁键；设置额外选项：过期时间和SET_IF_ABSENT选项
        return stringRedisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.set(lockKey.getBytes(), new byte[0],
                        Expiration.from(expireTime, timeUnit),
                        RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    /**
     * 获取锁
     */
    public boolean acquireLock(String key, String value, long expireTime) {
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
        return result != null && result;
    }

    /**
     * 释放锁
     */
    public void releaseLock(String key, String value) {
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if (value.equals(currentValue)) {
            stringRedisTemplate.delete(key);
        }
    }
}
