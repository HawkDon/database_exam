package com.oliverloenning.redis;

import redis.clients.jedis.Jedis;


public class Service {
    final private static Jedis jedis = new Jedis();

    public static Jedis getJedis() {
        return jedis;
    }
}
