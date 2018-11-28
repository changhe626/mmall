package com.mmall.util;

import com.mmall.common.RedisSharedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisSharedPoolUtil {


    public static String set(String key,String value){
        ShardedJedis jedis=null;
        String result=null;
        try{
            jedis= RedisSharedPool.getJedis();
            result=jedis.set(key,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }


    public static String get(String key){
        ShardedJedis jedis=null;
        String result=null;
        try{
            jedis=RedisSharedPool.getJedis();
            result=jedis.get(key);
        }catch (Exception e){
            log.error("get key:{} value:{} error",key,e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }


    public static Long delete(String key){
        ShardedJedis jedis=null;
        Long result=null;
        try{
            jedis=RedisSharedPool.getJedis();
            result=jedis.del(key);
        }catch (Exception e){
            log.error("get key:{} value:{} error",key,e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }


    public static String setEx(String key,String value,int time){
        ShardedJedis jedis=null;
        String result=null;
        try{
            jedis=RedisSharedPool.getJedis();
            result=jedis.setex(key,time,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }


    public static Long expire(String key,int time){
        ShardedJedis jedis=null;
        Long result=null;
        try{
            jedis=RedisSharedPool.getJedis();
            result=jedis.expire(key,time);
        }catch (Exception e){
            log.error("set key:{}  error",key,e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    public static Long setnx(String key,String value){
        ShardedJedis jedis=null;
        Long result=null;
        try{
            jedis= RedisSharedPool.getJedis();
            result=jedis.setnx(key,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }


    public static String getSet(String key,String value){
        ShardedJedis jedis=null;
        String result=null;
        try{
            jedis= RedisSharedPool.getJedis();
            result=jedis.getSet(key,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {

        set("age","2");
        String age = get("age");
        System.out.println(age);
        delete("age");

    }




}
