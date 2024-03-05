package com.gsl.tech.mockorder.impl;

import com.gsl.tech.mockorder.ConsumeModeService;
import com.gsl.tech.mockorder.MailWorkHandler;
import com.gsl.tech.mockorder.SmsEventHandler;
import org.springframework.stereotype.Service;

/**
 * @description: 方法实现
 * @date 2021/5/23 11:05
 */
@Service("independentAndShareModeService")
public class IndependentAndShareModeServiceImpl extends ConsumeModeService {

    @Override
    protected void disruptorOperate() {
        // 调用handleEventsWith，表示创建的多个消费者，每个都是独立消费的
        // 这里创建一个消费者，短信服务
        disruptor.handleEventsWith(new SmsEventHandler(eventCountPrinter));

        // mailWorkHandler1模拟一号邮件服务器
        MailWorkHandler mailWorkHandler1 = new MailWorkHandler(eventCountPrinter);

        // mailWorkHandler2模拟一号邮件服务器
        MailWorkHandler mailWorkHandler2 = new MailWorkHandler(eventCountPrinter);

        // 调用handleEventsWithWorkerPool，表示创建的多个消费者以共同消费的模式消费
        disruptor.handleEventsWithWorkerPool(mailWorkHandler1, mailWorkHandler2);
    }
}
