package com.changhong.db.Redis;


import com.changhong.db.SpringApplicationContext;
import com.changhong.util.UUIDUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by ava on 2017/5/16.
 * email:zhyx2014@yeah.net
 */
public class RedisHelper {
    private static final RedisTemplate template = SpringApplicationContext.getRedisTemplate();

    protected static void expire(String field, long sec) {
        template.expire(field, sec, TimeUnit.SECONDS);
    }

    /*value*/
    protected static String get(String key) {
        BoundValueOperations<String, String> bound = template.boundValueOps(key);
        return bound.get();
    }
    protected static void delete(String key) {
        template.delete(key);
    }
    protected static void set(String key, String value) {
        template.boundValueOps(key).set(value);
    }

    protected static void setAndExpire(String key, String value, long sec) {
        set(key, value);
        expire(key, sec);
    }

    /*map*/
    protected static void put(String field, String key, String value) {
        BoundHashOperations bound = template.boundHashOps(field);
        bound.put(key, value);
    }

    protected static Object hget(String field, String key) {
        BoundHashOperations operations = template.boundHashOps(field);
        return operations.get(key);
    }

    protected static List<String> hmget(String field, Set<String> keys) {
        BoundHashOperations operations = template.boundHashOps(field);
        return operations.multiGet(keys);
    }

    protected static void putAll(String field, Map<String, String> map) {
        BoundHashOperations operations = template.boundHashOps(field);
        operations.putAll(map);
    }
    protected static Map<String ,String> entries(String field){
        BoundHashOperations operations=template.boundHashOps(field);
        return operations.entries();
    }
    protected static Object getMap(String field, String key) {
        BoundHashOperations bound = template.boundHashOps(field);
        return bound.get(key);
    }

    protected static Map<String, String> innerMap(String field, String... keys) {
        Map<String, String> figures = new HashMap<>();
        BoundHashOperations bound = template.boundHashOps(field);
        for (String key : keys) {
            String value = (String) bound.get(key);
            if (StringUtils.isEmpty(value)) continue;
            figures.put(key, value);
        }
        return figures;
    }

    /*set*/
    protected static void addSet(String field, String value) {
        BoundSetOperations bound = template.boundSetOps(field);
        bound.add(value);
    }

    protected static boolean isMemberSet(String field, String value) {
        BoundSetOperations bound = template.boundSetOps(field);
        return bound.isMember(value);
    }

    protected static void addSetAll(String field, Object... values) {
        BoundSetOperations operations = template.boundSetOps(field);
        operations.add(values);
    }

    protected static Set<String> interSet(String field, Set<String> set) {
        try {
            if (CollectionUtils.isEmpty(set)) return null;
            BoundSetOperations operations = template.boundSetOps(field);
            String field1 = "semantic:" + UUIDUtils.uuid();
            BoundSetOperations operations1 = template.boundSetOps(field1);
            operations1.add(set.toArray());
            Set result = operations.intersect(field1);
            template.delete(field1);
            return result;
//            return operations.intersect(set);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static long countSet(String field) {
        BoundSetOperations bound = template.boundSetOps(field);
        return bound.size();
    }
}
