package com.project.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//@Component
public enum RedisUtil {

    INSTANCE;
    private final JedisPool pool;

/*    @Value("${redis.url}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.timeout}")
    private int timeout;
    @Value("${redis.password}")
    private String password;*/

//    RedisUtil() {
//        pool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
//    }
    private static final String HOST = "13.125.172.40";
    private static final int PORT = 6379;
    private static final int TIMEOUT = 1000;
    private static final String PASSWORD = "rkdalsrb123$";

    RedisUtil() {
        pool = new JedisPool(new JedisPoolConfig(), HOST, PORT, TIMEOUT, PASSWORD);
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long del(String key) {
        Jedis jedis = null;
        long delCnt = 0;
        try {
            jedis = pool.getResource();
            delCnt = jedis.del(key);
            return delCnt;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
