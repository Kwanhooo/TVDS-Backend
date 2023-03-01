package org.csu.inheritance.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 浅封装
 *
 * @author Kwanho
 */
@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /*========== key操作 ==========*/

    /**
     * 删除key
     *
     * @param key 键
     */
    public void delete(String key) {
        log.info("delete(..) => key -> {}", key);
        redisTemplate.delete(key);

    }

    /**
     * 是否存在key
     *
     * @param key 键
     * @return boolean
     */
    public boolean hasKey(String key) {
        log.info("hasKey(..) => key -> {}", key);
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key 键
     * @param timeout 时长
     * @param unit 时间单位
     * @return Boolean
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        log.info("expire(..) => key -> {},timeout -> {},unit -> {}", key);
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key 键
     * @param date 过期时间
     * @return Boolean
     */
    public Boolean expireAt(String key, Date date) {
        log.info("expireAt(..) => key -> {},date -> {}", key);
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 键
     * @return Boolean
     */
    public Boolean persist(String key) {
        log.info("persist(..) => key -> {}", key);
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key 键
     * @param unit 时间单位
     * @return
     */
    public Long getExpire(String key, TimeUnit unit) {
        log.info("getExpire(..) => key -> {},unit -> {}", key, unit);
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey oldKey
     * @param newKey newKey
     */
    public void rename(String oldKey, String newKey) {
        log.info("rename(..) => oldKey -> {},newKey -> {}", oldKey, newKey);
        redisTemplate.rename(oldKey, newKey);
    }

    /*========== SDS操作 ==========*/

    /**
     * 设置指定 key 的值
     *
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        log.info("set(..) => key -> {},value -> {}", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键
     * @return
     */
    public Object get(String key) {
        log.info("get(..) => key -> {}", key);
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取
     *
     * @param keys 键
     * @return
     */
    public List<Object> multiGet(Collection<String> keys) {
        log.info("multiGet(..) => keys -> {}", keys);
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 设置指定 key的值 ，并将 key 的过期时间设为 timeout
     *
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit    时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        log.info("set(..) => key -> {},value -> {},timeout -> {},unit -> {}", key, value, timeout, unit);
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 递增
     *
     * @param key  键
     * @param data 要增加几(大于0)
     */
    public long incr(String key, long data) {
        if (data < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        log.info("incr(..) => key -> {},data -> {}", key, data);
        return redisTemplate.opsForValue().increment(key, data);
    }

    /**
     * 递减
     *
     * @param key  键
     * @param data 要减少几(小于0)
     */
    public long decr(String key, long data) {
        if (data < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        log.info("decr(..) => key -> {},data -> {}", key, data);
        return redisTemplate.opsForValue().increment(key, -data);
    }

    /**
     * 若不存在key时, 向redis中添加一个key-value, 若存在，则不作任何操作, 返回false。
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setIfAbsent(String key, Object value) {
        log.info("setIfAbsent(...) => key -> {}, value -> {}", key, value);
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
        log.info("setIfAbsent(...) => result -> {}", result);
        return result;
    }

    /**
     * 若不存在key时, 向redis中添加一个(具有超时时长的)key-value, 返回成功/失败。
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        log.info("setIfAbsent(...) => key -> {}, value -> {}, key -> {}, value -> {}", key, value, timeout, unit);
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        log.info("setIfAbsent(...) => result -> {}", result);
        return result;
    }


    /*========== Hash操作 ==========*/

    /**
     * 向 key对应的 hash中，增加一个键值对 entryKey-entryValue
     *
     * @param key
     * @param entryKey
     * @param entryValue
     */
    public void hPut(String key, String entryKey, String entryValue) {
        log.info("hPut(...) => key -> {}, entryKey -> {}, entryValue -> {}", key, entryKey, entryValue);
        redisTemplate.opsForHash().put(key, entryKey, entryValue);
    }

    /**
     * 向key对应的hash中，增加maps
     *
     * @param key
     * @param maps
     */
    public void hPutAll(String key, Map<String, String> maps) {
        log.info("hPutAll(...) => key -> {}, maps -> {}", key, maps);
        redisTemplate.opsForHash().putAll(key, maps);
    }


    /**
     * 当key对应的hash中,不存在entryKey时，才(向key对应的hash中，)增加entryKey-entryValue
     *
     * @param key
     * @param entryKey
     * @param entryValue
     * @return
     */
    public boolean hPutIfAbsent(String key, String entryKey, String entryValue) {
        log.info("hPutIfAbsent(...) => key -> {}, entryKey -> {}, entryValue -> {}",
                key, entryKey, entryValue);
        Boolean result = redisTemplate.opsForHash().putIfAbsent(key, entryKey, entryValue);
        log.info("hPutIfAbsent(...) => result -> {}", result);
        return result;
    }


    /**
     * HashGet
     *
     * @param key
     * @param entryKey
     */
    public Object hGet(String key, String entryKey) {
        log.info("hGet(...) => key -> {}, entryKey -> {}", key, entryKey);
        Object entryValue = redisTemplate.opsForHash().get(key, entryKey);
        log.info("hGet(...) => entryValue -> {}", entryValue);
        return entryValue;
    }

    /**
     * 获取到 key对应的Map<K, V>
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hGetAll(String key) {
        log.info("hGetAll(...) => key -> {}", key);
        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        log.info("hGetAll(...) => result -> {}", result);
        return result;
    }


    /**
     * 批量获取(key对应的)hash中的entryKey的entryValue
     *
     * @param key
     * @param entryKeys
     * @return
     */
    public List<Object> hMultiGet(String key, Collection<Object> entryKeys) {
        log.info("hMultiGet(...) => key -> {}, entryKeys -> {}", key, entryKeys);
        List<Object> entryValues = redisTemplate.opsForHash().multiGet(key, entryKeys);
        log.info("hMultiGet(...) => entryValues -> {}", entryValues);
        return entryValues;
    }

    /**
     * (批量)删除(key对应的)hash中的对应entryKey-entryValue
     *
     * @param key
     * @param entryKeys
     * @return
     */
    public long hDelete(String key, Object... entryKeys) {
        log.info("hDelete(...) => key -> {}, entryKeys -> {}", key, entryKeys);
        Long count = redisTemplate.opsForHash().delete(key, entryKeys);
        log.info("hDelete(...) => count -> {}", count);
        return count;
    }

    /**
     * 查看(key对应的)hash中，是否存在entryKey对应的entry
     *
     * @param key
     * @param entryKey
     * @return
     */
    public boolean hExists(String key, String entryKey) {
        log.info("hDelete(...) => key -> {}, entryKeys -> {}", key, entryKey);
        Boolean exist = redisTemplate.opsForHash().hasKey(key, entryKey);
        log.info("hDelete(...) => exist -> {}", exist);
        return exist;
    }

    /**
     * 增/减(hash中的某个entryValue值) 整数
     *
     * @param key
     * @param entryKey
     * @param increment
     * @return
     */
    public long hIncrBy(String key, Object entryKey, long increment) {
        log.info("hIncrBy(...) => key -> {}, entryKey -> {}, increment -> {}",
                key, entryKey, increment);
        Long result = redisTemplate.opsForHash().increment(key, entryKey, increment);
        log.info("hIncrBy(...) => result -> {}", result);
        return result;
    }


    /**
     * 增/减(hash中的某个entryValue值) 浮点数
     *
     * @param key
     * @param entryKey
     * @param increment
     * @return
     */
    public double hIncrByFloat(String key, Object entryKey, double increment) {
        log.info("hIncrByFloat(...) => key -> {}, entryKey -> {}, increment -> {}",
                key, entryKey, increment);
        Double result = redisTemplate.opsForHash().increment(key, entryKey, increment);
        log.info("hIncrByFloat(...) => result -> {}", result);

        return result;
    }

    /**
     * 获取(key对应的)hash中的所有entryKey
     *
     * @param key
     * @return
     */
    public Set<Object> hKeys(String key) {
        log.info("hKeys(...) => key -> {}", key);
        Set<Object> entryKeys = redisTemplate.opsForHash().keys(key);
        log.info("hKeys(...) => entryKeys -> {}", entryKeys);
        return entryKeys;
    }

    /**
     * 取(key对应的)hash中的所有entryValue
     *
     * @param key
     * @return
     */
    public List<Object> hValues(String key) {
        log.info("hValues(...) => key -> {}", key);
        List<Object> entryValues = redisTemplate.opsForHash().values(key);
        log.info("hValues(...) => entryValues -> {}", entryValues);
        return entryValues;
    }

    /**
     * 获取(key对应的)hash中的所有entry的数量
     *
     * @param key
     * @return
     */
    public long hSize(String key) {
        log.info("hSize(...) => key -> {}", key);
        Long count = redisTemplate.opsForHash().size(key);
        log.info("hSize(...) => count -> {}", count);
        return count;
    }

    /*========== List操作 ==========*/


    /**
     * 从左端推入元素进列表
     *
     * @param key
     * @param item
     * @return
     */
    public long lLeftPush(String key, String item) {
        log.info("lLeftPush(...) => key -> {}, item -> {}", key, item);
        Long size = redisTemplate.opsForList().leftPush(key, item);
        log.info("lLeftPush(...) => size -> {}", size);
        return size;
    }

    /**
     * 从左端批量推入元素进列表
     *
     * @param key
     * @param items
     * @return
     */
    public long lLeftPushAll(String key, String... items) {
        log.info("lLeftPushAll(...) => key -> {}, items -> {}", key, items);
        Long size = redisTemplate.opsForList().leftPushAll(key, items);
        log.info("lLeftPushAll(...) => size -> {}", size);
        return size;
    }

    /**
     * 如果redis中存在key, 则从左端批量推入元素进列表;
     *
     * @param key
     * @param item
     * @return
     */
    public long lLeftPushIfPresent(String key, String item) {
        log.info("lLeftPushIfPresent(...) => key -> {}, item -> {}", key, item);
        Long size = redisTemplate.opsForList().leftPushIfPresent(key, item);
        log.info("lLeftPushIfPresent(...) => size -> {}", size);
        return size;
    }


    /**
     * 从list右侧推入元素
     *
     * @param key
     * @param item
     * @return
     */
    public long lRightPush(String key, String item) {
        log.info("lRightPush(...) => key -> {}, item -> {}", key, item);
        Long size = redisTemplate.opsForList().rightPush(key, item);
        log.info("lRightPush(...) => size -> {}", size);
        return size;
    }

    /**
     * 从右侧批量添加元素
     *
     * @param key
     * @param items
     * @return
     */
    public long lRightPushAll(String key, String... items) {
        log.info("lRightPushAll(...) => key -> {}, items -> {}", key, items);
        Long size = redisTemplate.opsForList().rightPushAll(key, items);
        log.info("lRightPushAll(...) => size -> {}", size);
        return size;
    }

    /**
     * 从左侧移出(key对应的)list中的第一个元素, 并将该元素返回
     *
     * @param key
     * @return
     */
    public Object lLeftPop(String key) {
        log.info("lLeftPop(...) => key -> {}", key);
        Object item = redisTemplate.opsForList().leftPop(key);
        log.info("lLeftPop(...) => item -> {}", item);
        return item;
    }

    /**
     * 从list右侧移出元素
     *
     * @param key
     * @return
     */
    public Object lRightPop(String key) {
        log.info("lRightPop(...) => key -> {}", key);
        Object item = redisTemplate.opsForList().rightPop(key);
        log.info("lRightPop(...) => item -> {}", item);
        return item;
    }

    /**
     * 设置(key对应的)list中对应索引位置index处的元素为item
     *
     * @param key
     * @param index
     * @param item
     */
    public void lSet(String key, long index, String item) {
        log.info("lSet(...) => key -> {}, index -> {}, item -> {}", key, index, item);
        redisTemplate.opsForList().set(key, index, item);
    }

    /**
     * 获取(key对应的)list中索引在[start, end]之间的item集
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lRange(String key, long start, long end) {
        log.info("lRange(...) => key -> {}, start -> {}, end -> {}", key, start, end);
        List<Object> result = redisTemplate.opsForList().range(key, start, end);
        log.info("lRange(...) => result -> {}", result);
        return result;
    }

    /**
     * 获取(key对应的)list的size
     *
     * @param key
     * @return
     */
    public long lSize(String key) {
        log.info("lSize(...) => key -> {}", key);
        Long size = redisTemplate.opsForList().size(key);
        log.info("lSize(...) => size -> {}", size);
        return size;
    }


    /*========== Set操作 ==========*/


    /**
     * 向(key对应的)set中添加items
     *
     * @param key
     * @param items
     * @return
     */
    public long sAdd(String key, String... items) {
        log.info("sAdd(...) => key -> {}, items -> {}", key, items);
        Long count = redisTemplate.opsForSet().add(key, items);
        log.info("sAdd(...) => count -> {}", count);

        return count;
    }


    /**
     * 从(key对应的)set中删除items
     *
     * @param key
     * @param items
     * @return
     */
    public long sRemove(String key, Object... items) {
        log.info("sRemove(...) => key -> {}, items -> {}", key, items);
        Long count = redisTemplate.opsForSet().remove(key, items);
        log.info("sRemove(...) => count -> {}", count);
        return count;
    }

    /**
     * 获取(key对应的)set中的元素个数
     *
     * @param key
     * @return
     */
    public long sSize(String key) {
        log.info("sSize(...) => key -> {}", key);
        Long size = redisTemplate.opsForSet().size(key);
        log.info("sSize(...) => size -> {}", size);
        return size;
    }

    /**
     * 判断(key对应的)set中是否含有item
     *
     * @param key
     * @param item
     * @return
     */
    public boolean sIsMember(String key, Object item) {
        log.info("sSize(...) => key -> {}, size -> {}", key, item);
        Boolean result = redisTemplate.opsForSet().isMember(key, item);
        log.info("sSize(...) => result -> {}", result);

        return result;
    }


    /**
     * 获取两个(key对应的)Set的交集
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<Object> sIntersect(String key, String otherKey) {
        log.info("sIntersect(...) => key -> {}, otherKey -> {}", key, otherKey);
        Set<Object> intersectResult = redisTemplate.opsForSet().intersect(key, otherKey);
        log.info("sIntersect(...) => intersectResult -> {}", intersectResult);
        return intersectResult;
    }


    /**
     * 获取两个(key对应的)Set的并集
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<Object> sUnion(String key, String otherKey) {
        log.info("sUnion(...) => key -> {}, otherKey -> {}", key, otherKey);
        Set<Object> unionResult = redisTemplate.opsForSet().union(key, otherKey);
        log.info("sUnion(...) => unionResult -> {}", unionResult);
        return unionResult;
    }


    /**
     * 差集
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<Object> sDifference(String key, String otherKey) {
        log.info("sDifference(...) => key -> {}, otherKey -> {}",
                key, otherKey);
        Set<Object> differenceResult = redisTemplate.opsForSet().difference(key, otherKey);
        log.info("sDifference(...) => differenceResult -> {}", differenceResult);
        return differenceResult;
    }


    /**
     * 获取key对应的set
     *
     * @param key
     * @return
     */
    public Set<Object> sMembers(String key) {
        log.info("sMembers(...) => key -> {}", key);
        Set<Object> members = redisTemplate.opsForSet().members(key);
        log.info("sMembers(...) => members -> {}", members);
        return members;
    }

    /*-------------Zset操作--------------------*/

    public boolean zAdd(String key, String item, double score) {
        log.info("zAdd(...) => key -> {}, item -> {}, score -> {}", key, item, score);
        Boolean result = redisTemplate.opsForZSet().add(key, item, score);
        log.info("zAdd(...) => result -> {}", result);
        return result;
    }

    /**
     * 从(key对应的)zset中移除项
     *
     * @param key
     * @param items
     * @return
     */
    public long zRemove(String key, Object... items) {
        log.info("zRemove(...) => key -> {}, items -> {}", key, items);
        Long count = redisTemplate.opsForZSet().remove(key, items);
        log.info("zRemove(...) => count -> {}", count);
        return count;
    }

    /**
     * 增/减 (key对应的zset中,)item的分数值
     *
     * @param key
     * @param item
     * @param delta
     * @return
     */
    public double zIncrementScore(String key, String item, double delta) {
        log.info("zIncrementScore(...) => key -> {}, item -> {}, delta -> {}", key, item, delta);
        Double scoreValue = redisTemplate.opsForZSet().incrementScore(key, item, delta);
        log.info("zIncrementScore(...) => scoreValue -> {}", scoreValue);
        return scoreValue;
    }

    /**
     * 返回item在(key对应的)zset中的(按score从小到大的)排名
     *
     * @param key
     * @param item
     * @return
     */
    public long zRank(String key, Object item) {
        log.info("zRank(...) => key -> {}, item -> {}", key, item);
        Long rank = redisTemplate.opsForZSet().rank(key, item);
        log.info("zRank(...) => rank -> {}", rank);
        return rank;
    }

    /**
     * 返回item在(key对应的)zset中的(按score从大到小的)排名
     *
     * @param key
     * @param item
     * @return
     */
    public long zReverseRank(String key, Object item) {
        log.info("zReverseRank(...) => key -> {}, item -> {}", key, item);
        Long reverseRank = redisTemplate.opsForZSet().reverseRank(key, item);
        log.info("zReverseRank(...) => reverseRank -> {}", reverseRank);
        return reverseRank;
    }

    /**
     * 根据索引位置， 获取(key对应的)zset中排名处于[start, end]中的item项集
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zRange(String key, long start, long end) {
        log.info("zRange(...) => key -> {}, start -> {}, end -> {}", key, start, end);
        Set<Object> result = redisTemplate.opsForZSet().range(key, start, end);
        log.info("zRange(...) => result -> {}", result);
        return result;
    }

    /**
     * 统计(key对应的)zset中item的个数
     *
     * @param key
     * @return
     */
    public long zSize(String key) {
        log.info("zSize(...) => key -> {}", key);
        Long size = redisTemplate.opsForZSet().size(key);
        log.info("zSize(...) => size -> {}", size);
        return size;
    }


    /**
     * 统计(key对应的)zset中item的个数
     *
     * @param key
     * @return
     */
    public long zZCard(String key) {
        log.info("zZCard(...) => key -> {}", key);
        Long size = redisTemplate.opsForZSet().zCard(key);
        log.info("zZCard(...) => size -> {}", size);
        return size;
    }

    /**
     * 统计(key对应的)zset中指定item的score
     *
     * @param key
     * @param item
     * @return
     */
    public double zScore(String key, Object item) {
        log.info("zScore(...) => key -> {}, item -> {}", key, item);
        Double score = redisTemplate.opsForZSet().score(key, item);
        log.info("zScore(...) => score -> {}", score);

        return score;
    }

}
