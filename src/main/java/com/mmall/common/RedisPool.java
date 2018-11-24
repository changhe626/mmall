package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    /**
     * 尽量加上默认值,防止被破坏了
     */
    //jedis连接池
    private static JedisPool pool;
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

    private static int port= PropertiesUtil.getIntProperty("redis.port","6379");
    private static String ip= PropertiesUtil.getProperty("redis.ip");

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

        pool=new JedisPool(config,ip,port,1000*2);
    }


    public static Jedis getJedis(){
        return pool.getResource();
    }


    public static void returnResource(Jedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }

    public static void returnBrokenResource(Jedis jedis){
        if(jedis!=null){
            pool.returnBrokenResource(jedis);
        }
    }


    public static void main(String[] args) {

        Jedis jedis = pool.getResource();
        jedis.set("name","zhaojun");
        returnResource(jedis);
        //临时调用,销毁连接池中所有链接
        pool.destroy();

    }







}
