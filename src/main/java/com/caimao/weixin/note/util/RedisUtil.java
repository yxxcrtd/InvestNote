package com.caimao.weixin.note.util;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, ?> redis;

	public void set(final byte[] key, final byte[] value, final long liveTime) {
		redis.execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
		}
		});
	}

	public Boolean setnx(final String key, final String value, final long liveTime) {
		return redis.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				Boolean res = connection.setNX(key.getBytes(), value.getBytes());
				if (liveTime > 0) {
					connection.expire(key.getBytes(), liveTime);
				}
				return res;
		}
		});
	}

	public void set(String key, String value, long liveTime) {
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	public Object get(final String key) {
		return redis.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				byte[] bvalue = conn.get(key.getBytes());
				if (null == bvalue) {
					return null;
				} else {
					return redis.getStringSerializer().deserialize(bvalue);
				}
		}
		});
	}

	public Object hGet(final String key, final String field) {
		return redis.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				byte[] bvalue = conn.hGet(key.getBytes(), field.getBytes());
				if (null == bvalue) {
					return null;
				} else {
					return redis.getStringSerializer().deserialize(bvalue);
				}
	}
		});
	}

	public void hSet(final String key, final String field, final String value) {
		redis.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn) throws DataAccessException {
				conn.hSet(key.getBytes(), field.getBytes(), value.getBytes());
				return null;
			}
		});
	}

	public void rPush(final String key, final String value) {
		redis.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn) throws DataAccessException {
				conn.rPush(key.getBytes(), value.getBytes());
				return null;
			}
		});
	}

	// 设置某一key的超时时间，单位秒
	public void expired(String key, long timeout) {
		redis.expire(key, timeout, TimeUnit.SECONDS);
	}

	public void del(final String key) {
		redis.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				return conn.del(key.getBytes());
			}
		});
	}

	/**
	 * key以秒为单位,返回给定 key 的剩余生存时间
	 *
	 * @param key
	 */
	public Long ttl(final String key) {
		return redis.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.ttl(key.getBytes());
			}
		});
	}

	/**
	 * 对一个key的value加一
	 *
	 * @param key
	 */
	public Long incr(final String key) {
		return redis.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.incr(key.getBytes());
			}
		});
	}

	public Long getTtl(final String key) {
		return (Long) redis.execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection conn) {
				Long value = conn.ttl(key.getBytes());
				return value;
			}
		});
	}

	public Object sAdd(final String key, final String value) {
		return redis.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) throws DataAccessException {
				return conn.sAdd(key.getBytes(), value.getBytes());
			}
		});
	}

	public Object sIsMember(final String key, final String value) {
		return redis.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) throws DataAccessException {
				return conn.sIsMember(key.getBytes(), value.getBytes());
			}
		});
	}

}
