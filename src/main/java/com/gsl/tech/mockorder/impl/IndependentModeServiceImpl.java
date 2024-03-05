package com.gsl.tech.mockorder.impl;

import com.gsl.tech.mockorder.ConsumeModeService;
import com.gsl.tech.mockorder.MailEventHandler;
import com.gsl.tech.mockorder.SmsEventHandler;
import org.springframework.stereotype.Service;

/**
 * @description: 方法实现
 * @date 2021/5/23 11:05
 */
@Service("independentModeService")
public class IndependentModeServiceImpl extends ConsumeModeService {

    @Override
    protected void disruptorOperate() {
        // 调用handleEventsWith，表示创建的多个消费者，每个都是独立消费的
        // 这里创建两个消费者，一个是短信的，一个是邮件的
        disruptor.handleEventsWith(new SmsEventHandler(eventCountPrinter), new MailEventHandler(eventCountPrinter));
    }
}
