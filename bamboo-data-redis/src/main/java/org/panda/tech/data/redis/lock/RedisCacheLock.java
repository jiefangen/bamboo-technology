package org.panda.tech.data.redis.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Collections;
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
     * lua脚本获取锁
     * 原子性，获取锁和设置过期时间原子执行
     *
     * @param lockKey 锁key
     * @param identifier 标识锁的持有者
     * @param expireTime 失效时间，单位秒
     */
    public boolean acquireLuaLock(String lockKey, String identifier, int expireTime) {
        String luaScript = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then " +
                "return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);
        Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(lockKey), identifier,
                String.valueOf(expireTime));
        return result != null && result == 1;
    }

    /**
     * lua脚本释放锁
     *
     *  @param lockKey 锁key
     *  @param identifier 标识锁的持有者
     */
    public void releaseLuaLock(String lockKey, String identifier) {
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);
        stringRedisTemplate.execute(redisScript, Collections.singletonList(lockKey), identifier);
    }
}
