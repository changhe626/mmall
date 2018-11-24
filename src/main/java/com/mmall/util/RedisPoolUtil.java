package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {


    public static String set(String key,String value){
        Jedis jedis=null;
        String result=null;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.set(key,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static String get(String key){
        Jedis jedis=null;
        String result=null;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.get(key);
        }catch (Exception e){
            log.error("get key:{} value:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static Long delete(String key){
        Jedis jedis=null;
        Long result=null;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.del(key);
        }catch (Exception e){
            log.error("get key:{} value:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static String setEx(String key,String value,int time){
        Jedis jedis=null;
        String result=null;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.setex(key,time,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static Long expire(String key,int time){
        Jedis jedis=null;
        Long result=null;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.expire(key,time);
        }catch (Exception e){
            log.error("set key:{}  error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {

        set("age","2");
        String age = get("age");
        System.out.println(age);
        delete("age");

    }




}
