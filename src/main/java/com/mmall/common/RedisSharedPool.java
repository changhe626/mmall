package com.mmall.common;

import com.google.common.collect.Lists;
import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisSharedPool {
    /**
     * 尽量加上默认值,防止被破坏了
     */
    //jedis连接池
    private static ShardedJedisPool pool;
    //最大数量
    private static int maxTotal= PropertiesUtil.getIntProperty("redis.max.total","20");
    //最大空闲数量
    private static int maxIdle=PropertiesUtil.getIntProperty("redis.max.idle","2");
    //最小空闲数量
    private static int minIdle=PropertiesUtil.getIntProperty("redis.min.idle","10");
    //获取一个实例的时候,是否需要测试
    private static Boolean testOnBorrow=PropertiesUtil.getBooleanProperty("redis.test.borrow","true");
    //放回一个实例的时候,是否需要测试
    private static Boolean testOnReturn=PropertiesUtil.getBooleanProperty("redis.test.return","true");

    private static int port1= PropertiesUtil.getIntProperty("redis1.port","6379");
    private static int port2= PropertiesUtil.getIntProperty("redis2.port","6380");
    private static String ip1= PropertiesUtil.getProperty("redis1.ip");
    private static String ip2= PropertiesUtil.getProperty("redis2.ip");

    static {
        initPool();
    }

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        //链接耗尽的时候,时候阻塞,false抛出异常,true就是超时异常
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(ip1,port1,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(ip2,port2,1000*2);
        List<JedisShardInfo> objects = Lists.newArrayList(info1,info2);

        pool=new ShardedJedisPool(config,objects, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

    }


    public static ShardedJedis getJedis(){
        return pool.getResource();
    }


    public static void returnResource(ShardedJedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        if(jedis!=null){
            pool.returnBrokenResource(jedis);
        }
    }


    public static void main(String[] args) {

        ShardedJedis jedis = pool.getResource();
        for (int i = 0; i < 10; i++) {
            jedis.set("key"+i,"value"+i);
        }
        returnResource(jedis);
        //临时调用,销毁连接池中所有链接
        //pool.destroy();
    }







}
