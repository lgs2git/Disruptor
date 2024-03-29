package com.gsl.tech.mockorder;

import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * @description: 定义事件处理逻辑
 * @date 2021/5/28 23:52
 */
@Slf4j
public class OrderWorkHandler implements WorkHandler<OrderEvent> {

    public OrderWorkHandler(Consumer<?> consumer) {
        this.consumer = consumer;
    }

    // 外部可以传入Consumer实现类，每处理一条消息的时候，consumer的accept方法就会被执行一次
    private Consumer<?> consumer;

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        log.info("work handler event : {}", event);

        // 这里延时100ms，模拟消费事件的逻辑的耗时
        Thread.sleep(100);

        // 如果外部传入了consumer，就要执行一次accept方法
        if (null!=consumer) {
            consumer.accept(null);
        }
    }
}
