package com.changhong.util;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
public class JedisUtil {
	private static JedisPool jedisPool;
	private static Object lock = new Object();
	static {
		synchronized (lock) {
			if(jedisPool == null){
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxIdle(2000);
				config.setMaxWaitMillis(2000l);
				config.setTestOnBorrow(true);
				config.setTestOnReturn(true);
				config.setTestWhileIdle(true);
				config.setMinEvictableIdleTimeMillis(60000l);
				config.setTimeBetweenEvictionRunsMillis(3000l);
				config.setNumTestsPerEvictionRun(-1);
				if(StringUtils.isBlank(Config.redisPassword)){
					jedisPool = new JedisPool(config, Config.redisHost,Config.redisPort,60000);
				}else{
					jedisPool = new JedisPool(config, Config.redisHost,Config.redisPort,60000,Config.redisPassword);
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		JedisUtil.hset("aaa", "bbb", "ccc", 3);
	}
	
	/**
	 *
	 * @param jedis
    */
	public static void close(Jedis jedis) {
		try {
			if(jedis == null){return;}
			jedis.close();
		} catch (Exception e) {
			if (jedis.isConnected()) {
				jedis.quit();
				jedis.disconnect();
			}
		}
		try {
			Thread.sleep(100L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 */
	public static Jedis getResource(){
		return jedisPool.getResource();
	}
	
	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	public static String get(String key, int index) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return value;
	}

	/**
	 * 获取数据
	 *
	 * @param key
	 * @return
	 */
	public static byte[] get(byte[] key, int index) {
		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return value;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @param index
     */
	public static void set(byte[] key, byte[] value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 *
	 * @param key
	 * @param value
	 * @param index
    */
	public static void set(String key, String value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @param time
     * @param index
     */
	public static void set(byte[] key, byte[] value, int time, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 *
	 * @param key
	 * @param value
	 * @param time
    * @param index
    */
	public static void set(String key, String value, int time, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}

	/**
	 *
	 * @param key
	 * @param field
	 * @param value
     * @param index
     */
	public static void hset(byte[] key, byte[] field, byte[] value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}

	/**
	 *
	 * @param key
	 * @param field
	 * @param value
     * @param index
     */
	public static void hset(String key, String field, String value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}
	/**
	 * 获取数据
	 *
	 * @param key
	 * @return
	 */
	public static String hget(String key, String field, int index) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			value = jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return value;
	}
	/**
	 * 获取数据
	 *
	 * @param key
	 * @return
	 */
	public static byte[] hget(byte[] key, byte[] field, int index) {
		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			value = jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
		return value;
	}

	/**
	 *
	 * @param key
	 * @param field
	 * @param index
     */
	public static void hdel(byte[] key, byte[] field, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 *
	 * @param key
	 * @param field
	 * @param index
    */
	public static void hdel(String key, String field, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 存储REDIS队列 顺序存储
	 * @param byte[] key reids键名
	 * @param byte[] value 键�??
	 */
	public static void lpush(byte[] key, byte[] value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.lpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	/**
	 * 存储REDIS队列 顺序存储
	 * @param String key reids键名
	 * @param String value 键�??
	 */
	public static void lpush(String key, String value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.lpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 存储REDIS队列 反向存储
	 * @param byte[] key reids键名
	 * @param byte[] value 键�??
	 */
	public static void rpush(byte[] key, byte[] value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.rpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 存储REDIS队列 反向存储
	 * @param byte[] key reids键名
	 * @param byte[] value 键�??
	 */
	public static void rpush(String key, String value, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.rpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 将列�? source 中的�?后一个元�?(尾元�?)弹出，并返回给客户端
	 * @param byte[] key reids键名
	 * @param byte[] value 键�??
	 */
	public static void rpoplpush(byte[] key, byte[] destination, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.rpoplpush(key, destination);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 将列�? source 中的�?后一个元�?(尾元�?)弹出，并返回给客户端
	 * @param byte[] key reids键名
	 * @param byte[] value 键�??
	 */
	public static void rpoplpush(String key, String destination, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.rpoplpush(key, destination);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 * 获取队列数据
	 * @param byte[] key 键名
	 * @return
	 */
	public static List<byte[]> lpopList(byte[] key, int index) {
		List<byte[]> list = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			list = jedis.lrange(key, 0, -1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return list;
	}
	
	/**
	 * 获取队列数据
	 * @param byte[] key 键名
	 * @return
	 */
	public static List<String> lpopList(String key, int index) {
		List<String> list = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			list = jedis.lrange(key, 0, -1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return list;
	}
	
	/**
	 * 获取队列数据
	 * @param byte[] key 键名
	 * @return
	 */
	public static byte[] rpop(byte[] key, int index) {
		byte[] bytes = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			bytes = jedis.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return bytes;
	}
	/**
	 * 获取队列数据
	 * @param String key 键名
	 * @return
	 */
	public static String rpop(String key, int index) {
		String bytes = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			bytes = jedis.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return bytes;
	}

	/**
	 * 向hash中添加数�?
	 * @param key
	 * @param hash
	 * @param index
     */
	public static void hmset(Object key, Map<String, String> hash, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.hmset(key.toString(), hash);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}

	/**
	 *
	 * @param key
	 * @param hash
	 * @param time
     * @param index
     */
	public static void hmset(Object key, Map<String, String> hash, int time, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.hmset(key.toString(), hash);
			jedis.expire(key.toString(), time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}

	/**
	 * 获取数据
	 * @param key
	 * @param index
	 * @param fields
     * @return
     */
	public static List<String> hmget(Object key, int index, String... fields) {
		List<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			result = jedis.hmget(key.toString(), fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return result;
	}

	/**
	 *
	 * @param key
	 * @param index
     * @return
     */
	public static Set<String> hkeys(String key, int index) {
		Set<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			result = jedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return result;
	}

	/**
	 *
	 * @param key
	 * @param from
	 * @param to
	 * @param index
     * @return
     */
	public static List<byte[]> lrange(byte[] key, int from, int to, int index) {
		List<byte[]> result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			result = jedis.lrange(key, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 *
	 * @param key
	 * @param from
	 * @param to
	 * @param index
    * @return
    */
	public static List<String> lrange(String key, int from, int to, int index) {
		List<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			result = jedis.lrange(key, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return result;
	}
	

	/**
	 *
	 * @param key
	 * @param index
     * @return
     */
	public static Map<byte[], byte[]> hgetAll(byte[] key, int index) {
		Map<byte[], byte[]> result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 *
	 * @param key
	 * @param index
    * @return
    */
	public static Map<String, String> hgetAll(String key, int index) {
		Map<String, String> result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return result;
	}

	/**
	 *
	 * @param key
	 * @param index
     */
	public static void del(byte[] key, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}
	
	/**
	 *
	 * @param key
	 * @param index
    */
	public static void del(String key, int index) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
	}

	/**
	 *
	 * @param key
	 * @param index
     * @return
     */
	public static long llen(byte[] key, int index) {
		long len = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return len;
	}
	
	/**
	 *
	 * @param key
	 * @param index
    * @return
    */
	public static long llen(String key, int index) {
		long len = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index);
			jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return len;
	}

	/**
	 *
	 * @param key
	 * @param fieldName
	 * @param index
     * @return
     */
	public static boolean hexists(String key, String fieldName, int index) {
		Jedis jedis = null;
		Boolean isIn = false;
		try {
			jedis = getResource();
			jedis.select(index);
			isIn = jedis.hexists(key, fieldName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return isIn;
	}
}