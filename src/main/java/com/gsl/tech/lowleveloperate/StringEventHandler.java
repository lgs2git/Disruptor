package com.gsl.tech.lowleveloperate;

import com.lmax.disruptor.EventHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.function.Consumer;

@Slf4j
public class StringEventHandler implements EventHandler<StringEvent> {

    public StringEventHandler(Consumer<?> consumer) {
        this.consumer = consumer;
    }

    // 外部可以传入Consumer实现类，每处理一条消息的时候，consumer的accept方法就会被执行一次
    private Consumer<?> consumer;

    @Override
    public void onEvent(StringEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("sequence [{}], endOfBatch [{}], event : {}", sequence, endOfBatch, event);

        // 这里延时100ms，模拟消费事件的逻辑的耗时
        Thread.sleep(100);

        // 如果外部传入了consumer，就要执行一次accept方法
        if (null!=consumer) {
            consumer.accept(null);
        }
    }
}
