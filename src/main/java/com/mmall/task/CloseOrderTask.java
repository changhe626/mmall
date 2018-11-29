package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisSharedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private RedissonManager redissonManager;

    //每个1分钟执行
    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1(){
        log.info("开始定时关闭订单的任务");
        int hour= PropertiesUtil.getIntProperty("close.order.task.time.out","2");
        iOrderService.closeOrder(hour);
        log.info("完成定时关闭订单的任务");
    }



    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2(){
        log.info("开始定时关闭订单的任务");
        long lockTimeOut=PropertiesUtil.getIntProperty("lock.time.out","5000");

        Long setnx = RedisSharedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        if(setnx !=null && setnx.intValue()==1){
            //返回值是1,表示成功,获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("完成定时关闭订单的任务");
    }


    /**
     * 自定义分布式锁
     */
    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3(){
        log.info("开始定时关闭订单的任务");
        long lockTimeOut=PropertiesUtil.getIntProperty("lock.time.out","5000");
        Long setnx = RedisSharedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        if(setnx !=null && setnx.intValue()==1){
            //返回值是1,表示成功,获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            //为获取到锁,继续判断,看能否重置,并且获取到锁.
            String s = RedisSharedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(s!=null && System.currentTimeMillis()>Long.parseLong(s)){
                //不为空,而且已经超时了  //重置锁
                //上面的s 和这个set有可能不一样,因为是集群tomcat.返回给定的key,->旧值判断,是否可以获取锁
                //当key没有旧值时,即key不存在,返回nil->获取锁
                //这里我们set了一个新value,获取旧值
                String set = RedisSharedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
                if(set== null || (set!=null && StringUtils.equals(s,set))){
                    //真正获取到锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else{
                    log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else {
                log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
            log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("完成定时关闭订单的任务");
    }


    /**
     * 使用redisson来进行分布式锁的获取
     */
    @Scheduled(cron="0 */1 * * * ?")
    public void closeOrderTaskV4(){
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            if(getLock = lock.tryLock(0,50, TimeUnit.SECONDS)){
                log.info("Redisson获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
                //iOrderService.closeOrder(hour);
            }else{
                log.info("Redisson没有获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson分布式锁获取异常",e);
        } finally {
            if(!getLock){
                return;
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁");
        }
    }


    /**
     * 设置过期时间,有效期是5s,防止死锁
     */
    private void closeOrder(String lockName){
        RedisSharedPoolUtil.expire(lockName,5);
        log.info("获取{},ThreadName:{}",lockName,Thread.currentThread().getName());
        int hour= PropertiesUtil.getIntProperty("close.order.task.time.out","2");
        iOrderService.closeOrder(hour);
        RedisSharedPoolUtil.delete(lockName);
    }


    /**
     * V2 版本时候...
     * 在使用tomcat 的shutdown.sh 的命令的时候,会调用这个方法,
     * 进行锁的删除,锁的释放,防止死锁.
     * 但是如果是直接kill进程还是不会执行
     */
    @PreDestroy
    public void destoryLock(){
        RedisSharedPoolUtil.delete(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }



}
