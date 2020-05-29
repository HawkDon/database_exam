package com.oliverloenning.redis;

import redis.clients.jedis.Jedis;


public class Service {
    final private static Jedis jedis = new Jedis("redis", 6379);

    public static Jedis getJedis() {
        return jedis;
    }
}
