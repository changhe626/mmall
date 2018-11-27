package com.mmall.task;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    //每个1分钟执行
    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1(){
        log.info("开始定时关闭订单的任务");
        int hour= PropertiesUtil.getIntProperty("close.order.task.time.out","2");
        iOrderService.closeOrder(hour);


    }



}
