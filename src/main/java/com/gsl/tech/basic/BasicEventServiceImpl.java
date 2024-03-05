package com.gsl.tech.basic;

import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;


@Service
@Slf4j
public class BasicEventServiceImpl implements BasicEventService {

    private static final int BUFFER_SIZE = 16;

    private Disruptor<StringEvent> disruptor;

    private StringEventProducer producer;

    /**
     * 统计消息总数
     */
    private final AtomicLong eventCount = new AtomicLong();

    @PostConstruct
    private void init() {
        // 实例化
        disruptor = new Disruptor<>(new StringEventFactory(),
                BUFFER_SIZE,
                new CustomizableThreadFactory("event-handler-"));

        // 准备一个匿名类，传给disruptor的事件处理类，
        // 这样每次处理事件时，都会将已经处理事件的总数打印出来
        Consumer<?> eventCountPrinter = new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                log.info("类BasicEventServiceImpl：accept()");
                long count = eventCount.incrementAndGet();
                log.info("receive [{}] event", count);
            }
        };

        // 指定处理类
        disruptor.handleEventsWith(new StringEventHandler(eventCountPrinter));

        //指定多消费者
//        StringEventHandler[] stringEventHandler = new StringEventHandler[3];
//        for (int i = 0; i < stringEventHandler.length; i++) {
//            stringEventHandler[i] = new StringEventHandler(eventCountPrinter);
//        }
//        disruptor.handleEventsWith(stringEventHandler);

        // 启动
        disruptor.start();

        // 生产者
        producer = new StringEventProducer(disruptor.getRingBuffer());
    }

    @Override
    public void publish(String value) {
        producer.onData(value);
    }

    /**
     * 发布一个事件，方式2
     */
    @Override
    public void publishEvent(String value) {
        producer.onDataByLambda(value);
    }

    @Override
    public long eventCount() {
        return eventCount.get();
    }
}
